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

/// <reference path="../../robotModel/robotParts/TouchSensor.ts" />
/// <reference path="../../robotModel/robotParts/ColorSensor.ts" />
/// <reference path="../../robotModel/robotParts/ColorSensorFull.ts" />
/// <reference path="../../robotModel/robotParts/ColorSensorPassive.ts" />
/// <reference path="../../robotModel/robotParts/ColorSensorRed.ts" />
/// <reference path="../../robotModel/robotParts/ColorSensorGreen.ts" />
/// <reference path="../../robotModel/robotParts/ColorSensorBlue.ts" />
/// <reference path="../../robotModel/robotParts/RangeSensor.ts" />
/// <reference path="../../robotModel/robotParts/LightSensor.ts" />
/// <reference path="../../../interfaces/engine/model/WorldModel.ts" />
/// <reference path="../../../interfaces/engine/items/RobotItem.ts" />
/// <reference path="../../../interfaces/robotModel/DeviceInfo.ts" />
/// <reference path="../../../types/TwoDPosition.ts" />
/// <reference path="../../../../constants/GeneralConstants.d.ts" />
/// <reference path="../../../../utils/MathUtils.ts" />
/// <reference path="../../../../vendor.d.ts" />

class SensorItem {
    protected robotItem: RobotItem;
    protected worldModel: WorldModel;
    protected image: RaphaelElement;
    protected width: number;
    protected height: number;
    protected rotationHandle: RaphaelElement;
    protected startCenter: TwoDPosition;
    protected startPosition: TwoDPosition;
    protected startDirection: number;
    protected sensorType: DeviceInfo;
    protected offsetPosition: TwoDPosition;
    protected robotOffsetPosition: TwoDPosition;
    protected direction: number;

    constructor(robotItem: RobotItem, worldModel: WorldModel, sensorType: DeviceInfo,
                pathToImage: string, isInteractive: boolean, position?: TwoDPosition) {
        this.robotItem = robotItem;
        this.worldModel = worldModel;
        var paper: RaphaelPaper = worldModel.getPaper();
        this.sensorType = sensorType;
        this.defineImageSizes(sensorType);
        this.startPosition = this.getStartPosition(position);
        this.offsetPosition = new TwoDPosition();
        this.robotOffsetPosition = new TwoDPosition();
        this.startDirection = 0;
        this.direction = 0;

        this.image = paper.image((pathToImage) ? pathToImage : this.pathToImage(),
            this.startPosition.x, this.startPosition.y, this.width, this.height);

        worldModel.insertAfterRobots(this.image);

        this.startCenter = new TwoDPosition(this.startPosition.x + this.width / 2, this.startPosition.y + this.height / 2);

        var handleRadius: number = 10;

        var handleAttrs = {
            fill: "white",
            "fill-opacity": 0,
            cursor: "pointer",
            "stroke-width": 1,
            stroke: "black"
        };

        this.rotationHandle = paper.circle(this.startPosition.x + this.width + 20,
            this.startPosition.y + this.height / 2, handleRadius).attr(handleAttrs);

        if (isInteractive) {
            this.initDragAndDrop();
        }

        this.hideHandles();
    }

    setStartDirection(direction: number) {
        this.startDirection = direction;
        this.rotate(direction);
    }

    setStartPosition() {
        this.robotOffsetPosition.x = 0;
        this.robotOffsetPosition.y = 0;
        this.image.attr({x: this.startPosition.x, y: this.startPosition.y});
        this.startCenter = new TwoDPosition(this.startPosition.x + this.width / 2, this.startPosition.y + this.height / 2);
        this.rotationHandle.attr({"cx": + this.startPosition.x + this.width + 20, "cy": this.startPosition.y + this.height / 2 });
        this.updateTransformation();
    }

    getStartPosition(position: TwoDPosition): TwoDPosition {
        var startX = this.robotItem.getStartPosition().x;
        var startY = this.robotItem.getStartPosition().y;
        if (position) {
            startX += position.x - this.width / 2;
            startY += position.y - this.height / 2;
        } else {
            startX = startX + this.robotItem.getWidth() + 15;
            startY = startY + this.robotItem.getHeight() / 2 - this.height / 2;
        }
        return new TwoDPosition(startX, startY);
    }

    name(): string {
        if (this.sensorType.isA(TouchSensor)) {
            return "touch";
        } else if (this.sensorType.isA(ColorSensorFull) || this.sensorType.isA(ColorSensorPassive)) {
            return "color_empty";
        } else if (this.sensorType.isA(ColorSensorRed)) {
            return "color_red";
        } else if (this.sensorType.isA(ColorSensorGreen)) {
            return "color_green";
        } else if (this.sensorType.isA(ColorSensorBlue)) {
            return "color_blue";
        } else if (this.sensorType.isA(RangeSensor)) {
            return "sonar";
        } else if (this.sensorType.isA(LightSensor)) {
            return "light";
        } else {
            alert(!"Unknown sensor type");
            return "";
        }
    }

