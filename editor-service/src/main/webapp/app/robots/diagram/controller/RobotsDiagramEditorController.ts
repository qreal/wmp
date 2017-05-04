import {MouseButton} from "../../../common/constants/MouseButton";
import {GesturesController} from "../../../common/gestures/GesturesController";
import {DiagramMenuController} from "../../../common/menu/controller/DiagramMenuController";
import app = require("../../../require/app");
import {Interpreter} from "../../interpreter/Interpreter";
import {ElementTypes} from "core/editorCore/model/ElementTypes";
import {DiagramScene} from "core/editorCore/model/DiagramScene";
import {DiagramEditorController} from "core/editorCore/controller/DiagramEditorController";
import {ElementConstructor} from "core/editorCore/model/ElementConstructor";
export class RobotsDiagramEditorController extends DiagramEditorController {

    private menuController: DiagramMenuController;
    private gesturesController: GesturesController;
    private diagramInterpreter: Interpreter;
    //Hack for firefox
    static $$ngIsClass: boolean;


    constructor($scope, $attrs) {
        super($scope, $attrs);

        var scene: DiagramScene = this.diagramEditor.getScene();
        this.menuController = new DiagramMenuController(this);
        this.gesturesController = new GesturesController(this.sceneController, this.diagramEditor.getScene());
        this.diagramInterpreter = new Interpreter();
        this.elementConstructor = new ElementConstructor(scene);

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
        }, "", "robots");
    }

    public openTwoDModel(): void {
        $("#diagram-area").hide();
        $("#two-d-model-area").show();
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
RobotsDiagramEditorController.$$ngIsClass = true;
app.controller("RobotsDiagramEditorController", RobotsDiagramEditorController);
console.log("Adding controller RobotsDiagramEditorController");
