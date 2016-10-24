/// <reference path="../../../common/menu/controller/DiagramMenuController.ts" />
/// <reference path="../../../robots/interpreter/Interpreter.ts" />
/// <reference path="../../../common/interfaces/editorCore.d.ts" />
/// <reference path="../../../common/interfaces/vendor.d.ts" />

class RobotsDiagramEditorController extends DiagramEditorController {

    private menuController: DiagramMenuController;
    private diagramInterpreter: Interpreter;

    constructor($scope, $attrs) {
        super($scope, $attrs);
        this.menuController = new DiagramMenuController(this);
        this.diagramInterpreter = new Interpreter();

        $scope.openTwoDModel = () => { this.openTwoDModel(); };
        $scope.createNewDiagram = () => { this.menuController.createNewDiagram(); };
        $scope.openFolderWindow = () => { this.menuController.openFolderWindow(); };
        $scope.saveCurrentDiagram = () => { this.menuController.saveCurrentDiagram(); };
        $scope.saveDiagramAs = () => { this.menuController.saveDiagramAs(); };
        $scope.clearAll = () => { this.clearAll(); };

        $scope.$on("interpret", (event, timeline) => {
            this.diagramInterpreter.interpret(this.getGraph(), this.getNodesMap(), this.getLinksMap(), timeline);
        });

        $scope.$on("stop", (event) => {
            this.diagramInterpreter.stop();
        });

        this.elementsTypeLoader.load((elementTypes: ElementTypes): void => {
            this.handleLoadedTypes(elementTypes);
        });
    }

    public handleLoadedTypes(elementTypes: ElementTypes): void {
        this.propertyEditorController = new PropertyEditorController(this.sceneController, this.undoRedoController);

        for (var typeName in elementTypes.uncategorisedTypes) {
            this.nodeTypesMap[typeName] = elementTypes.uncategorisedTypes[typeName];
        }

        var categories: Map<Map<NodeType>> = elementTypes.paletteTypes.categories;
        for (var category in categories) {
            for (var typeName in categories[category]) {
                this.nodeTypesMap[typeName] = categories[category][typeName];
            }
        }

        this.paletteController.appendBlocksPalette(elementTypes.paletteTypes);
        this.paletteController.initDraggable();
    }

    public openTwoDModel(): void {
        $("#diagram-area").hide();
        $("#two-d-model-area").show();
    }

    public clearAll(): void {
        this.clearState();
        this.menuController.clearState();
    }

}