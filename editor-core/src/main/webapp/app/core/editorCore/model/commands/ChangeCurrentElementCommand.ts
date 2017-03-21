import {DiagramElement} from "../DiagramElement";
import {Command} from "./Command";
export class ChangeCurrentElementCommand implements Command {

    private element: DiagramElement;
    private oldElement: DiagramElement;
    private executionFunction: (element: DiagramElement) => void;

    constructor(element: DiagramElement, oldElement: DiagramElement,
                executionFunction: (element: DiagramElement) => void) {
        this.element = element;
        this.oldElement = oldElement;
        this.executionFunction = executionFunction;
    }

    public execute(): void {
        this.executionFunction(this.element);
    }

    public revert(): void {
        this.executionFunction(this.oldElement);
    }

    public isRevertible() : boolean{
        return (this.oldElement !== this.element);
    }

}