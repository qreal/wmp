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

/// <reference path="../../../interfaces/engine/items/CubicBezierItem.ts" />
/// <reference path="../../../interfaces/engine/model/WorldModel.ts" />
/// <reference path="../../../../vendor.d.ts" />

class CubicBezierItemImpl implements CubicBezierItem {

    private path: RaphaelPath;
    private worldModel: WorldModel;

    constructor(worldModel: WorldModel, xStart: number, yStart: number, xEnd: number, yEnd: number,
                cp1X: number, cp1Y: number, cp2X: number, cp2Y: number,
                width: number, rgbaColor: RGBAColor, isInteractive: boolean) {
        var paper: RaphaelPaper = worldModel.getPaper();
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