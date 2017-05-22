import {Command} from "./Command";
export class ResizeCommand implements Command {

    private oldWidth: number;
    private oldHeight: number;
    private newWidth: number;
    private newHeight: number;
    private executionFunction: (x: number, y: number) => void;

    constructor(oldWidth: number, oldHeight: number, newWidth: number, newHeight: number,
                executionFunction: (x: number, y: number) => void) {
        this.oldWidth = oldWidth;
        this.oldHeight = oldHeight;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        this.executionFunction = executionFunction;
    }

    public execute(): void {
        this.executionFunction(this.newWidth, this.newHeight);
    }

    public revert(): void {
        this.executionFunction(this.oldWidth, this.oldHeight);
    }

    public isRevertible(): boolean {
        return !(this.newWidth === this.oldWidth && this.newHeight === this.oldHeight);
    }

}