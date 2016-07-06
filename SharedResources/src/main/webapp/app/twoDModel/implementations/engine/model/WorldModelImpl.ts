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

/// <reference path="../items/regions/RegionItem.ts" />
/// <reference path="../items/StartPositionItem.ts" />
/// <reference path="../items/LineItemImpl.ts" />
/// <reference path="../items/WallItemImpl.ts" />
/// <reference path="../items/PencilItemImpl.ts" />
/// <reference path="../items/EllipseItemImpl.ts" />
/// <reference path="../../../interfaces/engine/model/WorldModel.ts" />
/// <reference path="../../../interfaces/engine/items/AbstractItem.ts" />
/// <reference path="../../../interfaces/engine/items/ColorFieldItem.ts" />
/// <reference path="../../../interfaces/engine/items/WallItem.ts" />
/// <reference path="../../../../utils/ColorUtils.ts" />
/// <reference path="../../../../vendor.d.ts" />

class WorldModelImpl implements WorldModel {

    private drawMode: number = 0;
    private paper: RaphaelPaper;
    private currentElement: AbstractItem = null;
    private robotItemSet: RaphaelSet;
    private colorFields: ColorFieldItem[] = [];
    private wallItems: WallItem[] = [];
    private regions: RegionItem[] = [];
    private startPositionCross: StartPositionItem;
    private zoom: number;
    private width: number = 3000;
    private height: number = 3000;
    private isInteractive: boolean;
    private contextMenuId = "twoDModel_stage_context_menu";

    constructor(zoom: number, isInteractive: boolean) {
        this.zoom = (zoom) ? zoom : 1;
        this.isInteractive = isInteractive;
        this.paper = Raphael("twoDModel_stage", this.width, this.height);
        this.robotItemSet = this.paper.set();

        $(this.paper.canvas).attr("id", "twoDModel_paper");
        $(this.paper.canvas).css('overflow', 'auto');

        this.paper.setViewBox(0, 0, this.width, this.height, true);
        this.paper.canvas.setAttribute('preserveAspectRatio', 'none');
        $('#twoDModel_paper').attr('width', this.width * this.zoom).attr('height', this.height * this.zoom);

        var wall_pattern = '<pattern id="wall_pattern" patternUnits="userSpaceOnUse" width="85" height="80">\
                                        <image xlink:href="' + GeneralConstants.APP_ROOT_PATH +
            'images/2dmodel/2d_wall.png" width="85" height="80" />\
                                    </pattern>';
        $("body").append('<svg id="dummy" style="display:none"><defs>' + wall_pattern + '</defs></svg>');
        $("#twoDModel_paper defs").append($("#dummy pattern"));
        $("#dummy").remove();

        if (this.isInteractive) {
            this.initCustomContextMenu();
            this.initDeleteListener();
        }

        $(document).ready(() => {
            this.initMouseListeners();
        });
    }

    getZoom(): number {
        return this.zoom;
    }

    addRobotItemElement(element: RaphaelElement): void {
        this.robotItemSet.push(element);
    }

    insertBeforeRobots(element: RaphaelElement): void {
        element.insertBefore(this.robotItemSet);
    }

    insertAfterRobots(element: RaphaelElement): void {
        element.insertAfter(this.robotItemSet);
    }

    addWall(xStart: number, yStart: number, xEnd: number, yEnd: number): void {
        var exPositions = this.getExtendedPositions(xStart, yStart, xEnd, yEnd, WallItemImpl.getWidth());
        var wall = new WallItemImpl(this, exPositions.start.x, exPositions.start.y,
            exPositions.end.x, exPositions.end.y, this.isInteractive);
        wall.hideHandles();
        this.wallItems.push(wall);
    }

    addLine(xStart: number, yStart: number, xEnd: number, yEnd: number, width: number, rgbaColor: RGBAColor) {
        var exPositions = this.getExtendedPositions(xStart, yStart, xEnd, yEnd, width);
        var line = new LineItemImpl(this, exPositions.start.x, exPositions.start.y,
            exPositions.end.x, exPositions.end.y, width, rgbaColor, this.isInteractive);
        line.hideHandles();
        this.colorFields.push(line);
    }

