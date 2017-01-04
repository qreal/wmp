/// <reference path="DiagramEditorController.ts" />
/// <reference path="../model/DiagramScene.ts" />
/// <reference path="../model/DiagramElement.ts" />
/// <reference path="../model/PaletteTypes.ts" />
/// <reference path="../model/DiagramNode.ts" />
/// <reference path="../model/DefaultDiagramNode.ts" />
/// <reference path="../model/commands/Command.ts"/>
/// <reference path="../model/commands/SceneCommandFactory.ts" />
/// <reference path="../../../vendor.d.ts" />
/// <reference path="../../../common/constants/MouseButton.ts" />

class SceneController {

    private diagramEditorController: DiagramEditorController;
    private scene: DiagramScene;
    private currentElement: DiagramElement;
    private clickFlag : boolean;
    private rightClickFlag : boolean;
    private undoRedoController: UndoRedoController;
    private lastCellMouseDownPosition: {x: number, y: number};
    private paperCommandFactory: SceneCommandFactory;
    private contextMenuId = "scene-context-menu";

    constructor(diagramEditorController: DiagramEditorController, paper: DiagramScene) {
        this.diagramEditorController = diagramEditorController;
        this.undoRedoController = diagramEditorController.getUndoRedoController();
        this.scene = paper;
        this.paperCommandFactory = new SceneCommandFactory(this);
        this.clickFlag = false;
        this.rightClickFlag = false;
        this.lastCellMouseDownPosition = { x: 0, y: 0 };

        this.scene.on('cell:pointerdown', (cellView, event, x, y): void => {
            this.cellPointerdownListener(cellView, event, x, y);
        });
        this.scene.on('blank:pointerdown', (event, x, y): void => {
            this.blankPoinerdownListener(event, x, y);
        });

        this.scene.on('cell:pointerup', (cellView, event, x, y): void => {
            this.cellPointerupListener(cellView, event, x, y);
        });
        this.scene.on('cell:pointermove', (cellView, event, x, y): void => {
            this.cellPointermoveListener(cellView, event, x, y);
        });

        this.diagramEditorController.getGraph().on('change:position', (cell) => {
            if (!this.rightClickFlag) {
                return;
            }
            cell.set('position', cell.previous('position'));
        });

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
            node = new SubprogramNode(subprogramName, type, x, y, defaultNodeWidth, defaultNodeHeight,
                nodeProperties, image, subprogramId);
        } else {
            node = new DefaultDiagramNode(name, type, x, y, defaultNodeWidth, defaultNodeHeight,
                nodeProperties, image);
        }

