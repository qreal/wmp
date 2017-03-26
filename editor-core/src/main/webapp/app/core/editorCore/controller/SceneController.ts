import {MultiCommand} from "../model/commands/MultiCommand";
import {Command} from "../model/commands/Command";
import {Link} from "../model/Link";
import {DiagramNode} from "../model/DiagramNode";
import {DefaultDiagramNode} from "../model/DefaultDiagramNode";
import {DiagramScene} from "../model/DiagramScene";
import {MouseButton} from "../../../common/constants/MouseButton";
import {DefaultSize} from "../../../common/constants/DefaultSize";
import {DiagramElement} from "../model/DiagramElement";
import {SubprogramNode} from "../model/SubprogramNode";
import {Property} from "../model/Property";
import {NodeType} from "../model/NodeType";
import {DiagramElementListener} from "./DiagramElementListener";
import {SceneCommandFactory} from "../model/commands/SceneCommandFactory";
import {DiagramEditorController} from "./DiagramEditorController";
import {UndoRedoController} from "./UndoRedoController";
import {ContainerNodeType} from "../model/ContainerNodeType";
import {DiagramContainer} from "../model/DiagramContainer";
import {EmbedCommand} from "../model/commands/EmbedCommand";
export class SceneController {

    private diagramEditorController: DiagramEditorController;
    private scene: DiagramScene;
    private currentElement: DiagramElement;
    private clickFlag : boolean;
    private rightClickFlag : boolean;
    private undoRedoController: UndoRedoController;
    private lastCellMouseDownPosition: {x: number, y: number};
    private lastCellMouseDownSize: {width: number, height: number};
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
        this.lastCellMouseDownSize = { width: 0, height: 0 };

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

    public createLink(sourceId: string, target: any): void {

        var link: joint.dia.Link = this.scene.getCurrentLinkType();
        link.set({
            source: { id: sourceId },
            target: target
        });

        var nodeType: NodeType = this.diagramEditorController.getNodeType(this.scene.getCurrentLinkTypeName());
        var typeProperties: Map<String, Property> = nodeType.getPropertiesMap();

        var linkProperties: Map<String, Property> = new Map<String, Property>();
        for (var property in typeProperties) {
            linkProperties[property] = new Property(typeProperties[property].name,
                typeProperties[property].type, typeProperties[property].value);
        }

        var linkObject: Link = new Link(link, nodeType.getShownName(), nodeType.getName(), linkProperties);

        this.makeAndExecuteCreateLinkCommand(linkObject);
    }

