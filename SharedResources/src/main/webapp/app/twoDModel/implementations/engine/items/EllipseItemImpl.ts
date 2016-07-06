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

/// <reference path="../../../interfaces/engine/items/EllipseItem.ts" />
/// <reference path="../../../interfaces/engine/model/WorldModel.ts" />
/// <reference path="../../../../vendor.d.ts" />

class EllipseItemImpl implements EllipseItem {
    private ellipse: RaphaelElement;
    private worldModel: WorldModel;
    private handleTopLeft: RaphaelElement;
    private handleTopRight: RaphaelElement;
    private handleBottomLeft: RaphaelElement;
    private handleBottomRight: RaphaelElement;
    private handleSize: number  = 10;

    constructor(worldModel: WorldModel, xStart: number, yStart: number, width: number, color: string,
                isInteractive: boolean) {
        var paper = worldModel.getPaper();
        this.worldModel = worldModel;
        this.ellipse = paper.ellipse(xStart, yStart, 0, 0);
        this.ellipse.attr({
            fill: "white",
            "fill-opacity": 0,
            "stroke": color,
            "stroke-width": width
        });
        worldModel.insertBeforeRobots(this.ellipse);

        var handleAttrs = {
            fill: "white",
            "fill-opacity": 0,
            cursor: "pointer",
            "stroke-width": 1,
            stroke: "black"
        };

        this.handleTopLeft = paper.rect(xStart, yStart, this.handleSize, this.handleSize).attr(handleAttrs);
        this.handleTopRight = paper.rect(xStart - this.handleSize, yStart, this.handleSize, this.handleSize).attr(handleAttrs);
        this.handleBottomLeft = paper.rect(xStart, yStart - this.handleSize, this.handleSize, this.handleSize).attr(handleAttrs);
        this.handleBottomRight = paper.rect(xStart - this.handleSize, yStart - this.handleSize, this.handleSize, this.handleSize).attr(handleAttrs);

        if (isInteractive) {
            this.setDraggable(worldModel.getZoom());
        }
    }

    setDraggable(zoom: number): void {
        var ellipseItem = this;
        ellipseItem.ellipse.attr({cursor: "pointer"});
        var startTopLeftHandle = function () {
                if (!ellipseItem.worldModel.getDrawMode()) {
                    this.ox = this.attr("x");
                    this.oy = this.attr("y");
                    this.oppositeCornerX = ellipseItem.handleBottomRight.attr("x") + ellipseItem.handleSize;
                    this.oppositeCornerY = ellipseItem.handleBottomRight.attr("y") + ellipseItem.handleSize;
                }
                return this;
            },
            startTopRightHandle = function () {
                if (!ellipseItem.worldModel.getDrawMode()) {
                    this.ox = this.attr("x") + ellipseItem.handleSize;
                    this.oy = this.attr("y");
                    this.oppositeCornerX = ellipseItem.handleBottomLeft.attr("x");
                    this.oppositeCornerY = ellipseItem.handleBottomLeft.attr("y") + ellipseItem.handleSize;
                }
                return this;
            },
            startBottomLeftHandle = function () {
                if (!ellipseItem.worldModel.getDrawMode()) {
                    this.ox = this.attr("x");
                    this.oy = this.attr("y") + ellipseItem.handleSize;
                    this.oppositeCornerX = ellipseItem.handleTopRight.attr("x") + ellipseItem.handleSize;
                    this.oppositeCornerY = ellipseItem.handleTopRight.attr("y");
                }
                return this;
            },
            startBottomRightHandle = function () {
                if (!ellipseItem.worldModel.getDrawMode()) {
                    this.ox = this.attr("x") + ellipseItem.handleSize;
                    this.oy = this.attr("y") + ellipseItem.handleSize;
                    this.oppositeCornerX = ellipseItem.handleTopLeft.attr("x");
                    this.oppositeCornerY = ellipseItem.handleTopLeft.attr("y");
                }
                return this;
            },
            moveHandle = function (dx, dy) {
                if (!ellipseItem.worldModel.getDrawMode()) {
                    var newX = this.ox + dx / zoom;
                    var newY = this.oy + dy / zoom;
                    ellipseItem.updateCorner(this.oppositeCornerX, this.oppositeCornerY, newX, newY);
                }
                return this;
            },
            upHandle = function () {
                return this;
            };

        ellipseItem.handleTopLeft.drag(moveHandle, startTopLeftHandle, upHandle);
        ellipseItem.handleTopRight.drag(moveHandle, startTopRightHandle, upHandle);
        ellipseItem.handleBottomLeft.drag(moveHandle, startBottomLeftHandle, upHandle);
        ellipseItem.handleBottomRight.drag(moveHandle, startBottomRightHandle, upHandle);

        var startEllipse = function () {
                if (!ellipseItem.worldModel.getDrawMode()) {
                    this.cx = this.attr("cx");
                    this.cy = this.attr("cy");
                    this.handleTopLeftCoord = {
                        x: ellipseItem.handleTopLeft.attr("x"),
                        y: ellipseItem.handleTopLeft.attr("y")
                    };
                    this.handleTopRightCoord = {
                        x: ellipseItem.handleTopRight.attr("x"),
                        y: ellipseItem.handleTopRight.attr("y")
                    };
                    this.handleBottomLeftCoord = {
                        x: ellipseItem.handleBottomLeft.attr("x"),
                        y: ellipseItem.handleBottomLeft.attr("y")
                    };
                    this.handleBottomRightCoord = {
                        x: ellipseItem.handleBottomRight.attr("x"),
                        y: ellipseItem.handleBottomRight.attr("y")
                    };
                    ellipseItem.worldModel.setCurrentElement(ellipseItem);
                }
                return this;
            },
            moveEllipse = function (dx, dy) {
                dx /= zoom;
                dy /= zoom;
                if (!ellipseItem.worldModel.getDrawMode()) {
                    var newX = this.cx + dx;
                    var newY = this.cy + dy;
                    ellipseItem.ellipse.attr({cx: newX, cy: newY});

                    ellipseItem.handleTopLeft.attr({x : this.handleTopLeftCoord.x + dx, y: this.handleTopLeftCoord.y + dy});
                    ellipseItem.handleTopRight.attr({x : this.handleTopRightCoord.x + dx, y: this.handleTopRightCoord.y + dy});
                    ellipseItem.handleBottomLeft.attr({x : this.handleBottomLeftCoord.x + dx, y: this.handleBottomLeftCoord.y + dy});
                    ellipseItem.handleBottomRight.attr({x : this.handleBottomRightCoord.x + dx, y: this.handleBottomRightCoord.y + dy});
                }
                return this;
            },
            upEllipse = function () {
                return this;
            };

        ellipseItem.ellipse.drag(moveEllipse, startEllipse, upEllipse);
    }

