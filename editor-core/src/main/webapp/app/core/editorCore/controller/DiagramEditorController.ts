/// <reference path="../../../vendor.d.ts" />
import {PropertyEditorController} from "./PropertyEditorController";
import {ElementTypes} from "../model/ElementTypes";
import {DiagramParts} from "../model/DiagramParts";
import {NodeType} from "../model/NodeType";
import {UndoRedoController} from "./UndoRedoController";
import {Property} from "../model/Property";
import {DiagramElement} from "../model/DiagramElement";
import {Link} from "../model/Link";
import {DiagramNode} from "../model/DiagramNode";
import {ElementsTypeLoader} from "./loaders/ElementsTypeLoader";
import {SceneController} from "./SceneController";
import {DiagramEditor} from "../model/DiagramEditor";
import {DiagramElementListener} from "./DiagramElementListener";
import {PaletteController} from "./PaletteController";
import {ElementConstructor} from "../model/ElementConstructor"
import {MapUtils} from "../../../utils/MapUtils";
import {DiagramScene} from "../model/DiagramScene";
export class DiagramEditorController {

    protected diagramEditor: DiagramEditor;
    protected sceneController: SceneController;
    protected propertyEditorController: PropertyEditorController;
    protected elementsTypeLoader: ElementsTypeLoader;
    protected paletteController: PaletteController;
    protected nodeTypesMap: Map<string, NodeType>;
    protected linkPatternsMap: Map<string, joint.dia.Link>;
    protected undoRedoController: UndoRedoController;
    protected elementTypes: ElementTypes;
    protected elementConstructor: ElementConstructor;

    constructor($scope, $attrs) {
        this.undoRedoController = new UndoRedoController();
        this.nodeTypesMap = new Map<string, NodeType>();
        this.linkPatternsMap = new Map<string, joint.dia.Link>();
        this.paletteController = new PaletteController();
        DiagramElementListener.getNodeType = (type: string): NodeType => {
            return this.getNodeType(type);
        };
        this.diagramEditor = new DiagramEditor();
        this.elementConstructor = new ElementConstructor(this);
        this.sceneController = new SceneController(this, this.diagramEditor.getScene());
        this.elementsTypeLoader = new ElementsTypeLoader();

        $scope.undo = () => {
            this.undoRedoController.undo();
        };

        $scope.redo = () => {
            this.undoRedoController.redo();
        };

        $(document).bind("mousedown", function (e) {
            if (!($(e.target).parents(".custom-menu").length > 0)) {
                $(".custom-menu").hide(100);
            }
        });
    }

    public getGraph(): joint.dia.Graph {
        return this.diagramEditor.getGraph();
    }

    public getNodesMap(): Map<string, DiagramNode> {
        var paper = this.diagramEditor.getScene();
        return paper.getNodesMap();
    }

    public getLinksMap(): Map<string, Link> {
        var paper = this.diagramEditor.getScene();
        return paper.getLinksMap();
    }

    public setNodeProperties(element: DiagramElement): void {
        this.propertyEditorController.setNodeProperties(element)
    }

    public clearNodeProperties(): void {
        this.propertyEditorController.clearState();
    }

    public getNodeType(type: string): NodeType {
        return this.nodeTypesMap.get(type);
    }

    public getNodeProperties(type: string): Map<string, Property> {
        return this.nodeTypesMap.get(type).getPropertiesMap();
    }

    public getElementConstructor(): ElementConstructor {
        return this.elementConstructor;
    }

    public getUndoRedoController(): UndoRedoController {
        return this.undoRedoController;
    }

    public clearState(): void {
        this.propertyEditorController.clearState();
        this.sceneController.clearState();
        this.diagramEditor.clear();
        this.undoRedoController.clearStack();
    }

    public getDiagramParts(): DiagramParts {
        return new DiagramParts(this.getNodesMap(), this.getLinksMap());
    }

    public getNodeTypes(): Map<string, NodeType> {
        return this.nodeTypesMap;
    }

    public getLinkPatterns(): Map<string, joint.dia.Link> {
        return this.linkPatternsMap;
    }

    public addFromMap(diagramParts: DiagramParts): void {
        var scene = this.diagramEditor.getScene();
        scene.addNodesFromMap(diagramParts.nodesMap);
        scene.addLinksFromMap(diagramParts.linksMap);
    }

    public getScene(): DiagramScene {
        return this.diagramEditor.getScene();
    }

    protected handleLoadedTypes(elementTypes: ElementTypes): void {
        this.propertyEditorController = new PropertyEditorController(this.sceneController, this.undoRedoController);

        this.elementTypes = elementTypes;

        MapUtils.extend(this.linkPatternsMap, this.elementTypes.linkPatterns);
        MapUtils.extend(this.nodeTypesMap, elementTypes.blockTypes.convertToMap(), elementTypes.flowTypes.convertToMap());

        this.diagramEditor.getScene().setLinkPatterns(this.linkPatternsMap);
        this.paletteController.init(this.diagramEditor.getScene(), elementTypes, this.nodeTypesMap);
    }
}