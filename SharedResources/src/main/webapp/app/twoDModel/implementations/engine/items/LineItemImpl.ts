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

/// <reference path="../../../interfaces/engine/items/LineItem.ts" />
/// <reference path="../../../interfaces/engine/model/WorldModel.ts" />
/// <reference path="../../../../vendor.d.ts" />

class LineItemImpl implements LineItem {
    private path: RaphaelPath;
    private worldModel: WorldModel;
    private handleStart: RaphaelElement;
    private handleEnd: RaphaelElement;
    private pathArray;

    constructor(worldModel: WorldModel, xStart: number, yStart: number, xEnd: number, yEnd: number,
                width: number, rgbaColor: RGBAColor, isInteractive: boolean) {
        var paper = worldModel.getPaper();
        this.worldModel = worldModel;
        this.path = paper.path("M" + xStart + " " + yStart + " L" + xEnd + " " + yEnd);
        this.path.attr({
            "stroke": rgbaColor.rgb,
            "stroke-opacity": rgbaColor.alpha,
            "stroke-width": width
        });
        this.pathArray = this.path.attr("path");

        worldModel.insertBeforeRobots(this.path);

        var handleRadius: number = 10;

        var handleAttrs = {
            fill: "white",
            "fill-opacity": 0,
            cursor: "pointer",
            "stroke-width": 1,
            stroke: "black"
        };

        this.handleStart = paper.circle(xStart, yStart, handleRadius).attr(handleAttrs);
        this.handleEnd = paper.circle(xEnd, yEnd, handleRadius).attr(handleAttrs);

        if (isInteractive) {
            this.setDraggable(worldModel.getZoom());
        }
    }

    setDraggable(zoom: number): void {
        var line = this;
        line.path.attr({cursor: "pointer"});

        var startHandle = function () {
                if (!line.worldModel.getDrawMode()) {
                    this.cx = this.attr("cx");
                    this.cy = this.attr("cy");
                }
                return this;
            },
            moveHandleStart = function (dx, dy) {
                if (!line.worldModel.getDrawMode()) {
                    var newX = this.cx + dx / zoom;
                    var newY = this.cy + dy / zoom;
                    line.updateStart(newX, newY)
                }
                return this;
            },
            moveHandleEnd = function (dx, dy) {
                if (!line.worldModel.getDrawMode()) {
                    var newX = this.cx + dx / zoom;
                    var newY = this.cy + dy / zoom;
                    line.updateEnd(newX, newY);
                }
                return this;
            },
            upHandle = function () {
                return this;
            };

        line.handleStart.drag(moveHandleStart, startHandle, upHandle);
        line.handleEnd.drag(moveHandleEnd, startHandle, upHandle);


        var startPath = function () {
                if (!line.worldModel.getDrawMode()) {
                    this.startX = line.pathArray[0][1];
                    this.startY = line.pathArray[0][2]
                    this.endX = line.pathArray[1][1];
                    this.endY = line.pathArray[1][2]
                    this.ox = this.attr("x");
                    this.oy = this.attr("y");
                    line.worldModel.setCurrentElement(line);
                }
                return this;
            },
            movePath = function (dx, dy) {
                dx /= zoom;
                dy /= zoom;
                if (!line.worldModel.getDrawMode()) {
                    var trans_x = dx - this.ox;
                    var trans_y = dy - this.oy;

                    line.pathArray[0][1] = this.startX + dx;
                    line.pathArray[0][2] = this.startY + dy;
                    line.pathArray[1][1] = this.endX + dx;
                    line.pathArray[1][2] = this.endY + dy;
                    line.path.attr({path: line.pathArray});

                    this.ox = dx;
                    this.oy = dy;

                    var hStartX = line.handleStart.attr("cx") + trans_x;
                    var hStartY = line.handleStart.attr("cy") + trans_y;
                    var hEndX = line.handleEnd.attr("cx") + trans_x;
                    var hEndY = line.handleEnd.attr("cy") + trans_y;

                    line.handleStart.attr({cx: hStartX, cy: hStartY});
                    line.handleEnd.attr({cx: hEndX, cy: hEndY});
                }
                return this;
            },
            upPath = function () {
                return this;
            };

        line.path.drag(movePath, startPath, upPath);
    }

    getPath(): RaphaelPath {
        return this.path;
    }

    updateStart(x: number, y: number): void {
        this.pathArray[0][1] = x;
        this.pathArray[0][2] = y;
        this.path.attr({path: this.pathArray});
        this.handleStart.attr({cx: x, cy: y});
    }

    updateEnd(x: number, y: number): void {
        this.pathArray[1][1] = x;
        this.pathArray[1][2] = y;
        this.path.attr({path: this.pathArray});
        this.handleEnd.attr({cx: x, cy: y});
    }

    hideHandles(): void {
        this.handleStart.hide();
        this.handleEnd.hide();
    }

    showHandles(): void {
        this.handleStart.show();
        this.handleEnd.show();
    }

    remove(): void {
        this.handleStart.remove();
        this.handleEnd.remove();
        this.path.remove();
    }
    
}