export interface TwoDModelEngineFacade {
    setDrawLineMode(): void;
    setDrawWallMode(): void;
    setDrawPencilMode(): void;
    setDrawEllipseMode(): void;
    setNoneMode(): void;
    followRobot(value: boolean): void;
}