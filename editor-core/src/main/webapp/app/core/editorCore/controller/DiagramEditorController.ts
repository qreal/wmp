/// <reference path="SceneController.ts" />
/// <reference path="PropertyEditorController.ts" />
/// <reference path="loaders/ElementsTypeLoader.ts" />
/// <reference path="PaletteController.ts" />
/// <reference path="exporters/DiagramExporter.ts" />
/// <reference path="../model/DiagramEditor.ts" />
/// <reference path="../model/Map.ts"/>
/// <reference path="../../../vendor.d.ts" />

class DiagramEditorController {

    protected diagramEditor: DiagramEditor;
    protected sceneController: SceneController;
    protected propertyEditorController: PropertyEditorController;
    protected elementsTypeLoader: ElementsTypeLoader;
    protected paletteController: PaletteController;
    protected nodeTypesMap: Map<NodeType>;
    protected linkPatternsMap: Map<joint.dia.Link>;
    protected undoRedoController: UndoRedoController;
    protected elementTypes: ElementTypes;

    constructor($scope, $attrs) {
        this.undoRedoController = new UndoRedoController();
        this.nodeTypesMap = {};
        this.linkPatternsMap = {};
        this.paletteController = new PaletteController();
        DiagramElementListener.getNodeType = (type: string): NodeType => {
            return this.getNodeType(type);
        };
        this.diagramEditor = new DiagramEditor();
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

    public getNodesMap(): Map<DiagramNode> {
        var paper = this.diagramEditor.getScene();
        return paper.getNodesMap();
    }

    public getLinksMap(): Map<Link> {
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
        return this.nodeTypesMap[type];
    }

    public getNodeProperties(type: string): Map<Property> {
        return this.nodeTypesMap[type].getPropertiesMap();
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

    public getNodeTypes(): Map<NodeType> {
        return this.nodeTypesMap;
    }

    public getLinkPatterns(): Map<joint.dia.Link> {
        return this.linkPatternsMap;
    }

    public addFromMap(diagramParts: DiagramParts): void {
        var scene = this.diagramEditor.getScene();
        scene.addNodesFromMap(diagramParts.nodesMap);
        scene.addLinksFromMap(diagramParts.linksMap);
    }

    protected handleLoadedTypes(elementTypes: ElementTypes): void {
        this.propertyEditorController = new PropertyEditorController(this.sceneController, this.undoRedoController);

        this.elementTypes = elementTypes;

        $.extend(this.linkPatternsMap, elementTypes.linkPatterns);
        $.extend(this.nodeTypesMap, elementTypes.blockTypes.convertToMap(), elementTypes.flowTypes.convertToMap(),
            this.elementTypes.containerTypes.convertToMap());

        this.diagramEditor.getScene().setLinkPatterns(this.linkPatternsMap);
        this.paletteController.init(this.diagramEditor.getScene(), elementTypes, this.nodeTypesMap);
    }
}