    addCubicBezier(xStart: number, yStart: number, xEnd: number, yEnd: number,
                   cp1X: number, cp1Y: number, cp2X: number, cp2Y: number,
                   width: number, rgbaColor: RGBAColor) {
        var exStartPositions = this.getExtendedPositions(xStart, yStart, cp1X, cp1Y, width);
        var exEndPositions = this.getExtendedPositions(xEnd, yEnd, cp2X, cp2Y, width);
        var cubicBezier = new CubicBezierItemImpl(this, exStartPositions.start.x, exStartPositions.start.y,
            exEndPositions.start.x, exEndPositions.start.y, cp1X, cp1Y, cp2X, cp2Y,
            width, rgbaColor, this.isInteractive);
        cubicBezier.hideHandles();
        this.colorFields.push(cubicBezier);
    }

    private getExtendedPositions(xStart: number, yStart: number, xEnd: number,
                                 yEnd: number, width: number): {start: TwoDPosition, end: TwoDPosition} {
        var extension = width / 2;
        var cos = Math.abs(xEnd - xStart) / MathUtils.twoPointLenght(xStart, yStart, xEnd, yEnd);
        var extensionX = extension * cos;
        var sin = Math.abs(yEnd - yStart) / MathUtils.twoPointLenght(xStart, yStart, xEnd, yEnd);
        var extensionY = extension * sin;

        var exStartX: number;
        var exStartY: number;
        var exEndX: number;
        var exEndY: number;

        if (xStart < xEnd) {
            exStartX = xStart - extensionX;
            exEndX = xEnd + extensionX;
        } else {
            exStartX = xStart + extensionX;
            exEndX = xEnd - extensionX;
        }

        if (yStart < yEnd) {
            exStartY = yStart - extensionY;
            exEndY = yEnd + extensionY;
        } else {
            exStartY = yStart + extensionY;
            exEndY = yEnd - extensionY;
        }
        return { start: new TwoDPosition(exStartX, exStartY), end: new TwoDPosition(exEndX, exEndY) };
    }

    getMousePosition(e) {
        var offset = $("#twoDModel_stage").offset();
        var position = {
            x : (e.pageX - offset.left + $("#twoDModel_stage").scrollLeft()) / this.zoom,
            y : (e.pageY - offset.top + $("#twoDModel_stage").scrollTop()) / this.zoom
        }
        return position;
    }

    setDrawLineMode(): void {
        this.drawMode = 1;
    }

    setDrawWallMode(): void {
        this.drawMode = 2;
    }

    setDrawPencilMode(): void {
        this.drawMode = 3;
    }

    setDrawEllipseMode(): void {
        this.drawMode = 4;
    }

    getDrawMode(): number {
        return this.drawMode;
    }

    setNoneMode(): void {
        this.drawMode = 0;
    }

    getPaper(): RaphaelPaper {
        return this.paper;
    }

    setCurrentElement(element): void {
        if (this.currentElement) {
            this.currentElement.hideHandles();
        }
        this.currentElement = element;
        element.showHandles();
    }

    setStartPositionCross(x: number, y: number, direction: number, offsetX: number, offsetY: number) {
        this.startPositionCross = new StartPositionItem(this, x + offsetX, y + offsetY, direction);
    }

    clearPaper(): void {
        while (this.wallItems.length) {
            this.wallItems.pop().remove();
        }

        while (this.colorFields.length) {
            this.colorFields.pop().remove();
        }

        while (this.regions.length) {
            this.regions.pop().remove();
        }

        if (this.startPositionCross) {
            this.startPositionCross.remove();
        }
    }

    deserialize(xml, offsetX: number, offsetY: number): void {
        var regions = xml.getElementsByTagName("region");
        if (regions) {
            this.deserializeRegions(regions, offsetX, offsetY);
        }

        var walls = xml.getElementsByTagName("wall");
        if (walls) {
            this.deserializeWalls(walls, offsetX, offsetY);
        }

        var colorFields = xml.getElementsByTagName("colorFields")[0];
        if (colorFields) {
            this.deserializeColorFields(colorFields, offsetX, offsetY);
        }

        var startPosition = xml.getElementsByTagName("startPosition")[0];
        if (startPosition) {
            this.deserializeStartPosition(startPosition, offsetX, offsetY);
        }
    }

