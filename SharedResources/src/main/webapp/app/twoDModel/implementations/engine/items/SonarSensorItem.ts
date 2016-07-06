/*
 * Copyright Vladimir Zakharov 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/// <reference path="SensorItem.ts" />
/// <reference path="../../../interfaces/engine/model/WorldModel.ts" />
/// <reference path="../../../interfaces/engine/items/RobotItem.ts" />
/// <reference path="../../../interfaces/robotModel/DeviceInfo.ts" />
/// <reference path="../../../types/TwoDPosition.ts" />
/// <reference path="../../../../vendor.d.ts" />

class SonarSensorItem extends SensorItem {
    private scanningRegion: RaphaelPath;
    private sonarRange = 255;
    private regionStartX: number;
    private regionStartY: number;
    protected regionTranslation: string;
    protected regionRotation: string;

    constructor(robotItem: RobotItem, worldModel: WorldModel, sensorType: DeviceInfo,
                pathToImage: string, isInteractive: boolean, position?: TwoDPosition) {
        super(robotItem, worldModel, sensorType, pathToImage, isInteractive, position);
        var paper:RaphaelPaper = worldModel.getPaper();
        var defaultPosition = this.getStartPosition(position);

        this.regionStartX = defaultPosition.x + this.width / 2;
        this.regionStartY = defaultPosition.y + this.height / 2

        var regAngle = 20;
        var halfRegAngleInRad = regAngle / 2 * (Math.PI / 180)

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