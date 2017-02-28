class ResizeCommand implements Command {

    private oldWidth: number;
    private oldHeight: number;
    private newWidth: number;
    private newHeight: number;
    private cellView : joint.dia.CellView;
    private executionFunction: (x: number, y: number, cellView : joint.dia.CellView) => void;

    constructor(oldWidth: number, oldHeight: number, newWidth: number, newHeight: number, cellView : joint.dia.CellView,
                executionFunction: (x: number, y: number, cellView : joint.dia.CellView) => void) {
        this.oldWidth = oldWidth;
        this.oldHeight = oldHeight;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        this.cellView = cellView;
        this.executionFunction = executionFunction;
    }

    public execute(): void {
        this.executionFunction(this.newWidth, this.newHeight, this.cellView);
    }

    public revert(): void {
        this.executionFunction(this.oldWidth, this.oldHeight, this.cellView);
    }

    public isRevertible(): boolean {
        return !(this.newWidth === this.oldWidth && this.newHeight === this.oldHeight);
    }

}