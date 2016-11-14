/// <reference path="../../../common/interfaces/editorCore.d.ts" />
/// <reference path="../../../common/gestures/GesturesController.ts" />

class RobotsSceneController extends SceneController {
    private gesturesController: GesturesController;

    constructor(diagramEditorController: DiagramEditorController, paper: DiagramScene) {
        super(diagramEditorController, paper);
        this.gesturesController = new GesturesController(this, paper);

        document.addEventListener('mousedown', (event) => { this.gesturesController.onMouseDown(event) } );
        document.addEventListener('mouseup', (event) => { this.gesturesController.onMouseUp(event) } );
        $("#" + this.scene.getId()).mousemove((event) => { this.gesturesController.onMouseMove(event) } );
    }

    private blankPoinerdownListener(event, x, y): void {
        if (event.button == 2) {
            this.gesturesController.startDrawing();
        }
    }

    private cellPointerdownListener(cellView, event, x, y): void {
        if (event.button == 2) {
            this.rightClickFlag = true;
            this.gesturesController.startDrawing();
        }
    }

    private cellPointerupListener(cellView, event, x, y): void {
    }
}