/// <reference path="../../../interfaces/engine/items/CubicBezierItem.ts" />
/// <reference path="../../../interfaces/engine/model/WorldModel.ts" />
/// <reference path="../../../../../vendor.d.ts" />

class CubicBezierItemImpl implements CubicBezierItem {

    private path: RaphaelPath;
    private worldModel: WorldModel;

    constructor(worldModel: WorldModel, xStart: number, yStart: number, xEnd: number, yEnd: number,
                cp1X: number, cp1Y: number, cp2X: number, cp2Y: number,
                width: number, rgbaColor: RGBAColor, isInteractive: boolean) {
        var paper: RaphaelPaper = worldModel.getScene();
        this.worldModel = worldModel;
        this.path = paper.path("M " + xStart + "," + yStart + " C " + cp1X + "," + cp1Y + " " + cp2X + "," + cp2Y +
            " " + xEnd + "," + yEnd);
        this.path.toBack();
        this.path.attr({
            "stroke": rgbaColor.rgb,
            "stroke-opacity": rgbaColor.alpha,
            "stroke-width": width
        });

        worldModel.insertBeforeRobots(this.path);
    }

    getPath(): RaphaelPath {
        return this.path;
    }

    updateStart(x: number, y: number): void {
    }

    updateEnd(x: number, y: number): void {
    }

    updateCP1(x: number, y: number): void {
    }

    updateCP2(x: number, y: number): void {
    }

    remove(): void {
        this.path.remove();
    }

    showHandles(): void {
    }

    hideHandles(): void {
    }
    
}