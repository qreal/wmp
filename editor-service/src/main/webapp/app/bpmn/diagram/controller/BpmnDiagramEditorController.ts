import {MouseButton} from "../../../common/constants/MouseButton";
import {DiagramMenuController} from "../../../common/menu/controller/DiagramMenuController";
import {GesturesController} from "../../../common/gestures/GesturesController";
import {ElementTypes} from "core/editorCore/model/ElementTypes";
import {DiagramScene} from "core/editorCore/model/DiagramScene";
import {DiagramEditorController} from "core/editorCore/controller/DiagramEditorController"
import app = require("../../../require/app");
import {Interpreter} from "../../../robots/interpreter/Interpreter";
import {BpmnElementConstructor} from "../model/BpmnElementConstructor";
export class BpmnDiagramEditorController extends DiagramEditorController {

    private menuController: DiagramMenuController;
    private gesturesController: GesturesController;
    private diagramInterpreter: Interpreter;
    //Hack for firefox
    static $$ngIsClass: boolean;

    constructor($scope, $attrs) {
        super($scope, $attrs);

        var scene: DiagramScene = this.diagramEditor.getScene();
        this.gesturesController = new GesturesController(this.sceneController, this.diagramEditor.getScene());
        this.diagramInterpreter = new Interpreter();
        this.elementConstructor = new BpmnElementConstructor(this.getNodesMap(), this.nodeTypesMap);
        this.menuController = new DiagramMenuController(this);

        document.addEventListener('mousedown', (event) => { this.gesturesController.onMouseDown(event) } );
        document.addEventListener('mouseup', (event) => { this.gesturesController.onMouseUp(event) } );
        $("#" + scene.getId()).mousemove((event) => { this.gesturesController.onMouseMove(event) } );
        $("#elements-search").on('input', (event) => {
            this.paletteController.searchPaletteReload(event);
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

BpmnDiagramEditorController.$$ngIsClass = true;

app.controller("BpmnDiagramEditorController", BpmnDiagramEditorController);
console.log("Adding controller BpmnDiagramEditorController");
