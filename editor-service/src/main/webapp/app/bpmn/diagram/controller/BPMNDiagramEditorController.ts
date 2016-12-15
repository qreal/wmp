/// <reference path="../../../common/menu/controller/DiagramMenuController.ts" />
/// <reference path="../../../robots/interpreter/Interpreter.ts" />
/// <reference path="../../../common/interfaces/editorCore.d.ts" />
/// <reference path="../../../common/interfaces/vendor.d.ts" />
/// <reference path="BPMNSceneController.ts" />

class BPMNDiagramEditorController extends DiagramEditorController {

    private menuController: DiagramMenuController;
    private diagramInterpreter: Interpreter;

    constructor($scope, $attrs) {
        super($scope, $attrs);
        this.sceneController = new BPMNSceneController(this, this.diagramEditor.getScene());
        this.menuController = new DiagramMenuController(this);
        this.diagramInterpreter = new Interpreter();

        $scope.createNewDiagram = () => { this.menuController.createNewDiagram(); };
        $scope.openFolderWindow = () => { this.menuController.openFolderWindow(); };
        $scope.saveCurrentDiagram = () => { this.menuController.saveCurrentDiagram(); };
        $scope.saveDiagramAs = () => { this.menuController.saveDiagramAs(); };
        $scope.clearAll = () => { this.clearAll(); };

        this.elementsTypeLoader.load((elementTypes: ElementTypes): void => {
            this.handleLoadedTypes(elementTypes);
        }, "", "bpmn");
    }

    public handleLoadedTypes(elementTypes: ElementTypes): void {
        this.propertyEditorController = new PropertyEditorController(this.sceneController, this.undoRedoController);

        for (var typeName in elementTypes.uncategorisedTypes) {
            this.nodeTypesMap[typeName] = elementTypes.uncategorisedTypes[typeName];
        }

        Map.unite(this.nodeTypesMap, elementTypes.paletteTypes.convertToMap());

        this.paletteController.appendBlocksPalette(elementTypes.paletteTypes);
        this.paletteController.initDraggable();
    }

    public clearAll(): void {
        this.clearState();
        this.menuController.clearState();
    }

}