import {RegionItem} from "./RegionItem";
import {TwoDPosition} from "../../../../types/TwoDPosition";
import {WorldModel} from "../../../../interfaces/engine/model/WorldModel";
export class EllipseRegion extends RegionItem {

    constructor(worldModel: WorldModel) {
        super(worldModel);
        this.shape = worldModel.getScene().ellipse(0, 0, 0, 0);
        this.setColor(this.defaultColor);
        this.setWidht(this.defaultWidth);
        this.setHeight(this.defaultHeight);
        this.shape.toBack();
    }

    setPosition(position: TwoDPosition): void {
        this.shape.attr({cx: position.x + this.getWith(), cy: position.y + this.getHeight()});
    }

    setWidht(width: number): void {
        this.shape.attr({rx: width / 2});
    }

    setHeight(height: number): void {
        this.shape.attr({ry: height / 2});
    }

    getPosition(): TwoDPosition {
        return new TwoDPosition(this.shape.attr("cx"), this.shape.attr("cy"));
    }

    getWith(): number {
        return this.shape.attr("rx");
    }

    getHeight(): number {
        return this.shape.attr("ry");
    }
}