/*
 * Copyright Vladimir Zakharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/// <reference path="DiagramEditorController.ts" />
/// <reference path="../model/DiagramPaper.ts" />
/// <reference path="../model/DiagramElement.ts" />
/// <reference path="../model/PaletteTypes.ts" />
/// <reference path="../model/DiagramNode.ts" />
/// <reference path="../model/DefaultDiagramNode.ts" />
/// <reference path="../model/commands/Command.ts"/>
/// <reference path="../model/commands/PaperCommandFactory.ts" />
/// <reference path="../../vendor.d.ts" />

class PaperController {

    private diagramEditorController: DiagramEditorController;
    private paper: DiagramPaper;
    private currentElement: DiagramElement;
    private clickFlag : boolean;
    private rightClickFlag : boolean;
    private gesturesController: GesturesController;
    private undoRedoController: UndoRedoController;
    private lastCellMouseDownPosition: {x: number, y: number};
    private paperCommandFactory: PaperCommandFactory;
    private contextMenuId = "paper_context_menu";

    constructor(diagramEditorController: DiagramEditorController, paper: DiagramPaper) {
        this.diagramEditorController = diagramEditorController;
        this.undoRedoController = diagramEditorController.getUndoRedoController();
        this.paper = paper;
        this.paperCommandFactory = new PaperCommandFactory(this);
        this.clickFlag = false;
        this.rightClickFlag = false;
        this.gesturesController = new GesturesController(this, paper);
        this.lastCellMouseDownPosition = { x: 0, y: 0 };

        this.paper.on('cell:pointerdown', (cellView, event, x, y): void => {
            this.cellPointerdownListener(cellView, event, x, y);
        });
        this.paper.on('blank:pointerdown', (event, x, y): void => {
            this.blankPoinerdownListener(event, x, y);
        });

        this.paper.on('cell:pointerup', (cellView, event, x, y): void => {
            this.cellPointerupListener(cellView, event, x, y);
        });
        this.paper.on('cell:pointermove', (cellView, event, x, y): void => {
            this.cellPointermoveListener(cellView, event, x, y);
        });

        this.diagramEditorController.getGraph().on('change:position', (cell) => {
            if (!this.rightClickFlag) {
                return;
            }
            cell.set('position', cell.previous('position'));
        });

        document.addEventListener('mousedown', (event) => { this.gesturesController.onMouseDown(event) } );
        document.addEventListener('mouseup', (event) => { this.gesturesController.onMouseUp(event) } );
        $("#" + this.paper.getId()).mousemove((event) => { this.gesturesController.onMouseMove(event) } );

        this.initDropPaletteElementListener();

        this.initDeleteListener();
        this.initCustomContextMenu();
        this.initPropertyEditorListener();

        DiagramElementListener.makeAndExecuteCreateLinkCommand = (linkObject: Link): void => {
            this.makeAndExecuteCreateLinkCommand(linkObject);
        };
    }

    public getCurrentElement(): DiagramElement {
        return this.currentElement;
    }

    public clearState(): void {
        this.currentElement = null;
        this.clickFlag = false;
        this.rightClickFlag = false;
        this.lastCellMouseDownPosition = { x: 0, y: 0 };
    }

    public createLink(sourceId: string, targetId: string): void {
        var link: joint.dia.Link = new joint.dia.Link({
            attrs: {
                '.connection': { stroke: 'black' },
                '.marker-target': { fill: 'black', d: 'M 10 0 L 0 5 L 10 10 z' }
            },
            source: { id: sourceId },
            target: { id: targetId },
        });

        var typeProperties = this.diagramEditorController.getNodeProperties("ControlFlow");

        var linkProperties: Map<Property> = {};
        for (var property in typeProperties) {
            linkProperties[property] = new Property(typeProperties[property].name,
                typeProperties[property].type, typeProperties[property].value);
        }

        var linkObject: Link = new Link(link, linkProperties);

        this.makeAndExecuteCreateLinkCommand(linkObject);
    }

    public createNode(type: string, x: number, y: number, subprogramId?: string, subprogramName?: string): void {
        var image: string = this.diagramEditorController.getNodeType(type).getImage();
        var name: string = this.diagramEditorController.getNodeType(type).getName();

        var typeProperties: Map<Property> = this.diagramEditorController.getNodeType(type).getPropertiesMap();

        var nodeProperties: Map<Property> = {};
        for (var property in typeProperties) {
            nodeProperties[property] = new Property(typeProperties[property].name, typeProperties[property].type,
                typeProperties[property].value);
        }

        var node: DiagramNode;
        if (subprogramId) {
            node = new SubprogramNode(subprogramName, type, x, y, nodeProperties, image, subprogramId);
        } else {
            node = new DefaultDiagramNode(name, type, x, y, nodeProperties, image);
        }

        var command: Command = new MultiCommand([this.paperCommandFactory.makeCreateNodeCommand(node),
            this.paperCommandFactory.makeChangeCurrentElementCommand(node, this.currentElement)]);
        this.undoRedoController.addCommand(command);
        command.execute();
    }

    public createNodeInEventPositionFromNames(names: string[], event): void {
        var offsetX = (event.pageX - $("#" + this.paper.getId()).offset().left +
            $("#" + this.paper.getId()).scrollLeft()) / this.paper.getZoom();
        var offsetY = (event.pageY - $("#" + this.paper.getId()).offset().top +
            $("#" + this.paper.getId()).scrollTop()) / this.paper.getZoom();
        var gridSize: number = this.paper.getGridSize();
        offsetX -= offsetX % gridSize;
        offsetY -= offsetY % gridSize;

        var filteredNames: string[] = names.filter((type: string) => {
            return this.diagramEditorController.getNodeType(type) !== undefined;
        });

        if (filteredNames.length === 0) {
            return;
        }
        if (filteredNames.length === 1) {
            this.createNode(filteredNames[0], offsetX, offsetY);
            return;
        }

        var items = [];
        for (var i = 0; i < filteredNames.length; ++i) {
            items.push(
                {
                    "name": filteredNames[i],
                    "action": ((type, offsetX, offsetY) => { this.createNode(type, offsetX, offsetY);})
                        .bind(this, filteredNames[i], offsetX, offsetY)
                });
        }

        var contextMenu = new ContextMenu();
        var menuDiv = document.createElement("div");
        menuDiv.className = "gestures-menu";
        menuDiv.style.left = event.x + "px";
        menuDiv.style.top = event.y + "px";
        document.body.appendChild(menuDiv);
        contextMenu.showMenu(new CustomEvent("context-menu"), menuDiv, items);
    }

    public createLinkBetweenCurrentAndEventTargetElements(event): void {
        var diagramPaper: HTMLDivElement = <HTMLDivElement> document.getElementById(this.paper.getId());

        var elementBelow = this.diagramEditorController.getGraph().get('cells').find((cell) => {
            if (cell instanceof joint.dia.Link) return false; // Not interested in links.
            if (cell.id === this.currentElement.getJointObject().id) return false; // The same element as the dropped one.
            var mXBegin = cell.getBBox().origin().x;
            var mYBegin = cell.getBBox().origin().y;
            var mXEnd = cell.getBBox().corner().x;
            var mYEnd = cell.getBBox().corner().y;

            var leftElementPos:number = (event.pageX - $(diagramPaper).offset().left + $(diagramPaper).scrollLeft()) /
                this.paper.getZoom();
            var topElementPos:number = (event.pageY - $(diagramPaper).offset().top + $(diagramPaper).scrollTop()) /
                this.paper.getZoom();

            if ((mXBegin <= leftElementPos) && (mXEnd >= leftElementPos)
                && (mYBegin <= topElementPos) && (mYEnd >= topElementPos) && (this.rightClickFlag))
                return true;
            return false;
        });

        if (elementBelow) {
            this.createLink(this.currentElement.getJointObject().id, elementBelow.id);
        }
    }

    public changeCurrentElement(element: DiagramElement): void {
        if (element !== this.currentElement) {
            var changeCurrentElementCommand: Command = this.paperCommandFactory.makeChangeCurrentElementCommand(element,
                this.currentElement);
            this.undoRedoController.addCommand(changeCurrentElementCommand);
            changeCurrentElementCommand.execute();
        }
    }

    public makeAndExecuteCreateLinkCommand(link: Link): void {
        var createLinkCommand: Command = this.paperCommandFactory.makeCreateLinkCommand(link);
        this.undoRedoController.addCommand(createLinkCommand);
        createLinkCommand.execute();
    }

    public setCurrentElement(element: DiagramElement): void {
        if (this.currentElement) {
            this.unselectElement(this.currentElement.getJointObject());
        }
        this.currentElement = element;
        if (element) {
            this.selectElement(this.currentElement.getJointObject());
            this.diagramEditorController.setNodeProperties(element);
        } else {
            this.diagramEditorController.clearNodeProperties();
        }
    }

    public addNode(node: DiagramNode): void {
        if (node instanceof SubprogramNode) {
            this.paper.addSubprogramNode(node);
        } else {
            this.paper.addNode(node);
        }
    }

    public removeElement(element: DiagramElement): void {
        if (element) {
            if (element instanceof DefaultDiagramNode) {
                this.paper.removeNode(element.getJointObject().id);
            } else {
                this.paper.removeLink(element.getJointObject().id);
            }

            if (this.currentElement && element === this.currentElement) {
                this.diagramEditorController.clearNodeProperties();
                this.currentElement = null;
            }
        }
    }

    public addLink(link: Link): void {
        this.paper.addLinkToPaper(link);
    }

    private blankPoinerdownListener(event, x, y): void {
        if (event.button == 2) {
            this.gesturesController.startDrawing();
        }

        this.changeCurrentElement(null);
    }

    private cellPointerdownListener(cellView, event, x, y): void {
        this.clickFlag = true;
        this.rightClickFlag = false;

        var element: DiagramElement = this.paper.getNodeById(cellView.model.id) ||
            this.paper.getLinkById(cellView.model.id);
        this.changeCurrentElement(element);

        if (this.paper.getNodeById(cellView.model.id)) {
            if (event.button == 2) {
                this.rightClickFlag = true;
                this.gesturesController.startDrawing();
            } else {
                var node: DiagramNode = this.paper.getNodeById(cellView.model.id);
                this.lastCellMouseDownPosition.x = node.getX();
                this.lastCellMouseDownPosition.y = node.getY();
            }
        }

    }

    private cellPointerupListener(cellView, event, x, y): void {
        if ((this.clickFlag) && (event.button == 2)) {
            $("#" + this.contextMenuId).finish().toggle(100).
            css({
                left: event.pageX - $(document).scrollLeft() + "px",
                top: event.pageY - $(document).scrollTop() + "px"

            });
        } else if (event.button !== 2){
            var node: DiagramNode = this.paper.getNodeById(cellView.model.id);
            if (node) {
                var command: Command = this.paperCommandFactory.makeMoveCommand(node, this.lastCellMouseDownPosition.x,
                    this.lastCellMouseDownPosition.y, node.getX(), node.getY(), this.paper.getZoom());
                this.undoRedoController.addCommand(command);
            }
        }
    }

    private cellPointermoveListener(cellView, event, x, y): void {
        this.clickFlag = false;
    }

    private initDropPaletteElementListener(): void {
        var controller: PaperController = this;
        var paper: DiagramPaper = this.paper;

        $("#" + this.paper.getId()).droppable({
            drop: function(event, ui) {
                var topElementPos: number = (ui.offset.top - $(this).offset().top + $(this).scrollTop()) /
                    paper.getZoom();
                var leftElementPos: number = (ui.offset.left - $(this).offset().left + $(this).scrollLeft()) /
                    paper.getZoom();
                var gridSize: number = paper.getGridSize();
                topElementPos -= topElementPos % gridSize;
                leftElementPos -= leftElementPos % gridSize;

                var type = $(ui.draggable.context).data("type");

                controller.createNode(type, leftElementPos, topElementPos, $(ui.draggable.context).data("id"),
                    $(ui.draggable.context).data("name"));
            }
        });
    }

    private selectElement(jointObject): void {
        var jQueryEl = this.paper.findViewByModel(jointObject).$el;
        var oldClasses = jQueryEl.attr('class');
        jQueryEl.attr('class', oldClasses + ' selected');
    }

    private unselectElement(jointObject): void {
        $('input:text').blur();
        var jQueryEl = this.paper.findViewByModel(jointObject).$el;
        var removedClass = jQueryEl.attr('class').replace(new RegExp('(\\s|^)selected(\\s|$)', 'g'), '$2');
        jQueryEl.attr('class', removedClass);
    }

    private initCustomContextMenu(): void {
        var controller = this;
        $("#diagramContent").bind("contextmenu", function (event) {
            event.preventDefault();
        });

        $("#" + controller.contextMenuId + " li").click(function(){
            switch($(this).attr("data-action")) {
                case "delete":
                    controller.removeCurrentElement();
                    break;
            }

            $("#" + controller.contextMenuId).hide(100);
        });
    }

    private initDeleteListener(): void {
        var deleteKey: number = 46;
        $('html').keyup((event) => {
            if(event.keyCode == deleteKey) {
                if($("#" + this.paper.getId()).is(":visible") && !(document.activeElement.tagName === "INPUT")) {
                    this.removeCurrentElement();
                }
            }
        });
    }

    private removeCurrentElement(): void {
        var removeCommands: Command[] = [];
        removeCommands.push(this.paperCommandFactory.makeChangeCurrentElementCommand(null, this.currentElement));
        if (this.currentElement instanceof DefaultDiagramNode) {
            var node: DiagramNode = <DiagramNode> this.currentElement;
            var connectedLinks: Link[] = this.paper.getConnectedLinkObjects(node);
            connectedLinks.forEach((link: Link) => removeCommands.push(
                this.paperCommandFactory.makeRemoveLinkCommand(link)));
            removeCommands.push(this.paperCommandFactory.makeRemoveNodeCommand(node));
        } else if (this.currentElement instanceof Link) {
            removeCommands.push(this.paperCommandFactory.makeRemoveLinkCommand(<Link> this.currentElement));
        }
        var multiCommand: Command = new MultiCommand(removeCommands);
        this.undoRedoController.addCommand(multiCommand);
        multiCommand.execute();
    }

    private initPropertyEditorListener(): void {
        var controller = this;
        $(document).on('focus', ".property-edit-element input", function() {
            controller.changeCurrentElement(controller.paper.getNodeById($(this).data("id")));
        });
    }

}