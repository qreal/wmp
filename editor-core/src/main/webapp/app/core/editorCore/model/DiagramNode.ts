import {PropertyEditElement} from "./PropertyEditElement";
import {DiagramElement} from "./DiagramElement";
export abstract class DiagramNode extends DiagramElement {
    abstract getX(): number;
    abstract getY(): number;
    abstract getImagePath(): string;
    abstract getSize() : string;
    abstract setPosition(x: number, y: number, zoom: number, cellView : joint.dia.CellView): void;
    abstract setSize(width: number, height: number, cellView : joint.dia.CellView): void;
    abstract getPropertyEditElement(): PropertyEditElement;
    abstract initPropertyEditElements(zoom: number): void;
    abstract initResize(bbox, x: number, y: number, paddingPercent) : void;
    abstract completeResize() : void;
    abstract isResizing() : boolean;
    abstract pointermove(cellView, evt, x, y) : void;

    equals(node : DiagramNode) : boolean {
        if (node == undefined) {{
            return false;
        }}
        let elementsEquality = super.equals(node);
        let position = _.isEqual(this.getX(), node.getX()) && _.isEqual(this.getY(), node.getY());
        let image = _.isEqual(this.getImagePath(), node.getImagePath());

        let propertyEditElement;
        if (this.getPropertyEditElement() == undefined) {
            propertyEditElement = node.getPropertyEditElement() == undefined;
        } else {
            propertyEditElement = this.getPropertyEditElement().equals(node.getPropertyEditElement())
        }

        return elementsEquality && position && image && propertyEditElement;
    }
}