    private removeCurrentElement(): void {
        if (this.currentElement) {
            this.currentElement.remove();
        }
    }

    private deserializeRegions(regions, offsetX: number, offsetY: number): void {
        for (var i = 0; i < regions.length; i++) {
            var type = regions[i].getAttribute("type");

            switch (type) {
                case "rectangle":
                    if (regions[i].getAttribute("visible") == "true") {
                        var region = new RectangularRegion(this);
                        region.deserialize(regions[i], offsetX, offsetY);
                        this.regions.push(region);
                        break;
                    }
                case "ellipse":
                    if (regions[i].getAttribute("visible") == "true") {
                        var region = new EllipseRegion(this);
                        region.deserialize(regions[i], offsetX, offsetY);
                        this.regions.push(region);
                        break;
                    }
                default:
            }
        }
    }

    private deserializeWalls(walls, offsetX: number, offsetY: number): void {
        for (var i = 0; i < walls.length; i++) {
            var beginPos: TwoDPosition = this.getPosition(walls[i], 'begin');
            var endPos: TwoDPosition = this.getPosition(walls[i], 'end');
            this.addWall(beginPos.x + offsetX, beginPos.y + offsetY, endPos.x + offsetX, endPos.y + offsetY);
        }
    }

    private deserializeColorFields(colorFields, offsetX: number, offsetY: number): void {
        var lines = colorFields.getElementsByTagName("line");
        if (lines) {
            this.deserializeLines(lines, offsetX, offsetY);
        }

        var cubicBeziers = colorFields.getElementsByTagName("cubicBezier");
        if (cubicBeziers) {
            this.deserializeCubicBeziers(cubicBeziers, offsetX, offsetY);
        }
    }

    private deserializeLines(lines, offsetX: number, offsetY: number): void {
        for (var i = 0; i < lines.length; i++) {
            var beginPos: TwoDPosition = this.getPosition(lines[i], 'begin');
            var endPos: TwoDPosition = this.getPosition(lines[i], 'end');
            var width: number = parseInt(lines[i].getAttribute('stroke-width'));
            var color: string = lines[i].getAttribute('stroke');
            var rgbaColor: RGBAColor = ColorUtils.getRBGAColor(color);

            this.addLine(beginPos.x + offsetX, beginPos.y + offsetY,
                endPos.x + offsetX, endPos.y + offsetY, width, rgbaColor);
        }
    }

    private deserializeCubicBeziers(cubicBeziers, offsetX: number, offsetY: number): void {
        for (var i = 0; i < cubicBeziers.length; i++) {
            var beginPos: TwoDPosition = this.getPosition(cubicBeziers[i], 'begin');
            var endPos: TwoDPosition = this.getPosition(cubicBeziers[i], 'end');
            var cp1: TwoDPosition = this.getPosition(cubicBeziers[i], 'cp1');
            var cp2: TwoDPosition = this.getPosition(cubicBeziers[i], 'cp2');
            var width: number = parseInt(cubicBeziers[i].getAttribute('stroke-width'));
            var color: string = cubicBeziers[i].getAttribute('stroke');
            var rgbaColor: RGBAColor = ColorUtils.getRBGAColor(color);

            this.addCubicBezier(beginPos.x + offsetX, beginPos.y + offsetY,
                endPos.x + offsetX, endPos.y + offsetY, cp1.x + offsetX, cp1.y + offsetY,
                cp2.x + offsetX, cp2.y + offsetY , width, rgbaColor);
        }
    }