    updateCorner(oppositeCornerX: number, oppositeCornerY: number, x: number, y: number): void {
        var newCx = (oppositeCornerX + x) / 2;
        var newCy = (oppositeCornerY + y) / 2;
        var newRx = Math.abs(x - oppositeCornerX) / 2;
        var newRy = Math.abs(y - oppositeCornerY) / 2;
        this.ellipse.attr({"cx": newCx , "cy": newCy});
        this.ellipse.attr({"rx": newRx, "ry": newRy});

        if (x - oppositeCornerX >= 0 && y - oppositeCornerY >= 0) {
            this.handleTopLeft.attr({x : oppositeCornerX, y: oppositeCornerY});
            this.handleTopRight.attr({x : x - this.handleSize, y: oppositeCornerY});
            this.handleBottomLeft.attr({x : oppositeCornerX, y: y - this.handleSize});
            this.handleBottomRight.attr({x : x - this.handleSize, y: y - this.handleSize});
        } else if (x - oppositeCornerX < 0 && y - oppositeCornerY >= 0) {
            this.handleTopLeft.attr({x : x, y: oppositeCornerY});
            this.handleTopRight.attr({x : oppositeCornerX - this.handleSize, y: oppositeCornerY});
            this.handleBottomLeft.attr({x : x, y: y - this.handleSize});
            this.handleBottomRight.attr({x : oppositeCornerX - this.handleSize, y: y - this.handleSize});
        } else if (x - oppositeCornerX >= 0 && y - oppositeCornerY < 0) {
            this.handleTopLeft.attr({x : oppositeCornerX, y: y});
            this.handleTopRight.attr({x : x - this.handleSize, y: y});
            this.handleBottomLeft.attr({x : oppositeCornerX, y: oppositeCornerY - this.handleSize});
            this.handleBottomRight.attr({x : x - this.handleSize, y: oppositeCornerY - this.handleSize});
        } else if (x - oppositeCornerX < 0 && y - oppositeCornerY < 0) {
            this.handleTopLeft.attr({x : x, y: y});
            this.handleTopRight.attr({x : oppositeCornerX - this.handleSize, y: y});
            this.handleBottomLeft.attr({x : x, y: oppositeCornerY - this.handleSize});
            this.handleBottomRight.attr({x : oppositeCornerX - this.handleSize, y: oppositeCornerY - this.handleSize});
        }

    }

    hideHandles(): void {
        this.handleTopLeft.hide();
        this.handleTopRight.hide();
        this.handleBottomLeft.hide();
        this.handleBottomRight.hide();
    }

    showHandles(): void {
        this.handleTopLeft.show();
        this.handleTopRight.show();
        this.handleBottomLeft.show();
        this.handleBottomRight.show();
    }

    remove(): void {
        this.handleTopLeft.remove();
        this.handleTopRight.remove();
        this.handleBottomLeft.remove();
        this.handleBottomRight.remove();
        this.ellipse.remove();
    }
    
}