        var command: Command = new MultiCommand([this.paperCommandFactory.makeCreateNodeCommand(node),
            this.paperCommandFactory.makeChangeCurrentElementCommand(node, this.currentElement)]);
        this.undoRedoController.addCommand(command);
        command.execute();
    }

    public createNodeInEventPositionFromNames(names: string[], event): void {
        var offsetX = (event.pageX - $("#" + this.scene.getId()).offset().left +
            $("#" + this.scene.getId()).scrollLeft()) / this.scene.getZoom();
        var offsetY = (event.pageY - $("#" + this.scene.getId()).offset().top +
            $("#" + this.scene.getId()).scrollTop()) / this.scene.getZoom();
        var gridSize: number = this.scene.getGridSize();
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
        var diagramPaper: HTMLDivElement = <HTMLDivElement> document.getElementById(this.scene.getId());

        var elementBelow = this.diagramEditorController.getGraph().get('cells').find((cell) => {
            if (cell instanceof joint.dia.Link) return false; // Not interested in links.
            if (cell.id === this.currentElement.getJointObject().id) return false; // The same element as the dropped one.
            var mXBegin = cell.getBBox().origin().x;
            var mYBegin = cell.getBBox().origin().y;
            var mXEnd = cell.getBBox().corner().x;
            var mYEnd = cell.getBBox().corner().y;

            var leftElementPos:number = (event.pageX - $(diagramPaper).offset().left + $(diagramPaper).scrollLeft()) /
                this.scene.getZoom();
            var topElementPos:number = (event.pageY - $(diagramPaper).offset().top + $(diagramPaper).scrollTop()) /
                this.scene.getZoom();

            return ((mXBegin <= leftElementPos) && (mXEnd >= leftElementPos)
                && (mYBegin <= topElementPos) && (mYEnd >= topElementPos) && (this.rightClickFlag))
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
            this.scene.addSubprogramNode(node);
        } else {
            this.scene.addNode(node);
        }
    }

    public removeElement(element: DiagramElement): void {
        if (element) {
            if (element instanceof DefaultDiagramNode) {
                this.scene.removeNode(element.getJointObject().id);
            } else {
                this.scene.removeLink(element.getJointObject().id);
            }

            if (this.currentElement && element === this.currentElement) {
                this.diagramEditorController.clearNodeProperties();
                this.currentElement = null;
            }
        }
    }

    public addLink(link: Link): void {
        this.scene.addLinkToPaper(link);
    }

    private blankPoinerdownListener(event, x, y): void {
        this.changeCurrentElement(null);
    }

    private cellPointerdownListener(cellView, event, x, y): void {
        this.clickFlag = true;
        this.rightClickFlag = false;

        var element: DiagramElement = this.scene.getNodeById(cellView.model.id) ||
            this.scene.getLinkById(cellView.model.id);
        this.changeCurrentElement(element);

        if (this.scene.getNodeById(cellView.model.id) && event.button == MouseButton.left) {
            var node:DiagramNode = this.scene.getNodeById(cellView.model.id);
            this.lastCellMouseDownPosition.x = node.getX();
            this.lastCellMouseDownPosition.y = node.getY();
            cellView.highlight(cellView.model.id);
            node.initResize(cellView.getBBox(), x, y, 20);
        }
        if (event.button == MouseButton.right) {
            this.rightClickFlag = true;
        }
    }

    private cellPointerupListener(cellView, event, x, y): void {
        if (this.clickFlag && event.button == MouseButton.right) {
            $("#" + this.contextMenuId).finish().toggle(100).
            css({
                left: event.pageX - $(document).scrollLeft() + "px",
                top: event.pageY - $(document).scrollTop() + "px"

            });
        } else if (event.button == MouseButton.left){
            var node: DiagramNode = this.scene.getNodeById(cellView.model.id);
            if (node) {
                var command: Command = this.paperCommandFactory.makeMoveCommand(
                    node,
                    this.lastCellMouseDownPosition.x,
                    this.lastCellMouseDownPosition.y,
                    node.getX(),
                    node.getY(),
                    this.scene.getZoom());
                this.undoRedoController.addCommand(command);

                node.completeResize();
                cellView.unhighlight(cellView.model.id);
            }
        }
    }

    private cellPointermoveListener(cellView, event, x, y): void {
        this.clickFlag = false;
        var node: DiagramNode = this.scene.getNodeById(cellView.model.id);
        if (node) {
            node.pointermove(cellView, event, x, y);
        }
    }

    private initDropPaletteElementListener(): void {
        var controller: SceneController = this;
        var paper: DiagramScene = this.scene;

        $("#" + this.scene.getId()).droppable({
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
        var jQueryEl = this.scene.findViewByModel(jointObject).$el;
        var oldClasses = jQueryEl.attr('class');
        jQueryEl.attr('class', oldClasses + ' selected');
    }

    private unselectElement(jointObject): void {
        $('input:text').blur();
        var jQueryEl = this.scene.findViewByModel(jointObject).$el;
        var removedClass = jQueryEl.attr('class').replace(new RegExp('(\\s|^)selected(\\s|$)', 'g'), '$2');
        jQueryEl.attr('class', removedClass);
    }

    private initCustomContextMenu(): void {
        var controller = this;
        $("#diagram-area").bind("contextmenu", function (event) {
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
                if($("#" + this.scene.getId()).is(":visible") && !(document.activeElement.tagName === "INPUT")) {
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
            var connectedLinks: Link[] = this.scene.getConnectedLinkObjects(node);
            connectedLinks.forEach((link: Link) => removeCommands.push(
                this.paperCommandFactory.makeRemoveLinkCommand(link)));
            removeCommands.push(this.paperCommandFactory.makeRemoveNodeCommand(node));
        } else if (this.currentElement instanceof Link) {
            removeCommands.push(this.paperCommandFactory.makeRemoveLinkCommand(<Link> this.currentElement));
        }
        var multiCommand : Command = new MultiCommand(removeCommands);
        this.undoRedoController.addCommand(multiCommand);
        multiCommand.execute();
    }

    private initPropertyEditorListener(): void {
        var controller = this;
        $(document).on('focus', ".property-edit-element input", function() {
            controller.changeCurrentElement(controller.scene.getNodeById($(this).data("id")));
        });
    }

}