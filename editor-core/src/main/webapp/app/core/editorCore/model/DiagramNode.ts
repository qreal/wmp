import {PropertyEditElement} from "./PropertyEditElement";
import {DiagramElement} from "./DiagramElement";
export interface DiagramNode extends DiagramElement {

    getX(): number;
    getY(): number;
    getImagePath(): string;
    getSize() : string;
    setPosition(x: number, y: number, zoom: number, cellView : joint.dia.CellView): void;
    setSize(width: number, height: number, cellView : joint.dia.CellView): void;
    getPropertyEditElement(): PropertyEditElement;
    initPropertyEditElements(zoom: number): void;
    initResize(bbox, x: number, y: number, paddingPercent) : void;
    completeResize() : void;
    isResizing() : boolean;
    pointermove(cellView, evt, x, y) : void;

}