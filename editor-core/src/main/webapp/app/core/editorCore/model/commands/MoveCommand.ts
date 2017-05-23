import {Command} from "./Command";
export class MoveCommand implements Command {

    private oldX: number;
    private oldY: number;
    private newX: number;
    private newY: number;
    private zoom: number;
    private cellView : joint.dia.CellView;
    private executionFunction: (x: number, y: number) => void;

    constructor(oldX: number, oldY: number, newX: number, newY: number, zoom: number,
                cellView : joint.dia.CellView,
                executionFunction: (x: number, y: number) => void) {
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
        this.zoom = zoom;
        this.cellView = cellView;
        this.executionFunction = executionFunction;
    }

    public execute(): void {
        this.executionFunction(this.newX - this.oldX, this.newY - this.oldY);
    }

    public revert(): void {
        this.executionFunction(this.oldX - this.newX, this.oldY - this.newY);
    }

    public isRevertible(): boolean {
        return !(this.newX === this.oldX && this.newY === this.oldY);
    }

}