    private deserializeStartPosition(startPosition, offsetX: number, offsetY: number): void {
        var x = parseFloat(startPosition.getAttribute('x'));
        var y = parseFloat(startPosition.getAttribute('y'));
        var direction = parseFloat(startPosition.getAttribute('direction'));
        this.setStartPositionCross(x, y, direction, offsetX, offsetY);
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

    private initMouseListeners(): void {
        var worldModel = this;
        var shape;
        var isDrawing: boolean = false;
        var startDrawPoint;

        $("#twoDModel_stage").mousedown(function(e) {
            switch (worldModel.drawMode) {
                case 0:
                    if (e.target.nodeName === "svg") {
                        if (worldModel.currentElement) {
                            worldModel.currentElement.hideHandles();
                            worldModel.currentElement = null;
                        }
                    }
                    break;
                case 1:
                    var position = worldModel.getMousePosition(e);
                    var x = position.x;
                    var y = position.y;
                    var width = $("#pen_width_spinner").val();
                    var color = $("#pen_color_dropdown").val();
                    shape = new LineItemImpl(worldModel, x, y, x, y, width, new RGBAColor(1, color),
                        worldModel.isInteractive);
                    worldModel.colorFields.push(shape);
                    worldModel.setCurrentElement(shape);
                    isDrawing = true;
                    break
                case 2:
                    var position = worldModel.getMousePosition(e);
                    var x = position.x;
                    var y = position.y;
                    shape = new WallItemImpl(worldModel, x, y, x, y, worldModel.isInteractive);
                    worldModel.wallItems.push(shape);
                    worldModel.setCurrentElement(shape);
                    isDrawing = true;
                    break
                case 3:
                    var position = worldModel.getMousePosition(e);
                    var x = position.x;
                    var y = position.y;
                    var width = $("#pen_width_spinner").val();
                    var color = $("#pen_color_dropdown").val();
                    shape = new PencilItemImpl(worldModel, x, y, width, color, worldModel.isInteractive);
                    worldModel.colorFields.push(shape);
                    worldModel.setCurrentElement(shape);
                    isDrawing = true;
                    break
                case 4:
                    var position = worldModel.getMousePosition(e);
                    var x = position.x;
                    var y = position.y;
                    var width = $("#pen_width_spinner").val();
                    var color = $("#pen_color_dropdown").val();
                    startDrawPoint = {
                        "x": x,
                        "y": y
                    }
                    shape = new EllipseItemImpl(worldModel, x, y, width, color, worldModel.isInteractive);
                    worldModel.colorFields.push(shape);
                    worldModel.setCurrentElement(shape);
                    isDrawing = true;
                    break
                default:
            }
        });

        $("#twoDModel_stage").mousemove((e) => {
            if (worldModel.isInteractive && isDrawing) {
                switch (worldModel.drawMode) {
                    case 1:
                    case 2:
                        var position = worldModel.getMousePosition(e);
                        var x = position.x;
                        var y = position.y;
                        shape.updateEnd(x, y);
                        break
                    case 3:
                        var position = worldModel.getMousePosition(e);
                        var x = position.x;
                        var y = position.y;
                        shape.updatePath(x, y);
                        break
                    case 4:
                        var position = worldModel.getMousePosition(e);
                        var x = position.x;
                        var y = position.y;
                        shape.updateCorner(startDrawPoint.x, startDrawPoint.y, x, y);
                        break
                    default:
                }
            }
        });

        $("#twoDModel_stage").mouseup(function(event) {
            isDrawing = false;
            if (worldModel.isInteractive) {
                if (event.target.nodeName !== "svg" && !(worldModel.currentElement instanceof RobotItemImpl
                    || worldModel.currentElement instanceof SensorItem)) {
                    if (worldModel.drawMode === 0) {
                        if (event.button === 2) {
                            $("#" + worldModel.contextMenuId).finish().toggle(100).
                            css({
                                left: event.pageX - $(document).scrollLeft() + "px",
                                top: event.pageY - $(document).scrollTop() + "px"
                            });
                        }
                    }
                }
            }
        });
    }

    private initCustomContextMenu(): void {
        var controller = this;
        $("#twoDModelContent").bind("contextmenu", function (event) {
            event.preventDefault();
        });

        $("#" + controller.contextMenuId + " li").click(function(){
            switch($(this).attr("data-action")) {
                case "delete":
                    controller.removeCurrentElement();
                    break;
            }

            $("#" + controller.contextMenuId).hide(100);
        });
    }

    private initDeleteListener(): void {
        var deleteKey: number = 46;
        $('html').keyup((event) => {
            if(event.keyCode == deleteKey) {
                if($("#twoDModel_stage").is(":visible") && !(document.activeElement.tagName === "INPUT")) {
                    if (!(this.currentElement instanceof RobotItemImpl || this.currentElement instanceof SensorItem)) {
                        this.removeCurrentElement();
                    }
                }
            }
        });
    }

}