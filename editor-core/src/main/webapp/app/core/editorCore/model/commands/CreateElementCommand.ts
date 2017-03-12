import {DiagramElement} from "../DiagramElement";
import {Command} from "./Command";
import {AddElementEvent} from "../../events/AddElementEvent";
export class CreateElementCommand implements Command {

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
        AddElementEvent.signalEvent();
    }

    public revert(): void {
        this.revertFunction(this.element);
        AddElementEvent.signalEvent();
    }

    public isRevertible(): boolean {
        return true;
    }
}