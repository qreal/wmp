/// <reference path="DiagramElement.ts" />

interface DiagramNode extends DiagramElement {
    getX(): number;
    getY(): number;
    getImagePath(): string;
    setPosition(x: number, y: number, zoom: number): void;
    getPropertyEditElement(): PropertyEditElement;
    initPropertyEditElements(zoom: number): void;
}
