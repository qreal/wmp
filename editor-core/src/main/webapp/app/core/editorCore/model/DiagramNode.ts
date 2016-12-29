/// <reference path="DiagramElement.ts" />

interface DiagramNode extends DiagramElement {
    getX(): number;
    getY(): number;
    getImagePath(): string;
    getSize() : string;
    setPosition(x: number, y: number, zoom: number): void;
    getPropertyEditElement(): PropertyEditElement;
    initPropertyEditElements(zoom: number): void;
    setResizingFields(bbox, x: number, y: number, paddingPercent) : void;
    clearResizingFlags() : void;
    pointermove(cellView, evt, x, y) : void;
}