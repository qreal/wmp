import {DiagramElement} from "../DiagramElement";
import {Command} from "./Command";
export class CreateElementCommand implements Command {

    public static sideEffect : Command = null;

    private element: DiagramElement;
    private executionFunction: (element: DiagramElement) => void;
    private revertFunction: (element: DiagramElement) => void;

    constructor(element: DiagramElement, executionFunction: (element: DiagramElement) => void,
                revertFunction: (element: DiagramElement) => void) {
        this.element = element;
        this.executionFunction = executionFunction;
        this.revertFunction = revertFunction;
    }

    public execute(): void {
        this.executionFunction(this.element);
        this.makeSideEffect();
    }

    public revert(): void {
        this.revertFunction(this.element);
        this.makeSideEffect();
    }

    public isRevertible(): boolean {
        return true;
    }

    private makeSideEffect() {
        if (CreateElementCommand.sideEffect != null) {
            CreateElementCommand.sideEffect.execute();
        }
    }

}