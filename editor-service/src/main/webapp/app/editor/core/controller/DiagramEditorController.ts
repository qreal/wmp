/// <reference path="SceneController.ts" />
/// <reference path="PropertyEditorController.ts" />
/// <reference path="loaders/ElementsTypeLoader.ts" />
/// <reference path="PaletteController.ts" />
/// <reference path="parsers/DiagramJsonParser.ts" />
/// <reference path="exporters/DiagramExporter.ts" />
/// <reference path="../model/DiagramEditor.ts" />
/// <reference path="../model/RobotsDiagramNode.ts" />
/// <reference path="../../utils/Map.ts"/>
/// <reference path="../../interfaces/vendor.d.ts" />
/// <reference path="../../interfaces/pluginController.d.ts" />
/// <reference path="../../interfaces/plugins/undo-redo.d.ts" />

abstract class DiagramEditorController {

    protected scope: ng.IScope;
    protected diagramEditor: DiagramEditor;
    protected sceneController: SceneController;
    protected propertyEditorController: PropertyEditorController;
    protected elementsTypeLoader: ElementsTypeLoader;
    protected paletteController: PaletteController;
    protected nodeTypesMap: Map<NodeType>;
    protected undoRedoController: UndoRedoController;

    constructor($scope, $attrs) {
        this.scope = $scope;
        this.undoRedoController = PluginController.create("UndoRedoController");
        $scope.undo = () => {
            PluginController.exec(this.undoRedoController, "undo");
        };

        $scope.redo = () => {
            PluginController.exec(this.undoRedoController, "redo");
        };
        this.nodeTypesMap = {};
        this.paletteController = new PaletteController();
        DiagramElementListener.getNodeProperties = (type: string): Map<Property> => {
            return this.getNodeProperties(type);
        }
        this.diagramEditor = new DiagramEditor();
        this.sceneController = new SceneController(this, this.diagramEditor.getScene());
        this.elementsTypeLoader = new ElementsTypeLoader();

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
        PluginController.exec(this.undoRedoController, "clearStack");
    }

}