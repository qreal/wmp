import {Property} from "./Property";
import g = joint.g;

/**
 * Class that wraps text Joint object. Responsible for showing text on screen.
 */
export class PropertyEditElement {
    /**
     * Default size of text font.
     */
    public static fontSize = 16;

    /**
     * Text object which contains text information.
     */
    private jointObject: joint.shapes.basic.Rect;

    /**
     * Creates property at specified coordinates with specified name and value.
     * @param x x center axis coordinate
     * @param y y center axis coordinate
     * @param property property to set
     * @param graph graph for setting text on the plane.
     * @param typeOfInit -- temporary. 0 -- loading from the db. 1 init as usual
     */
    constructor(x: number, y: number, property: Property, graph: joint.dia.Graph, typeOfInit: number) {
        this.createTextObject(x, y, property, graph);

        if (typeOfInit == 0) {
            this.setPosition(x, y);
        }

        this.jointObject.on("cell:pointermove", (cellView, event, x, y): void => {
            this.setPosition(x, y);
        });
    }

    public getTextObject() : joint.shapes.basic.Rect {
        return this.jointObject;
    }

    /**
     * Returns center text x coordinate.
     * @return center text x coordinate.
     */
    getX(): number {
        return (this.jointObject.get("position"))['x'];
    }

    /**
     * Returns center text y coordinate.
     * @return center text y coordinate.
     */
    getY(): number {
        return (this.jointObject.get("position"))['y'];
    }

    /**
     * Sets the position of text.
     * @param x x axis coordinate
     * @param y y axis coordinate
     */
    public setPosition(x: number, y: number): void {
        this.jointObject.position(x, y);

        console.log("Current property edit element position. X : " + x + " Y :" + y);
        console.log("GetX " + this.getX() + " GetY " + this.getY());
    }

    /**
     * Creates new text object. Center of this text will be located by specified coordinates.
     * @param x x coordinate of center.
     * @param y y coordinate of center.
     * @param property new property with text in it.
     * @param graph graph, where text joint object is situated.
     */
    private createTextObject(x: number, y: number, property: Property, graph : joint.dia.Graph) {
        let text = property.name + " : " + property.value;
        let width: number = 0.5 * text.length * PropertyEditElement.fontSize;
        let height: number = PropertyEditElement.fontSize;

        let xPosition = x - width/2;
        let yPosition = y;
        let jointAttributes = {
            position: { x: xPosition, y: yPosition },
            size: { width: width, height: height },
            attrs: {
                rect: { fill: '#FFFFFF',
                    stroke: '#D35400', 'stroke-width': 1
                },
                text: {
                    text: text,
                },
            },
        };

        this.jointObject = new  joint.shapes.basic.Rect(jointAttributes);

        graph.addCell(this.jointObject);
    }

    /**
     * Sets new property. Removes old and replace it with new one property on the same position.
     * @param property property to set.
     * @param graph graph, where text joint object is situated.
     */
    public setProperty(property : Property, graph : joint.dia.Graph): void {
        let xCenter: number;
        let yCenter: number;
        if (this.jointObject !== null && typeof this.jointObject !== 'undefined') {
            xCenter = this.getX() + (this.jointObject.get("size"))['width']/2;
            yCenter = this.getY();
            this.jointObject.remove();
        }

        this.createTextObject(xCenter, yCenter, property, graph);
    }

    /**
     * Sets relative position. In other words: it moves text object by appending coordinates respectively.
     * @param deltaX x coordinate change
     * @param deltaY y coordinate change
     */
    public setRelativePosition(deltaX : number, deltaY : number): void {
        let currentX = this.getX() + deltaX;
        let currentY = this.getY() + deltaY;
        this.setPosition(currentX, currentY);
    }
}