enum Direction {
    Up, Down, Left, Right, None
}

class Scroller {

    private _scroll: boolean;

    private _intervalId: number;

    private _direction: Direction;

    constructor() {
        this.scroll = false;
        this.direction = Direction.None;
    }

    get direction(): Direction {
        return this._direction;
    }

    set direction(value: Direction) {
        this._direction = value;
    }

    get intervalId(): number {
        return this._intervalId;
    }

    set intervalId(value: number) {
        this._intervalId = value;
    }

    get scroll(): boolean {
        return this._scroll;
    }

    set scroll(value: boolean) {
        this._scroll = value;
    }

}