enum Direction {
    Up, Down, Left, Right, None
}

class Scroller {

    private scroll: boolean;

    private intervalId: number;

    private direction: Direction;

    constructor() {
        this.scroll = false;
        this.direction = Direction.None;
    }

    get direction(): Direction {
        return this.direction;
    }

    set direction(value: Direction) {
        this.direction = value;
    }

    get intervalId(): number {
        return this.intervalId;
    }

    set intervalId(value: number) {
        this.intervalId = value;
    }

    get scroll(): boolean {
        return this.scroll;
    }

    set scroll(value: boolean) {
        this.scroll = value;
    }

}