export enum Direction {
    Up, Down, Left, Right, None
}

export class Scroller {

    private scroll: boolean;

    private intervalId: number;

    private direction: Direction;

    constructor() {
        this.scroll = false;
        this.direction = Direction.None;
    }

    public getDirection(): Direction {
        return this.direction;
    }

    public setDirection(value: Direction) {
        this.direction = value;
    }

    public getIntervalId(): number {
        return this.intervalId;
    }

    public setIntervalId(value: number) {
        this.intervalId = value;
    }

    public getScroll(): boolean {
        return this.scroll;
    }

    public setScroll(value: boolean) {
        this.scroll = value;
    }

}