    pathToImage(): string
    {
        return GeneralConstants.APP_ROOT_PATH + "images/2dmodel/sensors/2d_" + this.name() + ".png";
    }

    protected defineImageSizes(sensorType): void {
        if (sensorType.isA(TouchSensor)) {
            this.width = 25;
            this.height = 25;
        } else if (sensorType.isA(ColorSensor) || sensorType.isA(LightSensor)) {
            this.width = 15;
            this.height = 15;
        } else if (sensorType.isA(RangeSensor)) {
            this.width = 35;
            this.height = 35;
        } else {
            alert("Unknown sensor type");
        }
    }

    move(deltaX: number, deltaY: number): void {
        this.robotOffsetPosition.x = deltaX;
        this.robotOffsetPosition.y = deltaY;
        this.updateTransformation();
    }

    rotate(angle: number): void {
        this.direction = angle;
        this.updateTransformation();
    }

    hideHandles(): void {
        this.rotationHandle.hide();
    }

    showHandles(): void {
        this.rotationHandle.toFront();
        this.rotationHandle.show();
    }

    remove(): void {
        this.image.remove();
        this.rotationHandle.remove();
    }

    private initDragAndDrop(): void {
        var sensorItem = this;

        var startHandle = function () {
                this.cx = sensorItem.rotationHandle.matrix.x(this.attr("cx"), this.attr("cy"));
                this.cy = sensorItem.rotationHandle.matrix.y(this.attr("cx"), this.attr("cy"));
                return this;
            },
            moveHandle = function (dx, dy) {
                var newX : number = this.cx + dx * (1 / sensorItem.worldModel.getZoom());
                var newY : number = this.cy + dy * (1 / sensorItem.worldModel.getZoom());

                var center: TwoDPosition = sensorItem.getCurrentCenter();
                var diffX : number = newX - center.x;
                var diffY : number = newY - center.y;
                var tan : number = diffY / diffX;
                var angle : number = Math.atan(tan) / (Math.PI / 180);
                if (diffX < 0) {
                    angle += 180;
                }

                sensorItem.rotate(angle - sensorItem.robotItem.getDirection());

                return this;
            },
            upHandle = function () {
                return this;
            };

        sensorItem.rotationHandle.drag(moveHandle, startHandle, upHandle);

        var start = function (event) {
                if (!sensorItem.worldModel.getDrawMode()) {
                    sensorItem.worldModel.setCurrentElement(sensorItem);
                    this.lastOffsetX = sensorItem.offsetPosition.x;
                    this.lastOffsetY = sensorItem.offsetPosition.y;
                }
                return this;
            },
            move = function (dx, dy) {
                var rotatedDelta: TwoDPosition = MathUtils.rotateVector(dx, dy, -sensorItem.robotItem.getDirection());
                sensorItem.offsetPosition.x = this.lastOffsetX + rotatedDelta.x * (1 / sensorItem.worldModel.getZoom());
                sensorItem.offsetPosition.y = this.lastOffsetY + rotatedDelta.y * (1 / sensorItem.worldModel.getZoom());
                sensorItem.updateTransformation();
                return this;
            },
            up = function () {
                return this;
            };

        this.image.drag(move, start, up);
    }

    public updateTransformation(): void {
        this.image.transform(this.getTransformation());
        this.rotationHandle.transform(this.getHandleTransformation());
    }

    private getTransformation(): string {
        return "T" + this.robotOffsetPosition.x + "," + this.robotOffsetPosition.y +
            "T" + this.offsetPosition.x + "," + this.offsetPosition.y +
            this.getRobotRotationTransformation() +
            this.getRotationTransformation();
    }

    private getHandleTransformation(): string {
        var center = this.getCurrentCenter();
        return "T" + this.robotOffsetPosition.x + "," + this.robotOffsetPosition.y +
            "T" + this.offsetPosition.x + "," + this.offsetPosition.y +
            this.getRobotRotationTransformation() +
            this.getRotationTransformation() + "," + center.x + "," + center.y;
    }

    protected getRobotRotationTransformation(): string {
        var center: TwoDPosition = this.robotItem.getCenter();
        return "R" + this.robotItem.getDirection() + "," + center.x + "," + center.y;
    }

    protected getRotationTransformation(): string {
        return "R" + this.direction;
    }

    private getCurrentCenter(): TwoDPosition {
        var centerX = this.image.matrix.x(this.startCenter.x, this.startCenter.y);
        var centerY = this.image.matrix.y(this.startCenter.x, this.startCenter.y);
        return new TwoDPosition(centerX, centerY);
    }

}