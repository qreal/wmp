export class Property {

    name: string;
    type: string;
    value: string;

    coordinates = undefined;

    constructor(name: string, type: string, value: string) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public setCoordinates(newX: number, newY: number): void {
        this.coordinates = {
            x: newX,
            y: newY,
        };

        console.log("Property with value " + this.value + " coordinates : x, y " + this.coordinates.x + " " + this.coordinates.y);
    }
}