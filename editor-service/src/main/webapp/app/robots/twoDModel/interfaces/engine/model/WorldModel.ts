export interface WorldModel {
    setDrawLineMode(): void;
    setDrawWallMode(): void;
    setDrawPencilMode(): void;
    setDrawEllipseMode(): void;
    getDrawMode(): number;
    setNoneMode(): void;
    getScene(): RaphaelPaper;
    getZoom(): number;
    setCurrentElement(element): void;
    clearScene(): void;
    deserialize(xml, offsetX: number, offsetY: number): void;
    addRobotItemElement(element: RaphaelElement): void;
    insertBeforeRobots(element: RaphaelElement): void;
    insertAfterRobots(element: RaphaelElement): void;
}