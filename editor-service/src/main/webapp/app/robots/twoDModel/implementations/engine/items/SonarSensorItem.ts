import {Constants} from "../model/Constants";
import {RobotItem} from "../../../interfaces/engine/items/RobotItem";
import {SensorItem} from "./SensorItem";
import {TwoDPosition} from "../../../types/TwoDPosition";
import {DeviceInfo} from "../../../interfaces/robotModel/DeviceInfo";
import {WorldModel} from "../../../interfaces/engine/model/WorldModel";
export class SonarSensorItem extends SensorItem {
    private scanningRegion: RaphaelPath;
    private sonarRange = 255;
    private regionStartX: number;
    private regionStartY: number;
    protected regionTranslation: string;
    protected regionRotation: string;

    constructor(robotItem: RobotItem, worldModel: WorldModel, sensorType: DeviceInfo,
                pathToImage: string, isInteractive: boolean, position?: TwoDPosition) {
        super(robotItem, worldModel, sensorType, pathToImage, isInteractive, position);
        var paper:RaphaelPaper = worldModel.getScene();
        var defaultPosition = this.getStartPosition(position);

        this.regionStartX = defaultPosition.x + this.width / 2;
        this.regionStartY = defaultPosition.y + this.height / 2;

        var regAngle = 20;
        var halfRegAngleInRad = regAngle / 2 * (Math.PI / 180);

        var rangeInPixels = this.sonarRange * Constants.pixelsInCm;

        var regionTopX = this.regionStartX + Math.cos(halfRegAngleInRad) * rangeInPixels;
        var regionTopY = this.regionStartY - Math.sin(halfRegAngleInRad) * rangeInPixels;

        var regionBottomX = regionTopX;
        var regionBottomY = this.regionStartY + Math.sin(halfRegAngleInRad) * rangeInPixels;

        this.scanningRegion = paper.path("M" + this.regionStartX + "," + this.regionStartY +
        "L" + regionTopX + "," + regionTopY +
        "Q" + (this.regionStartX + rangeInPixels) + "," + this.regionStartY + " " + regionBottomX + "," + regionBottomY +
        "Z");
        this.scanningRegion.attr({fill: "#c5d0de", stroke: "#b1bbc7", opacity: 0.5});

        worldModel.insertAfterRobots(this.scanningRegion);

        this.regionTranslation = "T0,0";
        this.regionRotation = "R0";
    }

    setStartPosition() {
        super.setStartPosition();
        this.updateRegionTransformation();
    }

    move(deltaX: number, deltaY: number): void {
        super.move(deltaX, deltaY);
        this.updateRegionTransformation();
    }

    public updateTransformation(): void {
        super.updateTransformation();
        this.updateRegionTransformation();
    }

    rotate(angle: number): void {
        super.rotate(angle);
        this.updateRegionTransformation();
    }

    remove():void {
        super.remove();
        this.scanningRegion.remove();
    }

    protected updateRegionTransformation(): void {
        var offsetX: number = this.offsetPosition.x + this.robotOffsetPosition.x;
        var offsetY: number = this.offsetPosition.y + this.robotOffsetPosition.y;
        this.regionTranslation = "T" + offsetX + "," + offsetY;
        this.scanningRegion.transform(this.getRegionTransformation());
    }

    private getRegionTransformation(): string {
        return  this.regionTranslation + this.getRobotRotationTransformation() +
            "r" + this.direction + "," + this.regionStartX + "," + this.regionStartY;
    }
}