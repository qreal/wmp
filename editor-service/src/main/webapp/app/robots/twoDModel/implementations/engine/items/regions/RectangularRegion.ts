import {RegionItem} from "./RegionItem";
import {WorldModel} from "../../../../interfaces/engine/model/WorldModel";
export class RectangularRegion extends RegionItem {

    constructor(worldModel: WorldModel) {
        super(worldModel);
        this.shape = worldModel.getScene().rect(0, 0, 0, 0);
        this.setColor(this.defaultColor);
        this.setWidht(this.defaultWidth);
        this.setHeight(this.defaultHeight);
        this.shape.toBack();
    }
}