    public createNode(type: string, x: number, y: number, subprogramId?: string, subprogramName?: string): void {
        var image: string = this.diagramEditorController.getNodeType(type).getImage();
        var name: string = this.diagramEditorController.getNodeType(type).getName();

        var typeProperties: Map<String, Property> = this.diagramEditorController.getNodeType(type).getPropertiesMap();

        var nodeProperties: Map<String, Property> = new Map<String, Property>();
        for (var property in typeProperties) {
            nodeProperties[property] = new Property(typeProperties[property].name, typeProperties[property].type,
                typeProperties[property].value);
        }

        var node: DiagramNode;
        if (subprogramId) {
            node = new SubprogramNode(subprogramName, type, x, y, DefaultSize.DEFAULT_NODE_WIDTH,
                DefaultSize.DEFAULT_NODE_HEIGHT, nodeProperties, image, subprogramId);
        } else {
            if (this.diagramEditorController.getNodeType(type) instanceof ContainerNodeType)
                node = new DiagramContainer(name, type, x, y, DefaultSize.DEFAULT_NODE_WIDTH,
                    DefaultSize.DEFAULT_NODE_HEIGHT, nodeProperties, image);
            else
                node = new DefaultDiagramNode(name, type, x, y, DefaultSize.DEFAULT_NODE_WIDTH,
                    DefaultSize.DEFAULT_NODE_HEIGHT, nodeProperties, image);
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
        var controller = this;
        var elementBelow = this.getElementBelow(event, function(cell: joint.dia.Element) {
            return !(cell instanceof joint.dia.Link || cell.id === controller.currentElement.getJointObject().id) && controller.rightClickFlag;
        });
        if (elementBelow) {
            this.createLink(this.currentElement.getJointObject().id, {id: elementBelow.id});
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
            var bbox = cellView.getBBox();
            this.lastCellMouseDownSize.width = bbox.width;
            this.lastCellMouseDownSize.height = bbox.height;
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
                if (node.isResizing()) {
                    var bbox = cellView.getBBox();
                    let command: Command = this.paperCommandFactory.makeResizeCommand(
                        node,
                        this.lastCellMouseDownSize.width,
                        this.lastCellMouseDownSize.height,
                        bbox.width,
                        bbox.height,
                        cellView);
                    this.undoRedoController.addCommand(command);
                } else {
                    this.moveNode(cellView, node);
                }
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

    private moveNode(cellView, node: DiagramNode): void {
        let command: MultiCommand = new MultiCommand([]);

        var dependentNodes: DiagramNode[] = this.getDependentNodes(node);
        var diffX: number = node.getX() - this.lastCellMouseDownPosition.x;
        var diffY: number = node.getY() - this.lastCellMouseDownPosition.y;
        dependentNodes.forEach((curNode: DiagramNode) => command.add(this.paperCommandFactory.makeMoveCommand(
            curNode,
            curNode.getX() - diffX,
            curNode.getY() - diffY,
            curNode.getX(),
            curNode.getY(),
            this.scene.getZoom(),
            cellView)));

        var parent: DiagramContainer = <DiagramContainer> this.scene.getNodeById(node.getJointObject().get('parent'));
        var oldParent: DiagramContainer = (node).getParentNode();
        if (parent !== oldParent) {
            var embedCommand = new EmbedCommand(node, parent, oldParent);
            embedCommand.execute();
            command.add(embedCommand);
        }
        this.undoRedoController.addCommand(command);
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

                if (paper.getCurrentLinkTypeName() == type) {
                    var elementBelow = controller.getElementBelow(event, function(cell: joint.dia.Element) {
                        return !(cell instanceof joint.dia.Link);
                    });
                    if (elementBelow) {
                        var bBox = elementBelow.getBBox();
                        controller.createLink(elementBelow.id, {
                            x: bBox.x + bBox.width + controller.scene.getGridSize() * 2,
                            y: bBox.y + bBox.height / 2
                        });
                    }
                } else {
                    controller.createNode(type, leftElementPos, topElementPos, $(ui.draggable.context).data("id"),
                        $(ui.draggable.context).data("name"));
                }
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
        var removedNodes: Set<DiagramNode> = new Set<DiagramNode>();
        var removedLinks: Set<Link> = new Set<Link>();
        var multiCommand: MultiCommand = new MultiCommand([this.paperCommandFactory.makeChangeCurrentElementCommand(null,
            this.currentElement)]);

        if (this.currentElement instanceof Link) {
            removedLinks.add(<Link> this.currentElement);
        } else {
            this.getDependentNodes(<DiagramNode> this.currentElement).forEach((node: DiagramNode) => {
                removedNodes.add(node);
            })
        }

        removedNodes.forEach((node: DiagramNode) => {
            this.scene.getConnectedLinkObjects(node).forEach((link: Link) => {
                removedLinks.add(link);
            });
        });

        removedLinks.forEach((link: Link) => {
            multiCommand.add(this.paperCommandFactory.makeRemoveLinkCommand(link));
        });

        removedNodes.forEach((node: DiagramNode) => {
            multiCommand.add(this.paperCommandFactory.makeEmbedCommand(node, null, node.getParentNode()));
            multiCommand.add(this.paperCommandFactory.makeRemoveNodeCommand(node));
        });

        this.undoRedoController.addCommand(multiCommand);
        multiCommand.execute();
    }

    private getDependentNodes(node: DiagramNode): DiagramNode[] {
        var elements: DiagramNode[] = [];
        if (node instanceof DiagramContainer) {
            var embeddedCells: joint.dia.Cell[] = node.getJointObject().getEmbeddedCells();
            for (var i = 0; i < embeddedCells.length; i++) {
                var diagramNode: DiagramNode = this.scene.getNodeById(embeddedCells[i].id);
                if (diagramNode)
                    elements.push(...this.getDependentNodes(diagramNode));
            }
        }
        elements.push(node);
        return elements;
    }

    private initPropertyEditorListener(): void {
        var controller = this;
        $(document).on('focus', ".property-edit-element input", function() {
            controller.changeCurrentElement(controller.scene.getNodeById($(this).data("id")));
        });
    }

    private getElementBelow(event: any, checker?: (cell: joint.dia.Element) => boolean): joint.dia.Element {
        var diagramPaper: HTMLDivElement = <HTMLDivElement> document.getElementById(this.scene.getId());
        var chosenElement: joint.dia.Element = null;
        var chosenWidth = -1;
        var cells: joint.dia.Element[] = this.diagramEditorController.getGraph().get('cells');
        cells.forEach((cell: joint.dia.Element) => {
            if (checker && !checker(cell))
                return false;

            var mXBegin = cell.getBBox().x;
            var mYBegin = cell.getBBox().y;
            var mXEnd = cell.getBBox().x + cell.getBBox().width;
            var mYEnd = cell.getBBox().y + cell.getBBox().height;
            var leftElementPos: number = (event.pageX - $(diagramPaper).offset().left + $(diagramPaper).scrollLeft()) /
                this.scene.getZoom();
            var topElementPos: number = (event.pageY - $(diagramPaper).offset().top + $(diagramPaper).scrollTop()) /
                this.scene.getZoom();

            if ((mXBegin <= leftElementPos) && (mXEnd >= leftElementPos)
                && (mYBegin <= topElementPos) && (mYEnd >= topElementPos)
                && (!chosenElement || mXEnd - mXBegin < chosenWidth)) {
                    chosenElement = cell;
                    chosenWidth = cell.getBBox().width;
            }
        });
        return chosenElement;
    }
}
