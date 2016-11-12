/// <reference path="../../../interfaces/engine/model/WorldModel.ts" />
/// <reference path="../../../../../common/constants/GeneralConstants.ts" />
/// <reference path="../../../../../vendor.d.ts" />

class StartPositionItem {
    private width = 15;
    private height = 15;
    private image: RaphaelElement;

    constructor(worldModel: WorldModel, x: number, y: number, direction: number) {
        this.image = worldModel.getScene().image(GeneralConstants.APP_ROOT_PATH + "images/2dmodel/cross.png",
            x - this.width / 2, y - this.height / 2,
            this.width, this.height);
        this.image.transform("R" + direction);
        this.image.toBack();
    }

    remove(): void {
        this.image.remove();
    }
    
}