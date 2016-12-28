/// <reference path="../../../common/menu/controller/DiagramMenuController.ts" />
/// <reference path="../../../robots/interpreter/Interpreter.ts" />
/// <reference path="../../../common/interfaces/editorCore.d.ts" />
/// <reference path="../../../common/interfaces/vendor.d.ts" />
/// <reference path="../../../common/gestures/GesturesController.ts" />

class BpmnDiagramEditorController extends DiagramEditorController {

    private menuController: DiagramMenuController;
    private gesturesController: GesturesController;
    private diagramInterpreter: Interpreter;

    constructor($scope, $attrs) {
        super($scope, $attrs);

        var scene: DiagramScene = this.diagramEditor.getScene();
        this.menuController = new DiagramMenuController(this);
        this.gesturesController = new GesturesController(this.sceneController, this.diagramEditor.getScene());
        this.diagramInterpreter = new Interpreter();

        document.addEventListener('mousedown', (event) => { this.gesturesController.onMouseDown(event) } );
        document.addEventListener('mouseup', (event) => { this.gesturesController.onMouseUp(event) } );
        $("#" + scene.getId()).mousemove((event) => { this.gesturesController.onMouseMove(event) } );
        $("#elements-search").on('input', (event) => {
            this.paletteController.searchPaletteReload(event, this.elementTypes, this.nodeTypesMap);
            this.paletteController.initDraggable();
            this.paletteController.initClick(this.diagramEditor.getScene());
        } );

        (scene as any).on('cell:pointerdown', (cellView, event, x, y): void => {
            this.cellPointerdownListener(cellView, event, x, y);
        });
        (scene as any).on('blank:pointerdown', (event, x, y): void => {
            this.blankPoinerdownListener(event, x, y);
        });

        $scope.createNewDiagram = () => { this.menuController.createNewDiagram(); };
        $scope.openFolderWindow = () => { this.menuController.openFolderWindow(); };
        $scope.saveCurrentDiagram = () => { this.menuController.saveCurrentDiagram(); };
        $scope.saveDiagramAs = () => { this.menuController.saveDiagramAs(); };
        $scope.clearAll = () => { this.clearAll(); };

        this.elementsTypeLoader.load((elementTypes: ElementTypes): void => {
            this.handleLoadedTypes(elementTypes);
        }, "", "bpmn");
    }

    public clearAll(): void {
        this.clearState();
        this.menuController.clearState();
    }

    private blankPoinerdownListener(event, x, y): void {
        if (event.button == MouseButton.right) {
            this.gesturesController.startDrawing();
        }
    }

    private cellPointerdownListener(cellView, event, x, y): void {
        if (event.button == MouseButton.right) {
            this.gesturesController.startDrawing();
        }
    }
}