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

/// <reference path="../../../interfaces/engine/items/WallItem.ts" />
/// <reference path="../../../interfaces/engine/model/WorldModel.ts" />
/// <reference path="../../../../vendor.d.ts" />

class WallItemImpl implements WallItem {
    private static width: number = 15;
    private path: RaphaelPath;
    private worldModel: WorldModel;
    private handleStart: RaphaelElement;
    private handleEnd: RaphaelElement;
    private pathArray;

    constructor(worldModel: WorldModel, xStart: number, yStart: number, xEnd: number, yEnd: number,
                isInteractive: boolean) {
        var paper = worldModel.getPaper();
        this.worldModel = worldModel;
        this.path = paper.path("M" + xStart + " " + yStart + " L" + xEnd + " " + yEnd);
        this.path.attr({
            "stroke-width": WallItemImpl.width
        });
        $(this.path.node).attr("class", "path");
        $(".path").attr("stroke", "url(#wall_pattern)");
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

    static getWidth(): number {
        return WallItemImpl.width;
    }

    setDraggable(zoom: number) {
        var wall = this;
        wall.path.attr({cursor: "pointer"});

        var start = function () {
                if (!wall.worldModel.getDrawMode()) {
                    this.cx = this.attr("cx");
                    this.cy = this.attr("cy");
                }
                return this;
            },
            moveStart = function (dx, dy) {
                if (!wall.worldModel.getDrawMode()) {
                    var newX = this.cx + dx / zoom;
                    var newY = this.cy + dy / zoom;
                    wall.updateStart(newX, newY)
                }
                return this;
            },
            moveEnd = function (dx, dy) {
                if (!wall.worldModel.getDrawMode()) {
                    var newX = this.cx + dx / zoom;
                    var newY = this.cy + dy / zoom;
                    wall.updateEnd(newX, newY);
                }
                return this;
            },
            up = function () {
                return this;
            };

        wall.handleStart.drag(moveStart, start, up);
        wall.handleEnd.drag(moveEnd, start, up);


        var startPath = function () {
                if (!wall.worldModel.getDrawMode()) {
                    this.startX = wall.pathArray[0][1];
                    this.startY = wall.pathArray[0][2]
                    this.endX = wall.pathArray[1][1];
                    this.endY = wall.pathArray[1][2]
                    this.ox = this.attr("x");
                    this.oy = this.attr("y");
                    wall.worldModel.setCurrentElement(wall);
                }
                return this;
            },
            movePath = function (dx, dy) {
                dx /= zoom;
                dy /= zoom;
                if (!wall.worldModel.getDrawMode()) {
                    var trans_x = dx - this.ox;
                    var trans_y = dy - this.oy;

                    wall.pathArray[0][1] = this.startX + dx;
                    wall.pathArray[0][2] = this.startY + dy;
                    wall.pathArray[1][1] = this.endX + dx;
                    wall.pathArray[1][2] = this.endY + dy;
                    wall.path.attr({path: wall.pathArray});

                    this.ox = dx;
                    this.oy = dy;

                    var hStartX = wall.handleStart.attr("cx") + trans_x;
                    var hStartY = wall.handleStart.attr("cy") + trans_y;
                    var hEndX = wall.handleEnd.attr("cx") + trans_x;
                    var hEndY = wall.handleEnd.attr("cy") + trans_y;

                    wall.handleStart.attr({cx: hStartX, cy: hStartY});
                    wall.handleEnd.attr({cx: hEndX, cy: hEndY});
                }
                return this;
            },
            upPath = function () {
                return this;
            };

        wall.path.drag(movePath, startPath, upPath);
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