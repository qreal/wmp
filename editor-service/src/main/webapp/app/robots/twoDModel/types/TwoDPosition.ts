export class TwoDPosition {
    x: number;
    y: number;

    constructor(x?: number, y?: number) {
        this.x = (x) ? x : 0;
        this.y = (y) ? y : 0;
    }
}