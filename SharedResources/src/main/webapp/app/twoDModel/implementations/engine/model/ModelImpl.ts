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

/// <reference path="WorldModelImpl.ts" />
/// <reference path="../../robotModel/TwoDRobotModel.ts" />
/// <reference path="../../../interfaces/engine/model/Model.ts" />
/// <reference path="../../../interfaces/engine/model/WorldModel.ts" />
/// <reference path="../../../interfaces/engine/model/Settings.ts" />
/// <reference path="../../../interfaces/engine/model/RobotModel.ts" />
/// <reference path="../../../types/TwoDPosition.ts" />
/// <reference path="../../../../utils/MathUtils.ts" />

class ModelImpl implements Model {
    private worldModel : WorldModel;
    private settings : Settings;
    private robotModels : RobotModel[] = [];
    private minX: number;
    private minY: number;
    private isInteractive: boolean;
    private timeline: Timeline;

    constructor(zoom: number) {
        var interactiveAttr: string = $("#twoDModel_stage").attr("interactive");
        this.isInteractive = (interactiveAttr === "false") ? false : true;
        var model = this;
        model.worldModel = new WorldModelImpl(zoom, this.isInteractive);
        this.minX = 3000;
        this.minY = 3000;
        this.timeline = new TimelineImpl();
    }

    getTimeline(): Timeline {
        return this.timeline;
    }

    getWorldModel() : WorldModel {
        return this.worldModel;
    }

    getRobotModels() : RobotModel[] {
        return this.robotModels;
    }

    getSetting() : Settings {
        return this.settings;
    }

    addRobotModel(robotModel: TwoDRobotModel): void {
        var model = this;
        $(document).ready(() => {
            var robot:RobotModel = new RobotModelImpl(model.worldModel, robotModel, new TwoDPosition(300, 300),
                this.isInteractive);
            model.robotModels.push(robot);
            model.timeline.addRobotModel(robot);
        });
    }

    deserialize(xml): void {
        this.findMinPos(xml);
        var offsetX = (this.minX < 0) ? (-this.minX + 700) : 700;
        var offsetY = (this.minY < 0) ? (-this.minY + 700) : 700;
        this.worldModel.deserialize(xml, offsetX, offsetY);

        var robots = xml.getElementsByTagName("robot");

        for (var i = 0; i < robots.length; i++) {
            this.robotModels[i].deserialize(robots[i], offsetX, offsetY);
        }
    }

    private findMinPos(xml): void {
        var walls = xml.getElementsByTagName("wall");
        if (walls) {
            this.compareAndSetMinPositionWithWalls(walls);
        }

        var colorFields = xml.getElementsByTagName("colorFields")[0];
        if (colorFields) {
            this.compareAndSetMinPositionWithColorFields(colorFields);
        }

        var regions = xml.getElementsByTagName("region");
        if (regions) {
            this.compareAndSetMinPositionWithRegions(regions);
        }

        var robots = xml.getElementsByTagName("robot");
        if (robots) {
            this.compareAndSetMinPositionWithRobots(robots);
        }
    }

    private compareAndSetMinPositionWithWalls(walls): void {
        for (var i = 0; i < walls.length; i++) {
            var beginPos: TwoDPosition = this.getPosition(walls[i], 'begin');
            var endPos: TwoDPosition = this.getPosition(walls[i], 'end');
            this.minX = MathUtils.min(beginPos.x, this.minX);
            this.minY = MathUtils.min(beginPos.y, this.minY);
            this.minX = MathUtils.min(endPos.x, this.minX);
            this.minY = MathUtils.min(endPos.y, this.minY);
        }
    }

    private compareAndSetMinPositionWithColorFields(colorFields): void {
        var lines = colorFields.getElementsByTagName("line");
        if (lines) {
            this.compareAndSetMinPositionWithLines(lines);
        }

        var cubicBeziers = colorFields.getElementsByTagName("cubicBezier")
        if (cubicBeziers) {
            this.compareAndSetMinPositionWithCubicBeziers(cubicBeziers);
        }
    }

    private compareAndSetMinPositionWithLines(lines): void {
        for (var i = 0; i < lines.length; i++) {
            var beginPos: TwoDPosition = this.getPosition(lines[i], 'begin');
            var endPos: TwoDPosition = this.getPosition(lines[i], 'end');
            var width: number = parseInt(lines[i].getAttribute('stroke-width'));

            this.minX = MathUtils.min(beginPos.x - width, this.minX);
            this.minY = MathUtils.min(beginPos.y - width, this.minY);
            this.minX = MathUtils.min(endPos.x - width, this.minX);
            this.minY = MathUtils.min(endPos.y - width, this.minY);
        }
    }

    private compareAndSetMinPositionWithCubicBeziers(cubicBeziers): void {
        for (var i = 0; i < cubicBeziers.length; i++) {
            var beginPos: TwoDPosition = this.getPosition(cubicBeziers[i], 'begin');
            var endPos: TwoDPosition = this.getPosition(cubicBeziers[i], 'end');
            var width: number = parseInt(cubicBeziers[i].getAttribute('stroke-width'));

            this.minX = MathUtils.min(beginPos.x - width, this.minX);
            this.minY = MathUtils.min(beginPos.y - width, this.minY);
            this.minX = MathUtils.min(endPos.x - width, this.minX);
            this.minY = MathUtils.min(endPos.y - width, this.minY);
        }
    }

    private compareAndSetMinPositionWithRegions(regions): void {
        for (var i = 0; i < regions.length; i++) {
            var x = parseFloat(regions[i].getAttribute('x'));
            var y = parseFloat(regions[i].getAttribute('y'));
            this.minX = MathUtils.min(x, this.minX);
            this.minY = MathUtils.min(y, this.minY);
        }
    }

    private compareAndSetMinPositionWithRobots(robots): void {
        for (var i = 0; i < robots.length; i++) {
            var posString = robots[i].getAttribute('position');
            var pos = this.parsePositionString(posString);
            this.minX = MathUtils.min(pos.x, this.minX);
            this.minY = MathUtils.min(pos.y, this.minY);

            var sensors = robots[i].getElementsByTagName("sensor");
            if (sensors) {
                this.compareAndSetMinPositionWithSensors(sensors);
            }

            var startPosition = robots[i].getElementsByTagName("startPosition")[0];
            if (startPosition) {
                var x = parseFloat(startPosition.getAttribute('x'));
                var y = parseFloat(startPosition.getAttribute('y'));
                this.minX = MathUtils.min(x, this.minX);
                this.minY = MathUtils.min(y, this.minY);
            }
        }
    }

    private compareAndSetMinPositionWithSensors(sensors): void {
        for (var j = 0; j < sensors.length; j++) {
            var posString = sensors[j].getAttribute('position');
            var pos = this.parsePositionString(posString);
            this.minX = MathUtils.min(pos.x, this.minX);
            this.minY = MathUtils.min(pos.y, this.minY);
        }
    }

    private getPosition(element, positionName): TwoDPosition {
        var positionStr: string = element.getAttribute(positionName);
        return this.parsePositionString(positionStr);
    }

    private parsePositionString(positionStr: string): TwoDPosition {
        var splittedStr = positionStr.split(":");
        var x = parseFloat(splittedStr[0]);
        var y = parseFloat(splittedStr[1]);
        return new TwoDPosition(x, y);
    }
}