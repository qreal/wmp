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

/// <reference path="../../../types/TwoDPosition.ts" />
/// <reference path="../../../../utils/MathUtils.ts" />
/// <reference path="../../../../vendor.d.ts" />

class Marker {

    private paper: RaphaelPaper;
    private down: boolean;
    private color: string;
    private robotCenter: TwoDPosition;
    private prevCenter: TwoDPosition;
    private height: number = 6;
    private pointSet: RaphaelSet;

    constructor(paper: RaphaelPaper, robotCenter: TwoDPosition) {
        this.paper = paper;
        this.down = false;
        this.color = "#000000";
        this.robotCenter = robotCenter;
        this.prevCenter = new TwoDPosition(robotCenter.x, robotCenter.y);
        this.pointSet = paper.set();
    }

    setCenter(robotCenter: TwoDPosition): void {
        this.prevCenter = new TwoDPosition(this.robotCenter.x, this.robotCenter.y);
        this.robotCenter = robotCenter;
    }

    setColor(color: string): void {
        this.color = color;
    }

    isDown(): boolean {
        return this.down;
    }

    setDown(down: boolean): void {
        this.down = down;
    }

    clear(): void {
        while(this.pointSet.length) {
            this.pointSet.pop().remove();
        }
        this.prevCenter = new TwoDPosition(this.robotCenter.x, this.robotCenter.y);
    }

    drawPoint(): void {
        var length = MathUtils.twoPointLenght(this.robotCenter.x, this.robotCenter.y,
                this.prevCenter.x, this.prevCenter.y);

        var diffX = this.robotCenter.x - this.prevCenter.x;
        var diffY = this.robotCenter.y - this.prevCenter.y;
        var angle;
        if (length) {
            var sin = Math.abs(diffY) / length;
            angle = MathUtils.toDeg(Math.asin(sin));

            if (diffX > 0 && diffY < 0) {
                angle = -angle;
            }
            if (diffX < 0 && diffY > 0) {
                angle = 180 - angle;
            }
            if (diffX < 0 && diffY < 0) {
                angle = -180 + angle;
            }

        } else {
            angle = 0;
        }

        var rect: RaphaelElement = this.paper.rect(this.prevCenter.x - 1,
            this.prevCenter.y - this.height / 2, length + 2, this.height);
        rect.attr({ "stroke-width": 0, "fill": this.color });
        rect.transform("R" + angle + "," + this.prevCenter.x + "," + this.prevCenter.y);
        rect.toBack();
        this.pointSet.push(rect);
    }

}