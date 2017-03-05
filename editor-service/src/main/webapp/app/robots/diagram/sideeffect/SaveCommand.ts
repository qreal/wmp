import {Command} from "core/editorCore/model/commands/Command";
import {DiagramMenuController} from "../../../common/menu/controller/DiagramMenuController";
export class SaveCommand implements Command {

    private menuController : DiagramMenuController;

    constructor(menuController : DiagramMenuController) {
        this.menuController = menuController;
    }

    public execute() : void {
        if (this.menuController.diagramSaved) {
            this.menuController.saveCurrentDiagram();
        }
    }

    public revert() : void {

    }

    public isRevertible() : boolean {
        return false;
    }
}

