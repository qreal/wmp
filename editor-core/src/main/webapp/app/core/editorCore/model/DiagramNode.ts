import {PropertyEditElement} from "./PropertyEditElement";
import {DiagramElement} from "./DiagramElement";
import {DiagramContainer} from "./DiagramContainer";
export interface DiagramNode extends DiagramElement {

    getX(): number;
    getY(): number;
    getImagePath(): string;
    getSize() : string;
    getParentNode(): DiagramContainer;
    setPosition(x: number, y: number, zoom: number, cellView : joint.dia.CellView): void;
    setSize(width: number, height: number, cellView : joint.dia.CellView): void;
    setParentNode(parent: DiagramContainer): void;
    getPropertyEditElement(): PropertyEditElement;
    initPropertyEditElements(zoom: number): void;
    initResize(bbox, x: number, y: number, paddingPercent) : void;
    completeResize() : void;
    isResizing() : boolean;
    pointermove(cellView, evt, x, y) : void;

}