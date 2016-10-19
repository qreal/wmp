class MoveCommand implements Command {

    private oldX: number;
    private oldY: number;
    private newX: number;
    private newY: number;
    private zoom: number;
    private executionFunction: (x: number, y: number, zoom: number) => void;

    constructor(oldX: number, oldY: number, newX: number, newY: number, zoom: number,
                executionFunction: (x: number, y: number) => void) {
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
        this.zoom = zoom;
        this.executionFunction = executionFunction;
    }

    public execute(): void {
        this.executionFunction(this.newX, this.newY, this.zoom);
    }

    public revert(): void {
        this.executionFunction(this.oldX, this.oldY, this.zoom);
    }

    public isRevertible(): boolean {
        return !(this.newX === this.oldX && this.newY === this.oldY);
    }

}