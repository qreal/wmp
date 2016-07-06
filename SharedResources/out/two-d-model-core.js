var LedWidget = (function () {
    function LedWidget() {
        this.color = "red";
    }
    LedWidget.prototype.setColor = function (color) {
        this.color = color;
        $("#led").css("background", color);
    };
    LedWidget.prototype.getColor = function () {
        return this.color;
    };
    LedWidget.prototype.reset = function () {
        this.color = "red";
        $("#led").css("background", "red");
    };
    LedWidget.prototype.show = function () {
        $("#led").show();
    };
    LedWidget.prototype.hide = function () {
        $("#led").hide();
    };
    return LedWidget;
})();
var MathUtils = (function () {
    function MathUtils() {
    }
    MathUtils.toDeg = function (radians) {
        return radians * (180 / Math.PI);
    };
    MathUtils.toRad = function (degrees) {
        return degrees * (Math.PI / 180);
    };
    MathUtils.twoPointLenght = function (x1, y1, x2, y2) {
        return Math.sqrt(this.sqr(x1 - x2) + this.sqr(y1 - y2));
    };
    MathUtils.sqr = function (x) {
        return x * x;
    };
    MathUtils.min = function (a, b) {
        return (a < b) ? a : b;
    };
    MathUtils.toRadians = function (angle) {
        return angle * Math.PI / 180.0;
    };
    MathUtils.toDegrees = function (angle) {
        return angle * 180.0 / Math.PI;
    };
    MathUtils.rotateVector = function (x, y, angle) {
        angle = MathUtils.toRadians(angle);
        var newX = x * Math.cos(angle) - y * Math.sin(angle);
        var newY = x * Math.sin(angle) + y * Math.cos(angle);
        return { x: newX, y: newY };
    };
    return MathUtils;
})();
var DisplayWidget = (function () {
    function DisplayWidget() {
        this.width = 218;
        this.height = 274;
        var canvas = document.getElementById("display");
        this.context = canvas.getContext("2d");
        this.smileImg = new Image();
        this.smileImg.src = GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/smile.png";
        this.sadSmileImg = new Image();
        this.sadSmileImg.src = GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/sadSmile.png";
        this.ledWidget = new LedWidget();
        this.isSmiles = false;
        this.isSadSmiles = false;
        this.background = "#a0a0a4";
    }
    DisplayWidget.prototype.setBackground = function (color) {
        this.background = color;
        $("#display").css('background', color);
    };
    DisplayWidget.prototype.drawSmile = function () {
        this.isSmiles = true;
        this.context.drawImage(this.smileImg, 0, 0, this.width, this.height);
    };
    DisplayWidget.prototype.drawSadSmile = function () {
        this.isSmiles = true;
        this.context.drawImage(this.sadSmileImg, 0, 0, this.width, this.height);
    };
    DisplayWidget.prototype.drawEllipse = function (x, y, a, b, color, thickness) {
        this.context.save();
        this.context.beginPath();
        this.context.translate(x, y);
        this.context.scale(a / b, 1);
        this.context.arc(0, 0, b, 0, 2 * Math.PI);
        this.context.restore();
        this.context.lineWidth = thickness;
        this.context.strokeStyle = color;
        this.context.stroke();
    };
    DisplayWidget.prototype.drawArc = function (x, y, a, b, startAngle, sweepLength, color, thickness) {
        var startAngleInRad = MathUtils.toRad(startAngle);
        var counterStartAngleInRad = 2 * Math.PI - startAngleInRad;
        var counterEndAngleInRad = 2 * Math.PI - MathUtils.toRad(startAngle + sweepLength);
        this.context.save();
        this.context.beginPath();
        this.context.translate(x, y);
        this.context.scale(a / b, 1);
        this.context.arc(0, 0, b, counterStartAngleInRad, counterEndAngleInRad, true);
        this.context.restore();
        this.context.lineWidth = thickness;
        this.context.strokeStyle = color;
        this.context.stroke();
    };
    DisplayWidget.prototype.drawRectangle = function (x, y, a, b, color, thickness) {
        this.context.beginPath();
        this.context.rect(x, y, a, b);
        this.context.lineWidth = thickness;
        this.context.strokeStyle = color;
        this.context.stroke();
    };
    DisplayWidget.prototype.drawLine = function (x1, y1, x2, y2, color, thickness) {
        this.context.beginPath();
        this.context.moveTo(x1, y1);
        this.context.lineTo(x2, y2);
        this.context.lineWidth = thickness;
        this.context.strokeStyle = color;
        this.context.stroke();
    };
    DisplayWidget.prototype.drawPoint = function (x, y, color, thickness) {
        this.context.fillStyle = color;
        this.context.fillRect(x - thickness / 2, y - thickness / 2, thickness, thickness);
    };
    DisplayWidget.prototype.drawText = function (x, y, text, color) {
        this.context.fillStyle = color;
        this.context.font = "12px Arial";
        this.context.fillText(text, x, y);
    };
    DisplayWidget.prototype.clearSmile = function () {
        this.isSmiles = false;
        this.redraw();
    };
    DisplayWidget.prototype.clearSadSmile = function () {
        this.isSadSmiles = false;
        this.redraw();
    };
    DisplayWidget.prototype.setLedColor = function (color) {
        this.ledWidget.setColor(color);
    };
    DisplayWidget.prototype.reset = function () {
        this.setBackground("#a0a0a4");
        this.isSmiles = false;
        this.isSadSmiles = false;
        this.clearScreen();
        this.ledWidget.reset();
    };
    DisplayWidget.prototype.redraw = function () {
        this.clearScreen();
        if (this.isSmiles) {
            this.drawSmile();
        }
        if (this.isSadSmiles) {
            this.drawSadSmile();
        }
    };
    DisplayWidget.prototype.clearScreen = function () {
        this.context.clearRect(0, 0, this.width, this.height);
    };
    DisplayWidget.prototype.show = function () {
        $("#menu_button").hide();
        $("#close_display").show();
        $("#controller").show();
        $("#display").show();
        $(".port_name").show();
        this.ledWidget.show();
    };
    DisplayWidget.prototype.hide = function () {
        $("#close_display").hide();
        $("#display").hide();
        $("#controller").hide();
        $(".port_name").hide();
        this.ledWidget.hide();
        $("#menu_button").show();
    };
    DisplayWidget.prototype.displayToFront = function () {
        $("#display").css("z-index", 100);
    };
    DisplayWidget.prototype.displayToBack = function () {
        $("#display").css("z-index", -1);
    };
    return DisplayWidget;
})();
var TwoDPosition = (function () {
    function TwoDPosition(x, y) {
        this.x = (x) ? x : 0;
        this.y = (y) ? y : 0;
    }
    return TwoDPosition;
})();
var Runner = (function () {
    function Runner() {
        this.boost = 2;
    }
    Runner.prototype.run = function (robotItem, displayWidget, result) {
        this.parseReport(result.report);
        displayWidget.displayToFront();
        var trajectory = JSON.parse(result.trace);
        var runner = this;
        var counter = 0;
        runner.timeoutId = setTimeout(function nextPoint() {
            var point = trajectory[counter];
            runner.doPointActions(robotItem, displayWidget, point);
            if (counter < trajectory.length - 1) {
                counter++;
                var delay = (trajectory[counter].timestamp - point.timestamp) / runner.boost;
                if (!delay) {
                    nextPoint();
                }
                else {
                    runner.timeoutId = setTimeout(nextPoint, delay);
                }
            }
            else {
                displayWidget.displayToBack();
                displayWidget.reset();
            }
        }, 0);
    };
    Runner.prototype.stop = function (robotItem, displayWidget) {
        displayWidget.displayToBack();
        if (this.timeoutId) {
            clearTimeout(this.timeoutId);
            this.timeoutId = undefined;
        }
        robotItem.clearCurrentPosition();
        displayWidget.reset();
    };
    Runner.prototype.parseReport = function (report) {
        var messageText = "";
        var level = report.messages[0].level;
        report.messages.forEach(function (message) {
            messageText += message.message + " ";
        });
        if (level === "info") {
            $("#infoAlert").removeClass("alert-danger");
            $("#infoAlert").addClass("alert-success");
        }
        else {
            if (level === "error") {
                $("#infoAlert").removeClass("alert-success");
                $("#infoAlert").addClass("alert-danger");
            }
        }
        $("#infoAlert").contents().last()[0].textContent = messageText;
        $("#infoAlert").show();
    };
    Runner.prototype.doPointActions = function (robotItem, displayWidget, point) {
        if (point.device) {
            switch (point.device) {
                case "display":
                    this.doDisplayActions(displayWidget, point);
                    break;
                case "led":
                    this.doLedActions(displayWidget, point);
                    break;
                case "marker":
                    this.doMarkerActions(robotItem, point);
                    break;
                case "shell":
                    this.doShellActions(point);
                default:
            }
        }
        else {
            robotItem.moveToPoint(point.x, point.y, point.rotation);
        }
    };
    Runner.prototype.doDisplayActions = function (displayWidget, point) {
        switch (point.property) {
            case "smiles":
                if (point.value) {
                    displayWidget.drawSmile();
                }
                else {
                    displayWidget.clearSmile();
                }
                break;
            case "sadSmiles":
                if (point.value) {
                    displayWidget.drawSadSmile();
                }
                else {
                    displayWidget.clearSadSmile();
                }
                break;
            case "background":
                displayWidget.setBackground(point.value);
                break;
            case "objects":
                this.drawObjects(displayWidget, point.value);
                break;
        }
    };
    Runner.prototype.doLedActions = function (displayWidget, point) {
        if (point.property === "color") {
            displayWidget.setLedColor(point.value);
        }
    };
    Runner.prototype.doMarkerActions = function (robotItem, point) {
        switch (point.property) {
            case "isDown":
                robotItem.setMarkerDown(point.value);
                break;
            case "color":
                robotItem.setMarkerColor(point.value);
                break;
            default:
        }
    };
    Runner.prototype.doShellActions = function (point) {
        switch (point.property) {
            case "lastPhrase":
                this.say(point.value);
                break;
        }
    };
    Runner.prototype.say = function (value) {
        var delay = 1000;
        var maxLegnth = 90;
        if (value.length > maxLegnth) {
            value = value.substr(0, maxLegnth) + "...";
        }
        $("#sayAlert").contents().last()[0].textContent = value;
        $("#sayAlert").show();
        $("#sayAlert").delay(delay).fadeOut(delay, function () {
            $("#sayAlert").hide();
        });
    };
    Runner.prototype.drawObjects = function (displayWidget, objects) {
        displayWidget.redraw();
        for (var i = 0; i < objects.length; i++) {
            var object = objects[i];
            switch (object.type) {
                case "ellipse":
                    displayWidget.drawEllipse(object.x, object.y, object.a, object.b, object.color, object.thickness);
                    break;
                case "arc":
                    displayWidget.drawArc(object.x, object.y, object.a, object.b, object.startAngle, object.spanAngle, object.color, object.thickness);
                    break;
                case "rectangle":
                    displayWidget.drawRectangle(object.x, object.y, object.width, object.height, object.color, object.thickness);
                    break;
                case "line":
                    displayWidget.drawLine(object.x1, object.y1, object.x2, object.y2, object.color, object.thickness);
                    break;
                case "point":
                    displayWidget.drawPoint(object.x, object.y, object.color, object.thickness);
                    break;
                case "text":
                    displayWidget.drawText(object.x, object.y, object.text, object.color);
                    break;
            }
        }
    };
    return Runner;
})();
var StageScroller = (function () {
    function StageScroller(zoom) {
        this.stage = $("#twoDModel_stage");
        this.zoom = zoom;
    }
    StageScroller.prototype.scrollToPoint = function (x, y) {
        var width = this.stage.width();
        var height = this.stage.height();
        var offsetX = x * this.zoom - width / 2;
        var offsetY = y * this.zoom - height / 2;
        this.stage.scrollLeft(offsetX);
        this.stage.scrollTop(offsetY);
    };
    return StageScroller;
})();
var RGBAColor = (function () {
    function RGBAColor(alpha, rgb) {
        this.alpha = alpha;
        this.rgb = rgb;
    }
    return RGBAColor;
})();
var ColorUtils = (function () {
    function ColorUtils() {
    }
    ColorUtils.getRBGAColor = function (color) {
        if (color.length == 9) {
            var rgb = "#" + color.substr(3, color.length - 3);
            var alpha = this.alphaHexTo0To1(color.substr(1, 2));
            return new RGBAColor(alpha, rgb);
        }
        else {
            return new RGBAColor(1, color);
        }
    };
    ColorUtils.alphaHexTo0To1 = function (alpha) {
        var decAlpha = parseInt(alpha, 16);
        return decAlpha / 255;
    };
    return ColorUtils;
})();
var RegionItem = (function () {
    function RegionItem(worldModel) {
        this.defaultColor = "#87CEFA";
        this.defaultWidth = 200;
        this.defaultHeight = 200;
        this.worldModel = worldModel;
    }
    RegionItem.prototype.setWidht = function (width) {
        this.shape.attr({ width: width });
    };
    RegionItem.prototype.setHeight = function (height) {
        this.shape.attr({ height: height });
    };
    RegionItem.prototype.setColor = function (color) {
        this.shape.attr({ stroke: color });
    };
    RegionItem.prototype.setOpacity = function (alpha) {
        this.shape.attr({ "stroke-opacity": alpha });
    };
    RegionItem.prototype.setPosition = function (position) {
        this.shape.attr({ x: position.x, y: position.y });
    };
    RegionItem.prototype.setVisible = function (visible) {
        visible ? this.shape.show() : this.shape.hide();
    };
    RegionItem.prototype.isVisible = function () {
        return $(this.shape.node).css("display") !== "none";
    };
    RegionItem.prototype.getPosition = function () {
        return new TwoDPosition(this.shape.attr("x"), this.shape.attr("y"));
    };
    RegionItem.prototype.getWith = function () {
        return this.shape.attr("width");
    };
    RegionItem.prototype.getHeight = function () {
        return this.shape.attr("height");
    };
    RegionItem.prototype.remove = function () {
        if (this.text) {
            this.text.remove();
        }
        this.shape.remove();
    };
    RegionItem.prototype.deserializePoint = function (element, xAttr, yAttr, offsetX, offsetY) {
        var x = parseFloat(xAttr);
        var y = parseFloat(yAttr);
        if (x !== undefined && y !== undefined) {
            return new TwoDPosition(x + offsetX, y + offsetY);
        }
        return new TwoDPosition(0, 0);
    };
    RegionItem.prototype.deserialize = function (element, offsetX, offsetY) {
        var _this = this;
        var color = element.getAttribute("color");
        if (color) {
            var rgbaColor = ColorUtils.getRBGAColor(color);
            this.setColor(rgbaColor.rgb);
            this.setOpacity(rgbaColor.alpha);
        }
        var visible = element.getAttribute("visible");
        if (visible) {
            this.setVisible(visible === "true");
        }
        var widthAttr = element.getAttribute("width");
        var heightAttr = element.getAttribute("height");
        if (widthAttr !== undefined && heightAttr !== undefined) {
            var width = parseFloat(widthAttr);
            var height = parseFloat(heightAttr);
            if (width !== undefined && height !== undefined) {
                this.setWidht(width);
                this.setHeight(height);
            }
        }
        var xAttr = element.getAttribute("x");
        var yAttr = element.getAttribute("y");
        if (xAttr !== undefined && yAttr !== undefined) {
            this.setPosition(this.deserializePoint(element, xAttr, yAttr, offsetX, offsetY));
        }
        var textX = element.getAttribute("textX");
        var textY = element.getAttribute("textY");
        if (textX !== undefined && textY !== undefined) {
        }
        var text = element.getAttribute("text");
        if (text) {
            var textPos = this.getPosition();
            setTimeout(function () {
                _this.text = _this.worldModel.getPaper().text(textPos.x + 5, textPos.y + 10, text).
                    attr({ "text-anchor": "start", fill: color, "font-size": 14 }).toBack();
            });
        }
    };
    return RegionItem;
})();
var StartPositionItem = (function () {
    function StartPositionItem(worldModel, x, y, direction) {
        this.width = 15;
        this.height = 15;
        this.image = worldModel.getPaper().image(GeneralConstants.APP_ROOT_PATH + "images/2dmodel/cross.png", x - this.width / 2, y - this.height / 2, this.width, this.height);
        this.image.transform("R" + direction);
        this.image.toBack();
    }
    StartPositionItem.prototype.remove = function () {
        this.image.remove();
    };
    return StartPositionItem;
})();
var LineItemImpl = (function () {
    function LineItemImpl(worldModel, xStart, yStart, xEnd, yEnd, width, rgbaColor, isInteractive) {
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
        var handleRadius = 10;
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
    LineItemImpl.prototype.setDraggable = function (zoom) {
        var line = this;
        line.path.attr({ cursor: "pointer" });
        var startHandle = function () {
            if (!line.worldModel.getDrawMode()) {
                this.cx = this.attr("cx");
                this.cy = this.attr("cy");
            }
            return this;
        }, moveHandleStart = function (dx, dy) {
            if (!line.worldModel.getDrawMode()) {
                var newX = this.cx + dx / zoom;
                var newY = this.cy + dy / zoom;
                line.updateStart(newX, newY);
            }
            return this;
        }, moveHandleEnd = function (dx, dy) {
            if (!line.worldModel.getDrawMode()) {
                var newX = this.cx + dx / zoom;
                var newY = this.cy + dy / zoom;
                line.updateEnd(newX, newY);
            }
            return this;
        }, upHandle = function () {
            return this;
        };
        line.handleStart.drag(moveHandleStart, startHandle, upHandle);
        line.handleEnd.drag(moveHandleEnd, startHandle, upHandle);
        var startPath = function () {
            if (!line.worldModel.getDrawMode()) {
                this.startX = line.pathArray[0][1];
                this.startY = line.pathArray[0][2];
                this.endX = line.pathArray[1][1];
                this.endY = line.pathArray[1][2];
                this.ox = this.attr("x");
                this.oy = this.attr("y");
                line.worldModel.setCurrentElement(line);
            }
            return this;
        }, movePath = function (dx, dy) {
            dx /= zoom;
            dy /= zoom;
            if (!line.worldModel.getDrawMode()) {
                var trans_x = dx - this.ox;
                var trans_y = dy - this.oy;
                line.pathArray[0][1] = this.startX + dx;
                line.pathArray[0][2] = this.startY + dy;
                line.pathArray[1][1] = this.endX + dx;
                line.pathArray[1][2] = this.endY + dy;
                line.path.attr({ path: line.pathArray });
                this.ox = dx;
                this.oy = dy;
                var hStartX = line.handleStart.attr("cx") + trans_x;
                var hStartY = line.handleStart.attr("cy") + trans_y;
                var hEndX = line.handleEnd.attr("cx") + trans_x;
                var hEndY = line.handleEnd.attr("cy") + trans_y;
                line.handleStart.attr({ cx: hStartX, cy: hStartY });
                line.handleEnd.attr({ cx: hEndX, cy: hEndY });
            }
            return this;
        }, upPath = function () {
            return this;
        };
        line.path.drag(movePath, startPath, upPath);
    };
    LineItemImpl.prototype.getPath = function () {
        return this.path;
    };
    LineItemImpl.prototype.updateStart = function (x, y) {
        this.pathArray[0][1] = x;
        this.pathArray[0][2] = y;
        this.path.attr({ path: this.pathArray });
        this.handleStart.attr({ cx: x, cy: y });
    };
    LineItemImpl.prototype.updateEnd = function (x, y) {
        this.pathArray[1][1] = x;
        this.pathArray[1][2] = y;
        this.path.attr({ path: this.pathArray });
        this.handleEnd.attr({ cx: x, cy: y });
    };
    LineItemImpl.prototype.hideHandles = function () {
        this.handleStart.hide();
        this.handleEnd.hide();
    };
    LineItemImpl.prototype.showHandles = function () {
        this.handleStart.show();
        this.handleEnd.show();
    };
    LineItemImpl.prototype.remove = function () {
        this.handleStart.remove();
        this.handleEnd.remove();
        this.path.remove();
    };
    return LineItemImpl;
})();
var WallItemImpl = (function () {
    function WallItemImpl(worldModel, xStart, yStart, xEnd, yEnd, isInteractive) {
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
        var handleRadius = 10;
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
    WallItemImpl.getWidth = function () {
        return WallItemImpl.width;
    };
    WallItemImpl.prototype.setDraggable = function (zoom) {
        var wall = this;
        wall.path.attr({ cursor: "pointer" });
        var start = function () {
            if (!wall.worldModel.getDrawMode()) {
                this.cx = this.attr("cx");
                this.cy = this.attr("cy");
            }
            return this;
        }, moveStart = function (dx, dy) {
            if (!wall.worldModel.getDrawMode()) {
                var newX = this.cx + dx / zoom;
                var newY = this.cy + dy / zoom;
                wall.updateStart(newX, newY);
            }
            return this;
        }, moveEnd = function (dx, dy) {
            if (!wall.worldModel.getDrawMode()) {
                var newX = this.cx + dx / zoom;
                var newY = this.cy + dy / zoom;
                wall.updateEnd(newX, newY);
            }
            return this;
        }, up = function () {
            return this;
        };
        wall.handleStart.drag(moveStart, start, up);
        wall.handleEnd.drag(moveEnd, start, up);
        var startPath = function () {
            if (!wall.worldModel.getDrawMode()) {
                this.startX = wall.pathArray[0][1];
                this.startY = wall.pathArray[0][2];
                this.endX = wall.pathArray[1][1];
                this.endY = wall.pathArray[1][2];
                this.ox = this.attr("x");
                this.oy = this.attr("y");
                wall.worldModel.setCurrentElement(wall);
            }
            return this;
        }, movePath = function (dx, dy) {
            dx /= zoom;
            dy /= zoom;
            if (!wall.worldModel.getDrawMode()) {
                var trans_x = dx - this.ox;
                var trans_y = dy - this.oy;
                wall.pathArray[0][1] = this.startX + dx;
                wall.pathArray[0][2] = this.startY + dy;
                wall.pathArray[1][1] = this.endX + dx;
                wall.pathArray[1][2] = this.endY + dy;
                wall.path.attr({ path: wall.pathArray });
                this.ox = dx;
                this.oy = dy;
                var hStartX = wall.handleStart.attr("cx") + trans_x;
                var hStartY = wall.handleStart.attr("cy") + trans_y;
                var hEndX = wall.handleEnd.attr("cx") + trans_x;
                var hEndY = wall.handleEnd.attr("cy") + trans_y;
                wall.handleStart.attr({ cx: hStartX, cy: hStartY });
                wall.handleEnd.attr({ cx: hEndX, cy: hEndY });
            }
            return this;
        }, upPath = function () {
            return this;
        };
        wall.path.drag(movePath, startPath, upPath);
    };
    WallItemImpl.prototype.getPath = function () {
        return this.path;
    };
    WallItemImpl.prototype.updateStart = function (x, y) {
        this.pathArray[0][1] = x;
        this.pathArray[0][2] = y;
        this.path.attr({ path: this.pathArray });
        this.handleStart.attr({ cx: x, cy: y });
    };
    WallItemImpl.prototype.updateEnd = function (x, y) {
        this.pathArray[1][1] = x;
        this.pathArray[1][2] = y;
        this.path.attr({ path: this.pathArray });
        this.handleEnd.attr({ cx: x, cy: y });
    };
    WallItemImpl.prototype.hideHandles = function () {
        this.handleStart.hide();
        this.handleEnd.hide();
    };
    WallItemImpl.prototype.showHandles = function () {
        this.handleStart.show();
        this.handleEnd.show();
    };
    WallItemImpl.prototype.remove = function () {
        this.handleStart.remove();
        this.handleEnd.remove();
        this.path.remove();
    };
    WallItemImpl.width = 15;
    return WallItemImpl;
})();
var PencilItemImpl = (function () {
    function PencilItemImpl(worldModel, xStart, yStart, width, color, isInteractive) {
        this.pathArray = new Array();
        var paper = worldModel.getPaper();
        this.worldModel = worldModel;
        this.pathArray[0] = ["M", xStart, yStart];
        this.path = paper.path(this.pathArray);
        this.path.attr({
            "stroke": color,
            "stroke-width": width
        });
        worldModel.insertBeforeRobots(this.path);
        if (isInteractive) {
            this.setDraggable(worldModel.getZoom());
        }
    }
    PencilItemImpl.prototype.setDraggable = function (zoom) {
        var pencilItem = this;
        pencilItem.path.attr({ cursor: "pointer" });
        var startPath = function () {
            if (!pencilItem.worldModel.getDrawMode()) {
                this.transformation = this.transform();
                pencilItem.worldModel.setCurrentElement(pencilItem);
            }
            return this;
        }, movePath = function (dx, dy) {
            dx /= zoom;
            dy /= zoom;
            if (!pencilItem.worldModel.getDrawMode()) {
                this.transform(this.transformation + "T" + dx + "," + dy);
            }
            return this;
        }, upPath = function () {
            return this;
        };
        pencilItem.path.drag(movePath, startPath, upPath);
    };
    PencilItemImpl.prototype.updatePath = function (x, y) {
        this.pathArray[this.pathArray.length] = ["L", x, y];
        this.path.attr({ path: this.pathArray });
    };
    PencilItemImpl.prototype.getPath = function () {
        return this.path;
    };
    PencilItemImpl.prototype.hideHandles = function () {
    };
    PencilItemImpl.prototype.showHandles = function () {
    };
    PencilItemImpl.prototype.remove = function () {
        this.path.remove();
    };
    return PencilItemImpl;
})();
var EllipseItemImpl = (function () {
    function EllipseItemImpl(worldModel, xStart, yStart, width, color, isInteractive) {
        this.handleSize = 10;
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
    EllipseItemImpl.prototype.setDraggable = function (zoom) {
        var ellipseItem = this;
        ellipseItem.ellipse.attr({ cursor: "pointer" });
        var startTopLeftHandle = function () {
            if (!ellipseItem.worldModel.getDrawMode()) {
                this.ox = this.attr("x");
                this.oy = this.attr("y");
                this.oppositeCornerX = ellipseItem.handleBottomRight.attr("x") + ellipseItem.handleSize;
                this.oppositeCornerY = ellipseItem.handleBottomRight.attr("y") + ellipseItem.handleSize;
            }
            return this;
        }, startTopRightHandle = function () {
            if (!ellipseItem.worldModel.getDrawMode()) {
                this.ox = this.attr("x") + ellipseItem.handleSize;
                this.oy = this.attr("y");
                this.oppositeCornerX = ellipseItem.handleBottomLeft.attr("x");
                this.oppositeCornerY = ellipseItem.handleBottomLeft.attr("y") + ellipseItem.handleSize;
            }
            return this;
        }, startBottomLeftHandle = function () {
            if (!ellipseItem.worldModel.getDrawMode()) {
                this.ox = this.attr("x");
                this.oy = this.attr("y") + ellipseItem.handleSize;
                this.oppositeCornerX = ellipseItem.handleTopRight.attr("x") + ellipseItem.handleSize;
                this.oppositeCornerY = ellipseItem.handleTopRight.attr("y");
            }
            return this;
        }, startBottomRightHandle = function () {
            if (!ellipseItem.worldModel.getDrawMode()) {
                this.ox = this.attr("x") + ellipseItem.handleSize;
                this.oy = this.attr("y") + ellipseItem.handleSize;
                this.oppositeCornerX = ellipseItem.handleTopLeft.attr("x");
                this.oppositeCornerY = ellipseItem.handleTopLeft.attr("y");
            }
            return this;
        }, moveHandle = function (dx, dy) {
            if (!ellipseItem.worldModel.getDrawMode()) {
                var newX = this.ox + dx / zoom;
                var newY = this.oy + dy / zoom;
                ellipseItem.updateCorner(this.oppositeCornerX, this.oppositeCornerY, newX, newY);
            }
            return this;
        }, upHandle = function () {
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
        }, moveEllipse = function (dx, dy) {
            dx /= zoom;
            dy /= zoom;
            if (!ellipseItem.worldModel.getDrawMode()) {
                var newX = this.cx + dx;
                var newY = this.cy + dy;
                ellipseItem.ellipse.attr({ cx: newX, cy: newY });
                ellipseItem.handleTopLeft.attr({ x: this.handleTopLeftCoord.x + dx, y: this.handleTopLeftCoord.y + dy });
                ellipseItem.handleTopRight.attr({ x: this.handleTopRightCoord.x + dx, y: this.handleTopRightCoord.y + dy });
                ellipseItem.handleBottomLeft.attr({ x: this.handleBottomLeftCoord.x + dx, y: this.handleBottomLeftCoord.y + dy });
                ellipseItem.handleBottomRight.attr({ x: this.handleBottomRightCoord.x + dx, y: this.handleBottomRightCoord.y + dy });
            }
            return this;
        }, upEllipse = function () {
            return this;
        };
        ellipseItem.ellipse.drag(moveEllipse, startEllipse, upEllipse);
    };
    EllipseItemImpl.prototype.updateCorner = function (oppositeCornerX, oppositeCornerY, x, y) {
        var newCx = (oppositeCornerX + x) / 2;
        var newCy = (oppositeCornerY + y) / 2;
        var newRx = Math.abs(x - oppositeCornerX) / 2;
        var newRy = Math.abs(y - oppositeCornerY) / 2;
        this.ellipse.attr({ "cx": newCx, "cy": newCy });
        this.ellipse.attr({ "rx": newRx, "ry": newRy });
        if (x - oppositeCornerX >= 0 && y - oppositeCornerY >= 0) {
            this.handleTopLeft.attr({ x: oppositeCornerX, y: oppositeCornerY });
            this.handleTopRight.attr({ x: x - this.handleSize, y: oppositeCornerY });
            this.handleBottomLeft.attr({ x: oppositeCornerX, y: y - this.handleSize });
            this.handleBottomRight.attr({ x: x - this.handleSize, y: y - this.handleSize });
        }
        else if (x - oppositeCornerX < 0 && y - oppositeCornerY >= 0) {
            this.handleTopLeft.attr({ x: x, y: oppositeCornerY });
            this.handleTopRight.attr({ x: oppositeCornerX - this.handleSize, y: oppositeCornerY });
            this.handleBottomLeft.attr({ x: x, y: y - this.handleSize });
            this.handleBottomRight.attr({ x: oppositeCornerX - this.handleSize, y: y - this.handleSize });
        }
        else if (x - oppositeCornerX >= 0 && y - oppositeCornerY < 0) {
            this.handleTopLeft.attr({ x: oppositeCornerX, y: y });
            this.handleTopRight.attr({ x: x - this.handleSize, y: y });
            this.handleBottomLeft.attr({ x: oppositeCornerX, y: oppositeCornerY - this.handleSize });
            this.handleBottomRight.attr({ x: x - this.handleSize, y: oppositeCornerY - this.handleSize });
        }
        else if (x - oppositeCornerX < 0 && y - oppositeCornerY < 0) {
            this.handleTopLeft.attr({ x: x, y: y });
            this.handleTopRight.attr({ x: oppositeCornerX - this.handleSize, y: y });
            this.handleBottomLeft.attr({ x: x, y: oppositeCornerY - this.handleSize });
            this.handleBottomRight.attr({ x: oppositeCornerX - this.handleSize, y: oppositeCornerY - this.handleSize });
        }
    };
    EllipseItemImpl.prototype.hideHandles = function () {
        this.handleTopLeft.hide();
        this.handleTopRight.hide();
        this.handleBottomLeft.hide();
        this.handleBottomRight.hide();
    };
    EllipseItemImpl.prototype.showHandles = function () {
        this.handleTopLeft.show();
        this.handleTopRight.show();
        this.handleBottomLeft.show();
        this.handleBottomRight.show();
    };
    EllipseItemImpl.prototype.remove = function () {
        this.handleTopLeft.remove();
        this.handleTopRight.remove();
        this.handleBottomLeft.remove();
        this.handleBottomRight.remove();
        this.ellipse.remove();
    };
    return EllipseItemImpl;
})();
var WorldModelImpl = (function () {
    function WorldModelImpl(zoom, isInteractive) {
        var _this = this;
        this.drawMode = 0;
        this.currentElement = null;
        this.colorFields = [];
        this.wallItems = [];
        this.regions = [];
        this.width = 3000;
        this.height = 3000;
        this.contextMenuId = "twoDModel_stage_context_menu";
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
        $(document).ready(function () {
            _this.initMouseListeners();
        });
    }
    WorldModelImpl.prototype.getZoom = function () {
        return this.zoom;
    };
    WorldModelImpl.prototype.addRobotItemElement = function (element) {
        this.robotItemSet.push(element);
    };
    WorldModelImpl.prototype.insertBeforeRobots = function (element) {
        element.insertBefore(this.robotItemSet);
    };
    WorldModelImpl.prototype.insertAfterRobots = function (element) {
        element.insertAfter(this.robotItemSet);
    };
    WorldModelImpl.prototype.addWall = function (xStart, yStart, xEnd, yEnd) {
        var exPositions = this.getExtendedPositions(xStart, yStart, xEnd, yEnd, WallItemImpl.getWidth());
        var wall = new WallItemImpl(this, exPositions.start.x, exPositions.start.y, exPositions.end.x, exPositions.end.y, this.isInteractive);
        wall.hideHandles();
        this.wallItems.push(wall);
    };
    WorldModelImpl.prototype.addLine = function (xStart, yStart, xEnd, yEnd, width, rgbaColor) {
        var exPositions = this.getExtendedPositions(xStart, yStart, xEnd, yEnd, width);
        var line = new LineItemImpl(this, exPositions.start.x, exPositions.start.y, exPositions.end.x, exPositions.end.y, width, rgbaColor, this.isInteractive);
        line.hideHandles();
        this.colorFields.push(line);
    };
    WorldModelImpl.prototype.addCubicBezier = function (xStart, yStart, xEnd, yEnd, cp1X, cp1Y, cp2X, cp2Y, width, rgbaColor) {
        var exStartPositions = this.getExtendedPositions(xStart, yStart, cp1X, cp1Y, width);
        var exEndPositions = this.getExtendedPositions(xEnd, yEnd, cp2X, cp2Y, width);
        var cubicBezier = new CubicBezierItemImpl(this, exStartPositions.start.x, exStartPositions.start.y, exEndPositions.start.x, exEndPositions.start.y, cp1X, cp1Y, cp2X, cp2Y, width, rgbaColor, this.isInteractive);
        cubicBezier.hideHandles();
        this.colorFields.push(cubicBezier);
    };
    WorldModelImpl.prototype.getExtendedPositions = function (xStart, yStart, xEnd, yEnd, width) {
        var extension = width / 2;
        var cos = Math.abs(xEnd - xStart) / MathUtils.twoPointLenght(xStart, yStart, xEnd, yEnd);
        var extensionX = extension * cos;
        var sin = Math.abs(yEnd - yStart) / MathUtils.twoPointLenght(xStart, yStart, xEnd, yEnd);
        var extensionY = extension * sin;
        var exStartX;
        var exStartY;
        var exEndX;
        var exEndY;
        if (xStart < xEnd) {
            exStartX = xStart - extensionX;
            exEndX = xEnd + extensionX;
        }
        else {
            exStartX = xStart + extensionX;
            exEndX = xEnd - extensionX;
        }
        if (yStart < yEnd) {
            exStartY = yStart - extensionY;
            exEndY = yEnd + extensionY;
        }
        else {
            exStartY = yStart + extensionY;
            exEndY = yEnd - extensionY;
        }
        return { start: new TwoDPosition(exStartX, exStartY), end: new TwoDPosition(exEndX, exEndY) };
    };
    WorldModelImpl.prototype.getMousePosition = function (e) {
        var offset = $("#twoDModel_stage").offset();
        var position = {
            x: (e.pageX - offset.left + $("#twoDModel_stage").scrollLeft()) / this.zoom,
            y: (e.pageY - offset.top + $("#twoDModel_stage").scrollTop()) / this.zoom
        };
        return position;
    };
    WorldModelImpl.prototype.setDrawLineMode = function () {
        this.drawMode = 1;
    };
    WorldModelImpl.prototype.setDrawWallMode = function () {
        this.drawMode = 2;
    };
    WorldModelImpl.prototype.setDrawPencilMode = function () {
        this.drawMode = 3;
    };
    WorldModelImpl.prototype.setDrawEllipseMode = function () {
        this.drawMode = 4;
    };
    WorldModelImpl.prototype.getDrawMode = function () {
        return this.drawMode;
    };
    WorldModelImpl.prototype.setNoneMode = function () {
        this.drawMode = 0;
    };
    WorldModelImpl.prototype.getPaper = function () {
        return this.paper;
    };
    WorldModelImpl.prototype.setCurrentElement = function (element) {
        if (this.currentElement) {
            this.currentElement.hideHandles();
        }
        this.currentElement = element;
        element.showHandles();
    };
    WorldModelImpl.prototype.setStartPositionCross = function (x, y, direction, offsetX, offsetY) {
        this.startPositionCross = new StartPositionItem(this, x + offsetX, y + offsetY, direction);
    };
    WorldModelImpl.prototype.clearPaper = function () {
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
    };
    WorldModelImpl.prototype.deserialize = function (xml, offsetX, offsetY) {
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
    };
    WorldModelImpl.prototype.removeCurrentElement = function () {
        if (this.currentElement) {
            this.currentElement.remove();
        }
    };
    WorldModelImpl.prototype.deserializeRegions = function (regions, offsetX, offsetY) {
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
    };
    WorldModelImpl.prototype.deserializeWalls = function (walls, offsetX, offsetY) {
        for (var i = 0; i < walls.length; i++) {
            var beginPos = this.getPosition(walls[i], 'begin');
            var endPos = this.getPosition(walls[i], 'end');
            this.addWall(beginPos.x + offsetX, beginPos.y + offsetY, endPos.x + offsetX, endPos.y + offsetY);
        }
    };
    WorldModelImpl.prototype.deserializeColorFields = function (colorFields, offsetX, offsetY) {
        var lines = colorFields.getElementsByTagName("line");
        if (lines) {
            this.deserializeLines(lines, offsetX, offsetY);
        }
        var cubicBeziers = colorFields.getElementsByTagName("cubicBezier");
        if (cubicBeziers) {
            this.deserializeCubicBeziers(cubicBeziers, offsetX, offsetY);
        }
    };
    WorldModelImpl.prototype.deserializeLines = function (lines, offsetX, offsetY) {
        for (var i = 0; i < lines.length; i++) {
            var beginPos = this.getPosition(lines[i], 'begin');
            var endPos = this.getPosition(lines[i], 'end');
            var width = parseInt(lines[i].getAttribute('stroke-width'));
            var color = lines[i].getAttribute('stroke');
            var rgbaColor = ColorUtils.getRBGAColor(color);
            this.addLine(beginPos.x + offsetX, beginPos.y + offsetY, endPos.x + offsetX, endPos.y + offsetY, width, rgbaColor);
        }
    };
    WorldModelImpl.prototype.deserializeCubicBeziers = function (cubicBeziers, offsetX, offsetY) {
        for (var i = 0; i < cubicBeziers.length; i++) {
            var beginPos = this.getPosition(cubicBeziers[i], 'begin');
            var endPos = this.getPosition(cubicBeziers[i], 'end');
            var cp1 = this.getPosition(cubicBeziers[i], 'cp1');
            var cp2 = this.getPosition(cubicBeziers[i], 'cp2');
            var width = parseInt(cubicBeziers[i].getAttribute('stroke-width'));
            var color = cubicBeziers[i].getAttribute('stroke');
            var rgbaColor = ColorUtils.getRBGAColor(color);
            this.addCubicBezier(beginPos.x + offsetX, beginPos.y + offsetY, endPos.x + offsetX, endPos.y + offsetY, cp1.x + offsetX, cp1.y + offsetY, cp2.x + offsetX, cp2.y + offsetY, width, rgbaColor);
        }
    };
    WorldModelImpl.prototype.deserializeStartPosition = function (startPosition, offsetX, offsetY) {
        var x = parseFloat(startPosition.getAttribute('x'));
        var y = parseFloat(startPosition.getAttribute('y'));
        var direction = parseFloat(startPosition.getAttribute('direction'));
        this.setStartPositionCross(x, y, direction, offsetX, offsetY);
    };
    WorldModelImpl.prototype.getPosition = function (element, positionName) {
        var positionStr = element.getAttribute(positionName);
        return this.parsePositionString(positionStr);
    };
    WorldModelImpl.prototype.parsePositionString = function (positionStr) {
        var splittedStr = positionStr.split(":");
        var x = parseFloat(splittedStr[0]);
        var y = parseFloat(splittedStr[1]);
        return new TwoDPosition(x, y);
    };
    WorldModelImpl.prototype.initMouseListeners = function () {
        var worldModel = this;
        var shape;
        var isDrawing = false;
        var startDrawPoint;
        $("#twoDModel_stage").mousedown(function (e) {
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
                    shape = new LineItemImpl(worldModel, x, y, x, y, width, new RGBAColor(1, color), worldModel.isInteractive);
                    worldModel.colorFields.push(shape);
                    worldModel.setCurrentElement(shape);
                    isDrawing = true;
                    break;
                case 2:
                    var position = worldModel.getMousePosition(e);
                    var x = position.x;
                    var y = position.y;
                    shape = new WallItemImpl(worldModel, x, y, x, y, worldModel.isInteractive);
                    worldModel.wallItems.push(shape);
                    worldModel.setCurrentElement(shape);
                    isDrawing = true;
                    break;
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
                    break;
                case 4:
                    var position = worldModel.getMousePosition(e);
                    var x = position.x;
                    var y = position.y;
                    var width = $("#pen_width_spinner").val();
                    var color = $("#pen_color_dropdown").val();
                    startDrawPoint = {
                        "x": x,
                        "y": y
                    };
                    shape = new EllipseItemImpl(worldModel, x, y, width, color, worldModel.isInteractive);
                    worldModel.colorFields.push(shape);
                    worldModel.setCurrentElement(shape);
                    isDrawing = true;
                    break;
                default:
            }
        });
        $("#twoDModel_stage").mousemove(function (e) {
            if (worldModel.isInteractive && isDrawing) {
                switch (worldModel.drawMode) {
                    case 1:
                    case 2:
                        var position = worldModel.getMousePosition(e);
                        var x = position.x;
                        var y = position.y;
                        shape.updateEnd(x, y);
                        break;
                    case 3:
                        var position = worldModel.getMousePosition(e);
                        var x = position.x;
                        var y = position.y;
                        shape.updatePath(x, y);
                        break;
                    case 4:
                        var position = worldModel.getMousePosition(e);
                        var x = position.x;
                        var y = position.y;
                        shape.updateCorner(startDrawPoint.x, startDrawPoint.y, x, y);
                        break;
                    default:
                }
            }
        });
        $("#twoDModel_stage").mouseup(function (event) {
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
    };
    WorldModelImpl.prototype.initCustomContextMenu = function () {
        var controller = this;
        $("#twoDModelContent").bind("contextmenu", function (event) {
            event.preventDefault();
        });
        $("#" + controller.contextMenuId + " li").click(function () {
            switch ($(this).attr("data-action")) {
                case "delete":
                    controller.removeCurrentElement();
                    break;
            }
            $("#" + controller.contextMenuId).hide(100);
        });
    };
    WorldModelImpl.prototype.initDeleteListener = function () {
        var _this = this;
        var deleteKey = 46;
        $('html').keyup(function (event) {
            if (event.keyCode == deleteKey) {
                if ($("#twoDModel_stage").is(":visible") && !(document.activeElement.tagName === "INPUT")) {
                    if (!(_this.currentElement instanceof RobotItemImpl || _this.currentElement instanceof SensorItem)) {
                        _this.removeCurrentElement();
                    }
                }
            }
        });
    };
    return WorldModelImpl;
})();
var Direction;
(function (Direction) {
    Direction[Direction["input"] = 0] = "input";
    Direction[Direction["output"] = 1] = "output";
})(Direction || (Direction = {}));
var ReservedVariableType;
(function (ReservedVariableType) {
    ReservedVariableType[ReservedVariableType["scalar"] = 0] = "scalar";
    ReservedVariableType[ReservedVariableType["vector"] = 1] = "vector";
})(ReservedVariableType || (ReservedVariableType = {}));
var CommonRobotModelImpl = (function () {
    function CommonRobotModelImpl() {
        this.ports = [];
        this.allowedConnections = {};
    }
    CommonRobotModelImpl.prototype.getAvailablePorts = function () {
        return this.ports;
    };
    CommonRobotModelImpl.prototype.addAllowedConnection = function (port, devices) {
        this.ports.push(port);
        this.allowedConnections[this.ports.indexOf(port)] = devices;
    };
    CommonRobotModelImpl.prototype.getConfigurablePorts = function () {
        var result = [];
        var robotModel = this;
        robotModel.getAvailablePorts().forEach(function (port) {
            var devices = robotModel.getAllowedDevices(port);
            if (devices.length > 1) {
                result.push(port);
            }
        });
        return result;
    };
    CommonRobotModelImpl.prototype.getAllowedDevices = function (port) {
        return this.allowedConnections[this.ports.indexOf(port)];
    };
    return CommonRobotModelImpl;
})();
var DeviceImpl = (function () {
    function DeviceImpl() {
    }
    return DeviceImpl;
})();
var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var AbstractSensor = (function (_super) {
    __extends(AbstractSensor, _super);
    function AbstractSensor() {
        _super.apply(this, arguments);
    }
    AbstractSensor.parentType = DeviceImpl;
    return AbstractSensor;
})(DeviceImpl);
var ScalarSensor = (function (_super) {
    __extends(ScalarSensor, _super);
    function ScalarSensor() {
        _super.apply(this, arguments);
    }
    ScalarSensor.parentType = AbstractSensor;
    return ScalarSensor;
})(AbstractSensor);
var LightSensor = (function (_super) {
    __extends(LightSensor, _super);
    function LightSensor() {
        _super.apply(this, arguments);
    }
    LightSensor.parentType = ScalarSensor;
    LightSensor.name = "light";
    LightSensor.friendlyName = "Light sensor";
    return LightSensor;
})(ScalarSensor);
var RangeSensor = (function (_super) {
    __extends(RangeSensor, _super);
    function RangeSensor() {
        _super.apply(this, arguments);
    }
    RangeSensor.parentType = ScalarSensor;
    RangeSensor.name = "sonar";
    RangeSensor.friendlyName = "Range sensor";
    return RangeSensor;
})(ScalarSensor);
var TrikInfraredSensor = (function (_super) {
    __extends(TrikInfraredSensor, _super);
    function TrikInfraredSensor() {
        _super.apply(this, arguments);
    }
    TrikInfraredSensor.parentType = RangeSensor;
    TrikInfraredSensor.name = "infrared";
    TrikInfraredSensor.friendlyName = "Infrared Sensor";
    return TrikInfraredSensor;
})(RangeSensor);
var TrikSonarSensor = (function (_super) {
    __extends(TrikSonarSensor, _super);
    function TrikSonarSensor() {
        _super.apply(this, arguments);
    }
    TrikSonarSensor.parentType = RangeSensor;
    TrikSonarSensor.friendlyName = "Sonic Sensor";
    return TrikSonarSensor;
})(RangeSensor);
var VectorSensor = (function (_super) {
    __extends(VectorSensor, _super);
    function VectorSensor() {
        _super.apply(this, arguments);
    }
    VectorSensor.parentType = AbstractSensor;
    return VectorSensor;
})(AbstractSensor);
var TrikLineSensor = (function (_super) {
    __extends(TrikLineSensor, _super);
    function TrikLineSensor() {
        _super.apply(this, arguments);
    }
    TrikLineSensor.parentType = VectorSensor;
    TrikLineSensor.name = "trikLineSensor";
    TrikLineSensor.friendlyName = "Line Sensor";
    return TrikLineSensor;
})(VectorSensor);
var TwoDRobotModel = (function (_super) {
    __extends(TwoDRobotModel, _super);
    function TwoDRobotModel(realModel, name) {
        _super.call(this);
        var twoDRobotModel = this;
        this.realModel = realModel;
        this.name = name;
        this.image = GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/trikTwoDRobot.svg";
        realModel.getAvailablePorts().forEach(function (port) {
            twoDRobotModel.addAllowedConnection(port, realModel.getAllowedDevices(port));
        });
    }
    TwoDRobotModel.prototype.sensorImagePath = function (deviceType) {
        if (deviceType.isA(LightSensor)) {
            return GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/twoDColorEmpty.svg";
        }
        else if (deviceType.isA(TrikInfraredSensor)) {
            return GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/twoDIrRangeSensor.svg";
        }
        else if (deviceType.isA(TrikSonarSensor)) {
            return GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/twoDUsRangeSensor.svg";
        }
        else if (deviceType.isA(TrikLineSensor)) {
            return GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/twoDVideoModule.svg";
        }
        return null;
    };
    TwoDRobotModel.prototype.getName = function () {
        return this.name;
    };
    TwoDRobotModel.prototype.getRobotImage = function () {
        return this.image;
    };
    TwoDRobotModel.prototype.getConfigurablePorts = function () {
        return this.realModel.getConfigurablePorts();
    };
    return TwoDRobotModel;
})(CommonRobotModelImpl);
var DevicesConfigurationProvider = (function () {
    function DevicesConfigurationProvider() {
        this.currentConfiguration = {};
    }
    DevicesConfigurationProvider.prototype.deviceConfigurationChanged = function (robotModelName, portName, device) {
        if (!this.currentConfiguration[robotModelName]) {
            this.currentConfiguration[robotModelName] = {};
        }
        if (device == null) {
            if (this.currentConfiguration[robotModelName][portName]) {
                delete this.currentConfiguration[robotModelName][portName];
            }
        }
        else {
            this.currentConfiguration[robotModelName][portName] = device;
        }
    };
    DevicesConfigurationProvider.prototype.getCurrentConfiguration = function (robotModelName, portName) {
        if (!this.currentConfiguration[robotModelName] || !this.currentConfiguration[robotModelName][portName]) {
            return null;
        }
        return this.currentConfiguration[robotModelName][portName];
    };
    return DevicesConfigurationProvider;
})();
var TouchSensor = (function (_super) {
    __extends(TouchSensor, _super);
    function TouchSensor() {
        _super.apply(this, arguments);
    }
    TouchSensor.parentType = ScalarSensor;
    TouchSensor.name = "touch";
    TouchSensor.friendlyName = "Touch sensor";
    return TouchSensor;
})(ScalarSensor);
var ColorSensor = (function (_super) {
    __extends(ColorSensor, _super);
    function ColorSensor() {
        _super.apply(this, arguments);
    }
    ColorSensor.parentType = ScalarSensor;
    ColorSensor.name = "color";
    ColorSensor.friendlyName = "Color sensor";
    return ColorSensor;
})(ScalarSensor);
var SensorsConfiguration = (function (_super) {
    __extends(SensorsConfiguration, _super);
    function SensorsConfiguration(robotModel) {
        _super.call(this);
        this.robotModel = robotModel;
        this.robotModelName = robotModel.info().getName();
    }
    SensorsConfiguration.prototype.addSensor = function (portName, sensorType, position, direction) {
        if (this.getCurrentConfiguration(this.robotModelName, portName)) {
            this.removeSensor(portName);
        }
        this.deviceConfigurationChanged(this.robotModel.info().getName(), portName, sensorType);
        if (this.isSensorHaveView(sensorType)) {
            this.robotModel.addSensorItem(portName, sensorType, this.robotModel.isModelInteractive(), position, direction);
        }
    };
    SensorsConfiguration.prototype.removeSensor = function (portName) {
        var sensor = this.getCurrentConfiguration(this.robotModelName, portName);
        if (sensor) {
            if (this.isSensorHaveView(sensor)) {
                this.robotModel.removeSensorItem(portName);
            }
            this.deviceConfigurationChanged(this.robotModelName, portName, null);
        }
    };
    SensorsConfiguration.prototype.deserialize = function (xml) {
        var sensors = xml.getElementsByTagName("sensor");
        for (var i = 0; i < sensors.length; i++) {
            if (!sensors[i].getAttribute('type')) {
                continue;
            }
            var portName = sensors[i].getAttribute('port').split("###")[0];
            var typeSplittedStr = sensors[i].getAttribute('type').split("::");
            var device = DeviceInfoImpl.fromString(typeSplittedStr[typeSplittedStr.length - 1]);
            var posString = sensors[i].getAttribute('position');
            var pos = this.parsePositionString(posString);
            var direction = parseFloat(sensors[i].getAttribute('direction'));
            this.addSensor(portName, device, pos, direction);
        }
    };
    SensorsConfiguration.prototype.parsePositionString = function (positionStr) {
        var splittedStr = positionStr.split(":");
        var x = parseFloat(splittedStr[0]);
        var y = parseFloat(splittedStr[1]);
        return new TwoDPosition(x, y);
    };
    SensorsConfiguration.prototype.isSensorHaveView = function (sensorType) {
        return sensorType.isA(TouchSensor)
            || sensorType.isA(ColorSensor)
            || sensorType.isA(LightSensor)
            || sensorType.isA(RangeSensor)
            || sensorType.isA(VectorSensor);
    };
    return SensorsConfiguration;
})(DevicesConfigurationProvider);
var ModelImpl = (function () {
    function ModelImpl(zoom) {
        this.robotModels = [];
        var interactiveAttr = $("#twoDModel_stage").attr("interactive");
        this.isInteractive = (interactiveAttr === "false") ? false : true;
        var model = this;
        model.worldModel = new WorldModelImpl(zoom, this.isInteractive);
        this.minX = 3000;
        this.minY = 3000;
        this.timeline = new TimelineImpl();
    }
    ModelImpl.prototype.getTimeline = function () {
        return this.timeline;
    };
    ModelImpl.prototype.getWorldModel = function () {
        return this.worldModel;
    };
    ModelImpl.prototype.getRobotModels = function () {
        return this.robotModels;
    };
    ModelImpl.prototype.getSetting = function () {
        return this.settings;
    };
    ModelImpl.prototype.addRobotModel = function (robotModel) {
        var _this = this;
        var model = this;
        $(document).ready(function () {
            var robot = new RobotModelImpl(model.worldModel, robotModel, new TwoDPosition(300, 300), _this.isInteractive);
            model.robotModels.push(robot);
            model.timeline.addRobotModel(robot);
        });
    };
    ModelImpl.prototype.deserialize = function (xml) {
        this.findMinPos(xml);
        var offsetX = (this.minX < 0) ? (-this.minX + 700) : 700;
        var offsetY = (this.minY < 0) ? (-this.minY + 700) : 700;
        this.worldModel.deserialize(xml, offsetX, offsetY);
        var robots = xml.getElementsByTagName("robot");
        for (var i = 0; i < robots.length; i++) {
            this.robotModels[i].deserialize(robots[i], offsetX, offsetY);
        }
    };
    ModelImpl.prototype.findMinPos = function (xml) {
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
    };
    ModelImpl.prototype.compareAndSetMinPositionWithWalls = function (walls) {
        for (var i = 0; i < walls.length; i++) {
            var beginPos = this.getPosition(walls[i], 'begin');
            var endPos = this.getPosition(walls[i], 'end');
            this.minX = MathUtils.min(beginPos.x, this.minX);
            this.minY = MathUtils.min(beginPos.y, this.minY);
            this.minX = MathUtils.min(endPos.x, this.minX);
            this.minY = MathUtils.min(endPos.y, this.minY);
        }
    };
    ModelImpl.prototype.compareAndSetMinPositionWithColorFields = function (colorFields) {
        var lines = colorFields.getElementsByTagName("line");
        if (lines) {
            this.compareAndSetMinPositionWithLines(lines);
        }
        var cubicBeziers = colorFields.getElementsByTagName("cubicBezier");
        if (cubicBeziers) {
            this.compareAndSetMinPositionWithCubicBeziers(cubicBeziers);
        }
    };
    ModelImpl.prototype.compareAndSetMinPositionWithLines = function (lines) {
        for (var i = 0; i < lines.length; i++) {
            var beginPos = this.getPosition(lines[i], 'begin');
            var endPos = this.getPosition(lines[i], 'end');
            var width = parseInt(lines[i].getAttribute('stroke-width'));
            this.minX = MathUtils.min(beginPos.x - width, this.minX);
            this.minY = MathUtils.min(beginPos.y - width, this.minY);
            this.minX = MathUtils.min(endPos.x - width, this.minX);
            this.minY = MathUtils.min(endPos.y - width, this.minY);
        }
    };
    ModelImpl.prototype.compareAndSetMinPositionWithCubicBeziers = function (cubicBeziers) {
        for (var i = 0; i < cubicBeziers.length; i++) {
            var beginPos = this.getPosition(cubicBeziers[i], 'begin');
            var endPos = this.getPosition(cubicBeziers[i], 'end');
            var width = parseInt(cubicBeziers[i].getAttribute('stroke-width'));
            this.minX = MathUtils.min(beginPos.x - width, this.minX);
            this.minY = MathUtils.min(beginPos.y - width, this.minY);
            this.minX = MathUtils.min(endPos.x - width, this.minX);
            this.minY = MathUtils.min(endPos.y - width, this.minY);
        }
    };
    ModelImpl.prototype.compareAndSetMinPositionWithRegions = function (regions) {
        for (var i = 0; i < regions.length; i++) {
            var x = parseFloat(regions[i].getAttribute('x'));
            var y = parseFloat(regions[i].getAttribute('y'));
            this.minX = MathUtils.min(x, this.minX);
            this.minY = MathUtils.min(y, this.minY);
        }
    };
    ModelImpl.prototype.compareAndSetMinPositionWithRobots = function (robots) {
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
    };
    ModelImpl.prototype.compareAndSetMinPositionWithSensors = function (sensors) {
        for (var j = 0; j < sensors.length; j++) {
            var posString = sensors[j].getAttribute('position');
            var pos = this.parsePositionString(posString);
            this.minX = MathUtils.min(pos.x, this.minX);
            this.minY = MathUtils.min(pos.y, this.minY);
        }
    };
    ModelImpl.prototype.getPosition = function (element, positionName) {
        var positionStr = element.getAttribute(positionName);
        return this.parsePositionString(positionStr);
    };
    ModelImpl.prototype.parsePositionString = function (positionStr) {
        var splittedStr = positionStr.split(":");
        var x = parseFloat(splittedStr[0]);
        var y = parseFloat(splittedStr[1]);
        return new TwoDPosition(x, y);
    };
    return ModelImpl;
})();
var DeviceInfoImpl = (function () {
    function DeviceInfoImpl(deviceType) {
        this.deviceType = deviceType;
        this.name = deviceType.name;
        this.friendlyName = deviceType.friendlyName;
    }
    DeviceInfoImpl.fromString = function (str) {
        if (!DeviceInfoImpl.createdInfos[str]) {
            throw new Error("DeviceInfo for " + str + " not found");
        }
        else {
            return DeviceInfoImpl.createdInfos[str];
        }
    };
    DeviceInfoImpl.getInstance = function (deviceType) {
        if (!DeviceInfoImpl.createdInfos[deviceType.name]) {
            var deviceInfo = new DeviceInfoImpl(deviceType);
            DeviceInfoImpl.createdInfos[deviceType.name] = deviceInfo;
            return deviceInfo;
        }
        else {
            return DeviceInfoImpl.createdInfos[deviceType.name];
        }
    };
    DeviceInfoImpl.prototype.getName = function () {
        return this.name;
    };
    DeviceInfoImpl.prototype.getFriendlyName = function () {
        return this.friendlyName;
    };
    DeviceInfoImpl.prototype.getType = function () {
        return this.deviceType;
    };
    DeviceInfoImpl.prototype.isA = function (type) {
        var currentParent = this.deviceType;
        while (currentParent && currentParent !== type) {
            currentParent = currentParent.parentType;
        }
        return currentParent != undefined;
    };
    DeviceInfoImpl.createdInfos = {};
    return DeviceInfoImpl;
})();
var TwoDModelEngineFacadeImpl = (function () {
    function TwoDModelEngineFacadeImpl($scope, $compile, $attrs) {
        var _this = this;
        var robotModel = new TwoDRobotModel(new TrikRobotModelBase(), "model");
        this.robotModelName = robotModel.getName();
        this.model = new ModelImpl(parseFloat($("#twoDModel_stage").attr("zoom")));
        this.model.addRobotModel(robotModel);
        $(document).ready(function () {
            _this.initPortsConfiguration($scope, $compile, robotModel);
            _this.makeUnselectable(document.getElementById("twoDModelContent"));
        });
        $scope.followRobot = function () { _this.followRobot(); };
        $scope.closeDisplay = function () { _this.closeDisplay(); };
        $scope.showDisplay = function () { _this.showDisplay(); };
        $scope.setDrawLineMode = function () { _this.setDrawLineMode(); };
        $scope.setDrawWallMode = function () { _this.setDrawWallMode(); };
        $scope.setDrawPencilMode = function () { _this.setDrawPencilMode(); };
        $scope.setDrawEllipseMode = function () { _this.setDrawEllipseMode(); };
        $scope.setNoneMode = function () { _this.setNoneMode(); };
    }
    TwoDModelEngineFacadeImpl.prototype.setDrawLineMode = function () {
        this.model.getWorldModel().setDrawLineMode();
    };
    TwoDModelEngineFacadeImpl.prototype.setDrawWallMode = function () {
        this.model.getWorldModel().setDrawWallMode();
    };
    TwoDModelEngineFacadeImpl.prototype.setDrawPencilMode = function () {
        this.model.getWorldModel().setDrawPencilMode();
    };
    TwoDModelEngineFacadeImpl.prototype.setDrawEllipseMode = function () {
        this.model.getWorldModel().setDrawEllipseMode();
    };
    TwoDModelEngineFacadeImpl.prototype.setNoneMode = function () {
        this.model.getWorldModel().setNoneMode();
    };
    TwoDModelEngineFacadeImpl.prototype.closeDisplay = function () {
        this.model.getRobotModels()[0].closeDisplay();
    };
    TwoDModelEngineFacadeImpl.prototype.showDisplay = function () {
        this.model.getRobotModels()[0].showDisplay();
    };
    TwoDModelEngineFacadeImpl.prototype.followRobot = function () {
        var robotModel = this.model.getRobotModels()[0];
        robotModel.follow(!$("#follow_button").hasClass('active'));
    };
    TwoDModelEngineFacadeImpl.prototype.initPortsConfiguration = function ($scope, $compile, twoDRobotModel) {
        var configurationDropdownsContent = "<p>";
        twoDRobotModel.getConfigurablePorts().forEach(function (port) {
            var portName = port.getName();
            var id = portName + "Select";
            configurationDropdownsContent += "<p>";
            configurationDropdownsContent += portName + " ";
            configurationDropdownsContent += "<select id='" + id + "' style='width: 150px'>";
            configurationDropdownsContent += "<option value='Unused'>Unused</option>";
            var devices = twoDRobotModel.getAllowedDevices(port);
            devices.forEach(function (device) {
                configurationDropdownsContent += "<option value='" + device.getName() + "'>" +
                    device.getFriendlyName();
                +"</option>";
            });
            configurationDropdownsContent += "</select>";
            configurationDropdownsContent += "</p>";
        });
        configurationDropdownsContent += "</p>";
        $('#configurationDropdowns').append($compile(configurationDropdownsContent)($scope));
        this.setPortsSelectsListeners(twoDRobotModel);
    };
    TwoDModelEngineFacadeImpl.prototype.setPortsSelectsListeners = function (twoDRobotModel) {
        var facade = this;
        var sensorsConfiguration = facade.model.getRobotModels()[0].getSensorsConfiguration();
        twoDRobotModel.getConfigurablePorts().forEach(function (port) {
            var portName = port.getName();
            var htmlId = "#" + portName + "Select";
            $(htmlId).change(function () {
                var newValue = $(htmlId).val();
                switch (newValue) {
                    case "Unused":
                        sensorsConfiguration.removeSensor(portName);
                        break;
                    default:
                        var device = DeviceInfoImpl.fromString(newValue);
                        sensorsConfiguration.addSensor(portName, device);
                }
            });
        });
    };
    TwoDModelEngineFacadeImpl.prototype.makeUnselectable = function (element) {
        if (element.nodeType == 1) {
            element.setAttribute("unselectable", "on");
        }
        var child = element.firstChild;
        while (child) {
            this.makeUnselectable(child);
            child = child.nextSibling;
        }
    };
    return TwoDModelEngineFacadeImpl;
})();
var CubicBezierItemImpl = (function () {
    function CubicBezierItemImpl(worldModel, xStart, yStart, xEnd, yEnd, cp1X, cp1Y, cp2X, cp2Y, width, rgbaColor, isInteractive) {
        var paper = worldModel.getPaper();
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
    CubicBezierItemImpl.prototype.getPath = function () {
        return this.path;
    };
    CubicBezierItemImpl.prototype.updateStart = function (x, y) {
    };
    CubicBezierItemImpl.prototype.updateEnd = function (x, y) {
    };
    CubicBezierItemImpl.prototype.updateCP1 = function (x, y) {
    };
    CubicBezierItemImpl.prototype.updateCP2 = function (x, y) {
    };
    CubicBezierItemImpl.prototype.remove = function () {
        this.path.remove();
    };
    CubicBezierItemImpl.prototype.showHandles = function () {
    };
    CubicBezierItemImpl.prototype.hideHandles = function () {
    };
    return CubicBezierItemImpl;
})();
var ColorSensorFull = (function (_super) {
    __extends(ColorSensorFull, _super);
    function ColorSensorFull() {
        _super.apply(this, arguments);
    }
    ColorSensorFull.parentType = ColorSensor;
    ColorSensorFull.name = "colorRecognition";
    ColorSensorFull.friendlyName = "Color sensor (full)";
    return ColorSensorFull;
})(ColorSensor);
var ColorSensorPassive = (function (_super) {
    __extends(ColorSensorPassive, _super);
    function ColorSensorPassive() {
        _super.apply(this, arguments);
    }
    ColorSensorPassive.parentType = ColorSensor;
    ColorSensorPassive.name = "colorNone";
    ColorSensorPassive.friendlyName = "Color sensor (passive)";
    return ColorSensorPassive;
})(ColorSensor);
var ColorSensorRed = (function (_super) {
    __extends(ColorSensorRed, _super);
    function ColorSensorRed() {
        _super.apply(this, arguments);
    }
    ColorSensorRed.parentType = ColorSensor;
    ColorSensorRed.name = "colorRed";
    ColorSensorRed.friendlyName = "Color sensor (red)";
    return ColorSensorRed;
})(ColorSensor);
var ColorSensorGreen = (function (_super) {
    __extends(ColorSensorGreen, _super);
    function ColorSensorGreen() {
        _super.apply(this, arguments);
    }
    ColorSensorGreen.parentType = ColorSensor;
    ColorSensorGreen.name = "colorGreen";
    ColorSensorGreen.friendlyName = "Color sensor (green)";
    return ColorSensorGreen;
})(ColorSensor);
var ColorSensorBlue = (function (_super) {
    __extends(ColorSensorBlue, _super);
    function ColorSensorBlue() {
        _super.apply(this, arguments);
    }
    ColorSensorBlue.parentType = ColorSensor;
    ColorSensorBlue.name = "colorBlue";
    ColorSensorBlue.friendlyName = "Color sensor (blue)";
    return ColorSensorBlue;
})(ColorSensor);
var SensorItem = (function () {
    function SensorItem(robotItem, worldModel, sensorType, pathToImage, isInteractive, position) {
        this.robotItem = robotItem;
        this.worldModel = worldModel;
        var paper = worldModel.getPaper();
        this.sensorType = sensorType;
        this.defineImageSizes(sensorType);
        this.startPosition = this.getStartPosition(position);
        this.offsetPosition = new TwoDPosition();
        this.robotOffsetPosition = new TwoDPosition();
        this.startDirection = 0;
        this.direction = 0;
        this.image = paper.image((pathToImage) ? pathToImage : this.pathToImage(), this.startPosition.x, this.startPosition.y, this.width, this.height);
        worldModel.insertAfterRobots(this.image);
        this.startCenter = new TwoDPosition(this.startPosition.x + this.width / 2, this.startPosition.y + this.height / 2);
        var handleRadius = 10;
        var handleAttrs = {
            fill: "white",
            "fill-opacity": 0,
            cursor: "pointer",
            "stroke-width": 1,
            stroke: "black"
        };
        this.rotationHandle = paper.circle(this.startPosition.x + this.width + 20, this.startPosition.y + this.height / 2, handleRadius).attr(handleAttrs);
        if (isInteractive) {
            this.initDragAndDrop();
        }
        this.hideHandles();
    }
    SensorItem.prototype.setStartDirection = function (direction) {
        this.startDirection = direction;
        this.rotate(direction);
    };
    SensorItem.prototype.setStartPosition = function () {
        this.robotOffsetPosition.x = 0;
        this.robotOffsetPosition.y = 0;
        this.image.attr({ x: this.startPosition.x, y: this.startPosition.y });
        this.startCenter = new TwoDPosition(this.startPosition.x + this.width / 2, this.startPosition.y + this.height / 2);
        this.rotationHandle.attr({ "cx": +this.startPosition.x + this.width + 20, "cy": this.startPosition.y + this.height / 2 });
        this.updateTransformation();
    };
    SensorItem.prototype.getStartPosition = function (position) {
        var startX = this.robotItem.getStartPosition().x;
        var startY = this.robotItem.getStartPosition().y;
        if (position) {
            startX += position.x - this.width / 2;
            startY += position.y - this.height / 2;
        }
        else {
            startX = startX + this.robotItem.getWidth() + 15;
            startY = startY + this.robotItem.getHeight() / 2 - this.height / 2;
        }
        return new TwoDPosition(startX, startY);
    };
    SensorItem.prototype.name = function () {
        if (this.sensorType.isA(TouchSensor)) {
            return "touch";
        }
        else if (this.sensorType.isA(ColorSensorFull) || this.sensorType.isA(ColorSensorPassive)) {
            return "color_empty";
        }
        else if (this.sensorType.isA(ColorSensorRed)) {
            return "color_red";
        }
        else if (this.sensorType.isA(ColorSensorGreen)) {
            return "color_green";
        }
        else if (this.sensorType.isA(ColorSensorBlue)) {
            return "color_blue";
        }
        else if (this.sensorType.isA(RangeSensor)) {
            return "sonar";
        }
        else if (this.sensorType.isA(LightSensor)) {
            return "light";
        }
        else {
            alert(!"Unknown sensor type");
            return "";
        }
    };
    SensorItem.prototype.pathToImage = function () {
        return GeneralConstants.APP_ROOT_PATH + "images/2dmodel/sensors/2d_" + this.name() + ".png";
    };
    SensorItem.prototype.defineImageSizes = function (sensorType) {
        if (sensorType.isA(TouchSensor)) {
            this.width = 25;
            this.height = 25;
        }
        else if (sensorType.isA(ColorSensor) || sensorType.isA(LightSensor)) {
            this.width = 15;
            this.height = 15;
        }
        else if (sensorType.isA(RangeSensor)) {
            this.width = 35;
            this.height = 35;
        }
        else {
            alert("Unknown sensor type");
        }
    };
    SensorItem.prototype.move = function (deltaX, deltaY) {
        this.robotOffsetPosition.x = deltaX;
        this.robotOffsetPosition.y = deltaY;
        this.updateTransformation();
    };
    SensorItem.prototype.rotate = function (angle) {
        this.direction = angle;
        this.updateTransformation();
    };
    SensorItem.prototype.hideHandles = function () {
        this.rotationHandle.hide();
    };
    SensorItem.prototype.showHandles = function () {
        this.rotationHandle.toFront();
        this.rotationHandle.show();
    };
    SensorItem.prototype.remove = function () {
        this.image.remove();
        this.rotationHandle.remove();
    };
    SensorItem.prototype.initDragAndDrop = function () {
        var sensorItem = this;
        var startHandle = function () {
            this.cx = sensorItem.rotationHandle.matrix.x(this.attr("cx"), this.attr("cy"));
            this.cy = sensorItem.rotationHandle.matrix.y(this.attr("cx"), this.attr("cy"));
            return this;
        }, moveHandle = function (dx, dy) {
            var newX = this.cx + dx * (1 / sensorItem.worldModel.getZoom());
            var newY = this.cy + dy * (1 / sensorItem.worldModel.getZoom());
            var center = sensorItem.getCurrentCenter();
            var diffX = newX - center.x;
            var diffY = newY - center.y;
            var tan = diffY / diffX;
            var angle = Math.atan(tan) / (Math.PI / 180);
            if (diffX < 0) {
                angle += 180;
            }
            sensorItem.rotate(angle - sensorItem.robotItem.getDirection());
            return this;
        }, upHandle = function () {
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
        }, move = function (dx, dy) {
            var rotatedDelta = MathUtils.rotateVector(dx, dy, -sensorItem.robotItem.getDirection());
            sensorItem.offsetPosition.x = this.lastOffsetX + rotatedDelta.x * (1 / sensorItem.worldModel.getZoom());
            sensorItem.offsetPosition.y = this.lastOffsetY + rotatedDelta.y * (1 / sensorItem.worldModel.getZoom());
            sensorItem.updateTransformation();
            return this;
        }, up = function () {
            return this;
        };
        this.image.drag(move, start, up);
    };
    SensorItem.prototype.updateTransformation = function () {
        this.image.transform(this.getTransformation());
        this.rotationHandle.transform(this.getHandleTransformation());
    };
    SensorItem.prototype.getTransformation = function () {
        return "T" + this.robotOffsetPosition.x + "," + this.robotOffsetPosition.y +
            "T" + this.offsetPosition.x + "," + this.offsetPosition.y +
            this.getRobotRotationTransformation() +
            this.getRotationTransformation();
    };
    SensorItem.prototype.getHandleTransformation = function () {
        var center = this.getCurrentCenter();
        return "T" + this.robotOffsetPosition.x + "," + this.robotOffsetPosition.y +
            "T" + this.offsetPosition.x + "," + this.offsetPosition.y +
            this.getRobotRotationTransformation() +
            this.getRotationTransformation() + "," + center.x + "," + center.y;
    };
    SensorItem.prototype.getRobotRotationTransformation = function () {
        var center = this.robotItem.getCenter();
        return "R" + this.robotItem.getDirection() + "," + center.x + "," + center.y;
    };
    SensorItem.prototype.getRotationTransformation = function () {
        return "R" + this.direction;
    };
    SensorItem.prototype.getCurrentCenter = function () {
        var centerX = this.image.matrix.x(this.startCenter.x, this.startCenter.y);
        var centerY = this.image.matrix.y(this.startCenter.x, this.startCenter.y);
        return new TwoDPosition(centerX, centerY);
    };
    return SensorItem;
})();
var SonarSensorItem = (function (_super) {
    __extends(SonarSensorItem, _super);
    function SonarSensorItem(robotItem, worldModel, sensorType, pathToImage, isInteractive, position) {
        _super.call(this, robotItem, worldModel, sensorType, pathToImage, isInteractive, position);
        this.sonarRange = 255;
        var paper = worldModel.getPaper();
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
        this.scanningRegion.attr({ fill: "#c5d0de", stroke: "#b1bbc7", opacity: 0.5 });
        worldModel.insertAfterRobots(this.scanningRegion);
        this.regionTranslation = "T0,0";
        this.regionRotation = "R0";
    }
    SonarSensorItem.prototype.setStartPosition = function () {
        _super.prototype.setStartPosition.call(this);
        this.updateRegionTransformation();
    };
    SonarSensorItem.prototype.move = function (deltaX, deltaY) {
        _super.prototype.move.call(this, deltaX, deltaY);
        this.updateRegionTransformation();
    };
    SonarSensorItem.prototype.updateTransformation = function () {
        _super.prototype.updateTransformation.call(this);
        this.updateRegionTransformation();
    };
    SonarSensorItem.prototype.rotate = function (angle) {
        _super.prototype.rotate.call(this, angle);
        this.updateRegionTransformation();
    };
    SonarSensorItem.prototype.remove = function () {
        _super.prototype.remove.call(this);
        this.scanningRegion.remove();
    };
    SonarSensorItem.prototype.updateRegionTransformation = function () {
        var offsetX = this.offsetPosition.x + this.robotOffsetPosition.x;
        var offsetY = this.offsetPosition.y + this.robotOffsetPosition.y;
        this.regionTranslation = "T" + offsetX + "," + offsetY;
        this.scanningRegion.transform(this.getRegionTransformation());
    };
    SonarSensorItem.prototype.getRegionTransformation = function () {
        return this.regionTranslation + this.getRobotRotationTransformation() +
            "r" + this.direction + "," + this.regionStartX + "," + this.regionStartY;
    };
    return SonarSensorItem;
})(SensorItem);
var Marker = (function () {
    function Marker(paper, robotCenter) {
        this.height = 6;
        this.paper = paper;
        this.down = false;
        this.color = "#000000";
        this.robotCenter = robotCenter;
        this.prevCenter = new TwoDPosition(robotCenter.x, robotCenter.y);
        this.pointSet = paper.set();
    }
    Marker.prototype.setCenter = function (robotCenter) {
        this.prevCenter = new TwoDPosition(this.robotCenter.x, this.robotCenter.y);
        this.robotCenter = robotCenter;
    };
    Marker.prototype.setColor = function (color) {
        this.color = color;
    };
    Marker.prototype.isDown = function () {
        return this.down;
    };
    Marker.prototype.setDown = function (down) {
        this.down = down;
    };
    Marker.prototype.clear = function () {
        while (this.pointSet.length) {
            this.pointSet.pop().remove();
        }
        this.prevCenter = new TwoDPosition(this.robotCenter.x, this.robotCenter.y);
    };
    Marker.prototype.drawPoint = function () {
        var length = MathUtils.twoPointLenght(this.robotCenter.x, this.robotCenter.y, this.prevCenter.x, this.prevCenter.y);
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
        }
        else {
            angle = 0;
        }
        var rect = this.paper.rect(this.prevCenter.x - 1, this.prevCenter.y - this.height / 2, length + 2, this.height);
        rect.attr({ "stroke-width": 0, "fill": this.color });
        rect.transform("R" + angle + "," + this.prevCenter.x + "," + this.prevCenter.y);
        rect.toBack();
        this.pointSet.push(rect);
    };
    return Marker;
})();
var RobotItemImpl = (function () {
    function RobotItemImpl(worldModel, position, imageFileName, isInteractive) {
        this.startCenter = new TwoDPosition();
        this.width = 50;
        this.height = 50;
        this.offsetX = 0;
        this.offsetY = 0;
        this.sensors = {};
        this.roughening = 5;
        this.counter = 0;
        this.worldModel = worldModel;
        this.startPosition = position;
        this.direction = 0;
        this.startDirection = 0;
        this.isFollow = false;
        this.scroller = new StageScroller(worldModel.getZoom());
        this.offsetPosition = new TwoDPosition();
        this.startCenter.x = position.x + this.width / 2;
        this.startCenter.y = position.y + this.height / 2;
        this.createElement(worldModel, position, imageFileName);
        if (isInteractive) {
            this.initDragAndDrop();
        }
        this.hideHandles();
    }
    RobotItemImpl.prototype.setStartPosition = function (position, direction) {
        this.startPosition = position;
        this.direction = direction;
        this.offsetPosition.x = 0;
        this.offsetPosition.y = 0;
        this.startDirection = direction;
        this.image.attr({ x: position.x, y: position.y });
        this.startCenter.x = position.x + this.width / 2;
        this.startCenter.y = position.y + this.height / 2;
        this.rotationHandle.attr({ "cx": +position.x + this.width + 20, "cy": position.y + this.height / 2 });
        this.updateTransformation();
        this.marker.setCenter(new TwoDPosition(this.startCenter.x, this.startCenter.y));
    };
    RobotItemImpl.prototype.hideHandles = function () {
        this.rotationHandle.hide();
    };
    RobotItemImpl.prototype.showHandles = function () {
        this.rotationHandle.toFront();
        this.rotationHandle.show();
    };
    RobotItemImpl.prototype.getWidth = function () {
        return this.width;
    };
    RobotItemImpl.prototype.getHeight = function () {
        return this.height;
    };
    RobotItemImpl.prototype.getStartPosition = function () {
        return this.startPosition;
    };
    RobotItemImpl.prototype.getDirection = function () {
        return this.direction;
    };
    RobotItemImpl.prototype.getCenter = function () {
        var centerX = this.image.matrix.x(this.startCenter.x, this.startCenter.y);
        var centerY = this.image.matrix.y(this.startCenter.x, this.startCenter.y);
        return new TwoDPosition(centerX, centerY);
    };
    RobotItemImpl.prototype.removeSensorItem = function (portName) {
        var sensor = this.sensors[portName];
        if (sensor) {
            sensor.remove();
            delete this.sensors[portName];
        }
    };
    RobotItemImpl.prototype.addSensorItem = function (portName, sensorType, pathToImage, isInteractive, position, direction) {
        var sensor;
        if (sensorType.isA(RangeSensor)) {
            sensor = new SonarSensorItem(this, this.worldModel, sensorType, pathToImage, isInteractive, position);
        }
        else {
            sensor = new SensorItem(this, this.worldModel, sensorType, pathToImage, isInteractive, position);
        }
        if (direction) {
            sensor.setStartDirection(direction);
        }
        sensor.move(this.offsetPosition.x, this.offsetPosition.y);
        sensor.updateTransformation();
        this.sensors[portName] = sensor;
    };
    RobotItemImpl.prototype.moveSensors = function (deltaX, deltaY) {
        for (var portName in this.sensors) {
            var sensor = this.sensors[portName];
            sensor.move(deltaX, deltaY);
        }
    };
    RobotItemImpl.prototype.clearCurrentPosition = function () {
        if (this.timeoutId) {
            clearTimeout(this.timeoutId);
            this.timeoutId = undefined;
        }
        this.marker.setDown(false);
        this.marker.setColor("#000000");
        this.marker.clear();
        this.setStartPosition(this.startPosition, this.startDirection);
        this.clearSensorsPosition();
        this.counter = 0;
    };
    RobotItemImpl.prototype.setOffsetX = function (offsetX) {
        this.offsetX = offsetX;
    };
    RobotItemImpl.prototype.setOffsetY = function (offsetY) {
        this.offsetY = offsetY;
    };
    RobotItemImpl.prototype.moveToPoint = function (x, y, rotation) {
        var newX = x + this.offsetX;
        var newY = y + this.offsetY;
        this.offsetPosition.x = newX - this.startPosition.x;
        this.offsetPosition.y = newY - this.startPosition.y;
        this.direction = rotation;
        this.updateTransformation();
        this.moveSensors(this.offsetPosition.x, this.offsetPosition.y);
        this.updateMarkerState();
    };
    RobotItemImpl.prototype.move = function (deltaX, deltaY, direction) {
        this.offsetPosition.x += deltaX;
        this.offsetPosition.y += deltaY;
        this.direction = direction;
        this.updateTransformation();
        this.moveSensors(this.offsetPosition.x, this.offsetPosition.y);
        this.updateMarkerState();
    };
    RobotItemImpl.prototype.setMarkerDown = function (down) {
        this.marker.setDown(down);
    };
    RobotItemImpl.prototype.setMarkerColor = function (color) {
        this.marker.setColor(color);
    };
    RobotItemImpl.prototype.remove = function () {
        this.rotationHandle.remove();
        this.image.remove();
        for (var portName in this.sensors) {
            this.removeSensorItem(portName);
        }
    };
    RobotItemImpl.prototype.hide = function () {
        this.image.hide();
    };
    RobotItemImpl.prototype.show = function () {
        this.image.show();
    };
    RobotItemImpl.prototype.follow = function (value) {
        this.isFollow = value;
    };
    RobotItemImpl.prototype.returnToStart = function () {
        this.scroller.scrollToPoint(this.startPosition.x, this.startPosition.y);
    };
    RobotItemImpl.prototype.updateSensorsTransformation = function () {
        for (var portName in this.sensors) {
            var sensor = this.sensors[portName];
            sensor.updateTransformation();
        }
    };
    RobotItemImpl.prototype.clearSensorsPosition = function () {
        for (var portName in this.sensors) {
            var sensor = this.sensors[portName];
            sensor.setStartPosition();
        }
    };
    RobotItemImpl.prototype.updateTransformation = function () {
        this.image.transform(this.getTransformation());
        this.rotationHandle.transform(this.getTransformation());
        var center = this.getCenter();
        if (this.isFollow) {
            this.scroller.scrollToPoint(center.x, center.y);
        }
    };
    RobotItemImpl.prototype.initDragAndDrop = function () {
        var robotItem = this;
        var startHandle = function () {
            this.cx = robotItem.rotationHandle.matrix.x(this.attr("cx"), this.attr("cy"));
            this.cy = robotItem.rotationHandle.matrix.y(this.attr("cx"), this.attr("cy"));
            return this;
        }, moveHandle = function (dx, dy) {
            var newX = this.cx + dx * (1 / robotItem.worldModel.getZoom());
            var newY = this.cy + dy * (1 / robotItem.worldModel.getZoom());
            var center = robotItem.getCenter();
            var diffX = newX - center.x;
            var diffY = newY - center.y;
            var tan = diffY / diffX;
            var angle = Math.atan(tan) / (Math.PI / 180);
            if (diffX < 0) {
                angle += 180;
            }
            robotItem.direction = angle;
            robotItem.updateTransformation();
            robotItem.updateSensorsTransformation();
            return this;
        }, upHandle = function () {
            return this;
        };
        robotItem.rotationHandle.drag(moveHandle, startHandle, upHandle);
        var start = function (event) {
            if (!robotItem.worldModel.getDrawMode()) {
                robotItem.worldModel.setCurrentElement(robotItem);
                this.lastOffsetX = robotItem.offsetPosition.x;
                this.lastOffsetY = robotItem.offsetPosition.y;
            }
            return this;
        }, move = function (dx, dy) {
            robotItem.offsetPosition.x = this.lastOffsetX + dx * (1 / robotItem.worldModel.getZoom());
            robotItem.offsetPosition.y = this.lastOffsetY + dy * (1 / robotItem.worldModel.getZoom());
            robotItem.updateTransformation();
            robotItem.moveSensors(robotItem.offsetPosition.x, robotItem.offsetPosition.y);
            return this;
        }, up = function () {
            return this;
        };
        this.image.drag(move, start, up);
    };
    RobotItemImpl.prototype.updateMarkerState = function () {
        var center = this.getCenter();
        if (this.marker.isDown()) {
            if (this.counter > this.roughening) {
                this.marker.setCenter(new TwoDPosition(center.x, center.y));
                this.marker.drawPoint();
                this.counter = 0;
            }
            else {
                this.counter++;
            }
        }
        else {
            this.marker.setCenter(new TwoDPosition(center.x, center.y));
        }
    };
    RobotItemImpl.prototype.createElement = function (worldModel, position, imageFileName) {
        var paper = worldModel.getPaper();
        this.image = paper.image(imageFileName, position.x, position.y, this.width, this.height);
        worldModel.addRobotItemElement(this.image);
        this.marker = new Marker(paper, new TwoDPosition(this.startCenter.x, this.startCenter.y));
        var handleRadius = 10;
        var handleAttrs = {
            fill: "white",
            "fill-opacity": 0,
            cursor: "pointer",
            "stroke-width": 1,
            stroke: "black"
        };
        this.rotationHandle = paper.circle(position.x + this.width + 20, position.y + this.height / 2, handleRadius).attr(handleAttrs);
    };
    RobotItemImpl.prototype.getTransformation = function () {
        var cx = this.startCenter.x + this.offsetPosition.x;
        var cy = this.startCenter.y + this.offsetPosition.y;
        return "T" + this.offsetPosition.x + "," + this.offsetPosition.y + "R" + this.direction + "," + cx + "," + cy;
    };
    return RobotItemImpl;
})();
var EllipseRegion = (function (_super) {
    __extends(EllipseRegion, _super);
    function EllipseRegion(worldModel) {
        _super.call(this, worldModel);
        this.shape = worldModel.getPaper().ellipse(0, 0, 0, 0);
        this.setColor(this.defaultColor);
        this.setWidht(this.defaultWidth);
        this.setHeight(this.defaultHeight);
        this.shape.toBack();
    }
    EllipseRegion.prototype.setPosition = function (position) {
        this.shape.attr({ cx: position.x + this.getWith(), cy: position.y + this.getHeight() });
    };
    EllipseRegion.prototype.setWidht = function (width) {
        this.shape.attr({ rx: width / 2 });
    };
    EllipseRegion.prototype.setHeight = function (height) {
        this.shape.attr({ ry: height / 2 });
    };
    EllipseRegion.prototype.getPosition = function () {
        return new TwoDPosition(this.shape.attr("cx"), this.shape.attr("cy"));
    };
    EllipseRegion.prototype.getWith = function () {
        return this.shape.attr("rx");
    };
    EllipseRegion.prototype.getHeight = function () {
        return this.shape.attr("ry");
    };
    return EllipseRegion;
})(RegionItem);
var RectangularRegion = (function (_super) {
    __extends(RectangularRegion, _super);
    function RectangularRegion(worldModel) {
        _super.call(this, worldModel);
        this.shape = worldModel.getPaper().rect(0, 0, 0, 0);
        this.setColor(this.defaultColor);
        this.setWidht(this.defaultWidth);
        this.setHeight(this.defaultHeight);
        this.shape.toBack();
    }
    return RectangularRegion;
})(RegionItem);
var Constants = (function () {
    function Constants() {
    }
    Constants.robotWheelDiameterInPx = 16;
    Constants.robotWheelDiameterInCm = 5.6;
    Constants.pixelsInCm = Constants.robotWheelDiameterInPx / Constants.robotWheelDiameterInCm;
    return Constants;
})();
var DeviceConfiguration = (function () {
    function DeviceConfiguration() {
        this.configurationMap = {};
        this.configurationMap["M1"] = new Motor();
        this.configurationMap["M2"] = new Motor();
        this.configurationMap["M3"] = new Motor();
        this.configurationMap["M4"] = new Motor();
    }
    DeviceConfiguration.prototype.getConfigurationMap = function () {
        return this.configurationMap;
    };
    DeviceConfiguration.prototype.getDeviceByPortName = function (portName) {
        return this.configurationMap[portName];
    };
    DeviceConfiguration.prototype.setDeviceToPort = function (portName, device) {
        this.configurationMap[portName] = device;
    };
    DeviceConfiguration.prototype.clearState = function () {
        for (var portName in this.configurationMap) {
            if (this.configurationMap[portName] instanceof Motor) {
                var motor = this.configurationMap[portName];
                motor.setPower(0);
            }
        }
    };
    return DeviceConfiguration;
})();
var RobotModelImpl = (function () {
    function RobotModelImpl(worldModel, twoDRobotModel, position, isInteractive) {
        this.worldModel = worldModel;
        this.twoDRobotModel = twoDRobotModel;
        this.isInteractive = isInteractive;
        this.robotItem = new RobotItemImpl(worldModel, position, twoDRobotModel.getRobotImage(), isInteractive);
        this.sensorsConfiguration = new SensorsConfiguration(this);
        this.displayWidget = new DisplayWidget();
        this.deviceConfiguration = new DeviceConfiguration();
        this.runner = new Runner();
    }
    RobotModelImpl.prototype.info = function () {
        return this.twoDRobotModel;
    };
    RobotModelImpl.prototype.removeSensorItem = function (portName) {
        this.robotItem.removeSensorItem(portName);
    };
    RobotModelImpl.prototype.getSensorsConfiguration = function () {
        return this.sensorsConfiguration;
    };
    RobotModelImpl.prototype.addSensorItem = function (portName, deviceType, isInteractive, position, direction) {
        this.robotItem.addSensorItem(portName, deviceType, this.twoDRobotModel.sensorImagePath(deviceType), isInteractive, position, direction);
    };
    RobotModelImpl.prototype.isModelInteractive = function () {
        return this.isInteractive;
    };
    RobotModelImpl.prototype.parsePositionString = function (positionStr) {
        var splittedStr = positionStr.split(":");
        var x = parseFloat(splittedStr[0]);
        var y = parseFloat(splittedStr[1]);
        return new TwoDPosition(x, y);
    };
    RobotModelImpl.prototype.deserialize = function (xml, offsetX, offsetY) {
        var posString = xml.getAttribute('position');
        var pos = this.parsePositionString(posString);
        pos.x += offsetX;
        pos.y += offsetY;
        var direction = parseFloat(xml.getAttribute('direction'));
        this.robotItem.setStartPosition(pos, direction);
        this.robotItem.setOffsetX(offsetX);
        this.robotItem.setOffsetY(offsetY);
        this.robotItem.returnToStart();
        this.sensorsConfiguration.deserialize(xml);
        this.robotItem.show();
    };
    RobotModelImpl.prototype.showCheckResult = function (result) {
        this.stopPlay();
        this.runner.run(this.robotItem, this.displayWidget, result);
    };
    RobotModelImpl.prototype.stopPlay = function () {
        this.runner.stop(this.robotItem, this.displayWidget);
        this.robotItem.clearCurrentPosition();
        this.robotItem.returnToStart();
    };
    RobotModelImpl.prototype.closeDisplay = function () {
        this.displayWidget.hide();
    };
    RobotModelImpl.prototype.showDisplay = function () {
        this.displayWidget.show();
    };
    RobotModelImpl.prototype.follow = function (value) {
        this.robotItem.follow(value);
    };
    RobotModelImpl.prototype.getDeviceByPortName = function (portName) {
        return this.deviceConfiguration.getDeviceByPortName(portName);
    };
    RobotModelImpl.prototype.nextFragment = function () {
        var angle = MathUtils.toRadians(this.robotItem.getDirection());
        var robotHeight = 50;
        var timeInterval = 1;
        var speedLeft = this.getDeviceByPortName("M3").getPower() / 70;
        var speedRight = this.getDeviceByPortName("M4").getPower() / 70;
        var averageSpeed = (speedLeft + speedRight) / 2;
        var deltaX = 0;
        var deltaY = 0;
        if (speedLeft != speedRight) {
            var radius = speedLeft * robotHeight / (speedLeft - speedRight);
            var averageRadius = radius - robotHeight / 2;
            var angularSpeed = 0;
            if (speedLeft == -speedRight) {
                angularSpeed = speedLeft / radius;
            }
            else {
                angularSpeed = averageSpeed / averageRadius;
            }
            var gammaRadians = timeInterval * angularSpeed;
            angle += gammaRadians;
            deltaX = averageSpeed * Math.cos(angle);
            deltaY = averageSpeed * Math.sin(angle);
        }
        else {
            deltaX = averageSpeed * Math.cos(angle);
            deltaY += averageSpeed * Math.sin(angle);
        }
        this.robotItem.move(deltaX, deltaY, MathUtils.toDegrees(angle));
    };
    RobotModelImpl.prototype.setMarkerDown = function (down) {
        this.robotItem.setMarkerDown(down);
    };
    RobotModelImpl.prototype.setMarkerColor = function (color) {
        this.robotItem.setMarkerColor(color);
    };
    RobotModelImpl.prototype.clearState = function () {
        this.deviceConfiguration.clearState();
    };
    RobotModelImpl.prototype.clearCurrentPosition = function () {
        this.robotItem.clearCurrentPosition();
    };
    RobotModelImpl.prototype.getDisplayWidget = function () {
        return this.displayWidget;
    };
    return RobotModelImpl;
})();
var TimelineImpl = (function () {
    function TimelineImpl() {
        this.fps = 60;
        this.defaultFrameLength = 1000 / this.fps;
        this.slowSpeedFactor = 2;
        this.normalSpeedFactor = 5;
        this.fastSpeedFactor = 10;
        this.immediateSpeedFactor = 100000000;
        this.speedFactor = 1;
        this.robotModels = [];
        this.setActive(false);
    }
    TimelineImpl.prototype.start = function () {
        if (this.isActive) {
            return;
        }
        this.setActive(true);
        var timeline = this;
        this.cyclesCount = 0;
        this.robotModels[0].getDisplayWidget().displayToFront();
        this.intervalId = setInterval(function () { timeline.onTimer(timeline); }, this.defaultFrameLength);
    };
    TimelineImpl.prototype.stop = function () {
        this.setActive(false);
        this.robotModels[0].getDisplayWidget().displayToBack();
        this.robotModels[0].getDisplayWidget().reset();
        clearInterval(this.intervalId);
        this.robotModels.forEach(function (model) { model.clearState(); });
    };
    TimelineImpl.prototype.onTimer = function (timeline) {
        if (!this.isActive) {
            return;
        }
        this.cyclesCount++;
        if (this.cyclesCount >= this.speedFactor) {
            timeline.getRobotModels().forEach(function (model) {
                model.nextFragment();
            });
            this.cyclesCount = 0;
        }
    };
    TimelineImpl.prototype.setActive = function (value) {
        this.isActive = value;
    };
    TimelineImpl.prototype.setSpeedFactor = function (factor) {
        this.speedFactor = factor;
    };
    TimelineImpl.prototype.getSpeedFactor = function () {
        return this.speedFactor;
    };
    TimelineImpl.prototype.getRobotModels = function () {
        return this.robotModels;
    };
    TimelineImpl.prototype.addRobotModel = function (robotModel) {
        this.robotModels.push(robotModel);
    };
    return TimelineImpl;
})();
var PortInfoImpl = (function () {
    function PortInfoImpl(name, direction, nameAliases, reservedVariable, reservedVariableType) {
        this.nameAliases = [];
        this.reservedVariableType = ReservedVariableType.scalar;
        this.name = name;
        this.direction = direction;
        this.nameAliases = nameAliases;
        this.reservedVariable = reservedVariable;
        this.reservedVariableType = reservedVariableType;
    }
    PortInfoImpl.prototype.getName = function () {
        return this.name;
    };
    PortInfoImpl.prototype.getDirection = function () {
        return this.direction;
    };
    PortInfoImpl.prototype.getNameAliases = function () {
        return this.nameAliases;
    };
    PortInfoImpl.prototype.getReservedVariable = function () {
        return this.reservedVariable;
    };
    PortInfoImpl.prototype.getReservedVariableType = function () {
        return this.reservedVariableType;
    };
    return PortInfoImpl;
})();
var Display = (function (_super) {
    __extends(Display, _super);
    function Display() {
        _super.apply(this, arguments);
    }
    Display.parentType = DeviceImpl;
    Display.name = "display";
    Display.friendlyName = "Display";
    return Display;
})(DeviceImpl);
var Speaker = (function (_super) {
    __extends(Speaker, _super);
    function Speaker() {
        _super.apply(this, arguments);
    }
    Speaker.parentType = DeviceImpl;
    Speaker.name = "speaker";
    Speaker.friendlyName = "Speaker";
    return Speaker;
})(DeviceImpl);
var Button = (function (_super) {
    __extends(Button, _super);
    function Button() {
        _super.apply(this, arguments);
    }
    Button.parentType = ScalarSensor;
    Button.name = "button";
    Button.friendlyName = "Button";
    return Button;
})(ScalarSensor);
var Motor = (function (_super) {
    __extends(Motor, _super);
    function Motor(power) {
        if (power === void 0) { power = 0; }
        _super.call(this);
        this.power = power;
    }
    Motor.prototype.getPower = function () {
        return this.power;
    };
    Motor.prototype.setPower = function (power) {
        this.power = power;
    };
    Motor.parentType = DeviceImpl;
    Motor.name = "motor";
    Motor.friendlyName = "Motor";
    return Motor;
})(DeviceImpl);
var EncoderSensor = (function (_super) {
    __extends(EncoderSensor, _super);
    function EncoderSensor() {
        _super.apply(this, arguments);
    }
    EncoderSensor.parentType = ScalarSensor;
    EncoderSensor.name = "encoder";
    EncoderSensor.friendlyName = "Encoder";
    return EncoderSensor;
})(ScalarSensor);
var GyroscopeSensor = (function (_super) {
    __extends(GyroscopeSensor, _super);
    function GyroscopeSensor() {
        _super.apply(this, arguments);
    }
    GyroscopeSensor.parentType = ScalarSensor;
    GyroscopeSensor.name = "gyroscope";
    GyroscopeSensor.friendlyName = "Gyroscope";
    return GyroscopeSensor;
})(ScalarSensor);
var AccelerometerSensor = (function (_super) {
    __extends(AccelerometerSensor, _super);
    function AccelerometerSensor() {
        _super.apply(this, arguments);
    }
    AccelerometerSensor.parentType = ScalarSensor;
    AccelerometerSensor.name = "accelerometer";
    AccelerometerSensor.friendlyName = "Accelerometer";
    return AccelerometerSensor;
})(ScalarSensor);
var TrikMotionSensor = (function (_super) {
    __extends(TrikMotionSensor, _super);
    function TrikMotionSensor() {
        _super.apply(this, arguments);
    }
    TrikMotionSensor.parentType = ScalarSensor;
    TrikMotionSensor.name = "motion";
    TrikMotionSensor.friendlyName = "Motion Sensor";
    return TrikMotionSensor;
})(ScalarSensor);
var TrikLed = (function (_super) {
    __extends(TrikLed, _super);
    function TrikLed() {
        _super.apply(this, arguments);
    }
    TrikLed.parentType = DeviceImpl;
    TrikLed.name = "led";
    TrikLed.friendlyName = "Led";
    return TrikLed;
})(DeviceImpl);
var TrikColorSensor = (function (_super) {
    __extends(TrikColorSensor, _super);
    function TrikColorSensor() {
        _super.apply(this, arguments);
    }
    TrikColorSensor.parentType = VectorSensor;
    TrikColorSensor.name = "trikColorSensor";
    TrikColorSensor.friendlyName = "Color Sensor";
    return TrikColorSensor;
})(VectorSensor);
var TrikObjectSensor = (function (_super) {
    __extends(TrikObjectSensor, _super);
    function TrikObjectSensor() {
        _super.apply(this, arguments);
    }
    TrikObjectSensor.parentType = VectorSensor;
    TrikObjectSensor.name = "trikObjectSensor";
    TrikObjectSensor.friendlyName = "Object Sensor";
    return TrikObjectSensor;
})(VectorSensor);
var TrikShell = (function (_super) {
    __extends(TrikShell, _super);
    function TrikShell() {
        _super.apply(this, arguments);
    }
    TrikShell.parentType = DeviceImpl;
    TrikShell.name = "shell";
    TrikShell.friendlyName = "Shell";
    return TrikShell;
})(DeviceImpl);
var TrikGamepadButton = (function (_super) {
    __extends(TrikGamepadButton, _super);
    function TrikGamepadButton() {
        _super.apply(this, arguments);
    }
    TrikGamepadButton.parentType = Button;
    TrikGamepadButton.name = "gamepadButton";
    TrikGamepadButton.friendlyName = "Android Gamepad Button";
    return TrikGamepadButton;
})(Button);
var TrikGamepadPad = (function (_super) {
    __extends(TrikGamepadPad, _super);
    function TrikGamepadPad() {
        _super.apply(this, arguments);
    }
    TrikGamepadPad.parentType = VectorSensor;
    TrikGamepadPad.name = "gamepadPad";
    TrikGamepadPad.friendlyName = "Android Gamepad Pad";
    return TrikGamepadPad;
})(VectorSensor);
var TrikGamepadPadPressSensor = (function (_super) {
    __extends(TrikGamepadPadPressSensor, _super);
    function TrikGamepadPadPressSensor() {
        _super.apply(this, arguments);
    }
    TrikGamepadPadPressSensor.parentType = Button;
    TrikGamepadPadPressSensor.name = "gamepadPadPressSensor";
    TrikGamepadPadPressSensor.friendlyName = "Android Gamepad Pad as Button";
    return TrikGamepadPadPressSensor;
})(Button);
var TrikGamepadWheel = (function (_super) {
    __extends(TrikGamepadWheel, _super);
    function TrikGamepadWheel() {
        _super.apply(this, arguments);
    }
    TrikGamepadWheel.parentType = ScalarSensor;
    TrikGamepadWheel.name = "gamepadWheel";
    TrikGamepadWheel.friendlyName = "Android Gamepad Wheel";
    return TrikGamepadWheel;
})(ScalarSensor);
var TrikGamepadConnectionIndicator = (function (_super) {
    __extends(TrikGamepadConnectionIndicator, _super);
    function TrikGamepadConnectionIndicator() {
        _super.apply(this, arguments);
    }
    TrikGamepadConnectionIndicator.parentType = ScalarSensor;
    TrikGamepadConnectionIndicator.name = "gamepadConnectionIndicator";
    TrikGamepadConnectionIndicator.friendlyName = "Android Gamepad Connection Indicator";
    return TrikGamepadConnectionIndicator;
})(ScalarSensor);
var TrikRobotModelBase = (function (_super) {
    __extends(TrikRobotModelBase, _super);
    function TrikRobotModelBase() {
        _super.call(this);
        var analogPortConnections = [this.lightSensorInfo(), this.infraredSensorInfo()];
        this.addAllowedConnection(new PortInfoImpl("DisplayPort", Direction.output), [this.displayInfo()]);
        this.addAllowedConnection(new PortInfoImpl("SpeakerPort", Direction.output), [this.speakerInfo()]);
        this.addAllowedConnection(new PortInfoImpl("Left", Direction.input, [], "buttonLeft"), [this.buttonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("Right", Direction.input, [], "buttonRight"), [this.buttonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("Up", Direction.input, [], "buttonUp"), [this.buttonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("Down", Direction.input, [], "buttonDown"), [this.buttonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("Enter", Direction.input, [], "buttonEnter"), [this.buttonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("Esc", Direction.input, [], "buttonEsc"), [this.buttonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("C1", Direction.output, ["JC1"]), [this.servoMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("C2", Direction.output, ["JC2"]), [this.servoMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("C3", Direction.output, ["JC3"]), [this.servoMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("E1", Direction.output, ["JE1"]), [this.servoMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("E2", Direction.output, ["JE2"]), [this.servoMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("E3", Direction.output, ["JE3"]), [this.servoMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("E4", Direction.output, ["JE4"]), [this.servoMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("M1", Direction.output, ["JM1", "A", "1"]), [this.powerMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("M2", Direction.output, ["JM2", "B", "2"]), [this.powerMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("M3", Direction.output, ["JM3", "C", "3"]), [this.powerMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("M4", Direction.output, ["JM4", "D", "4"]), [this.powerMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("B1", Direction.input, ["JB1", "M1", "JM1", "A", "1"], "encoder1"), [this.encoderInfo()]);
        this.addAllowedConnection(new PortInfoImpl("B2", Direction.input, ["JB2", "M2", "JM2", "B", "2"], "encoder2"), [this.encoderInfo()]);
        this.addAllowedConnection(new PortInfoImpl("B3", Direction.input, ["JB3", "M3", "JM3", "C", "3"], "encoder3"), [this.encoderInfo()]);
        this.addAllowedConnection(new PortInfoImpl("B4", Direction.input, ["JB4", "M4", "JM4", "D", "4"], "encoder4"), [this.encoderInfo()]);
        this.addAllowedConnection(new PortInfoImpl("A1", Direction.input, ["JA1"], "sensorA1"), analogPortConnections);
        this.addAllowedConnection(new PortInfoImpl("A2", Direction.input, ["JA2"], "sensorA2"), analogPortConnections);
        this.addAllowedConnection(new PortInfoImpl("A3", Direction.input, ["JA3"], "sensorA3"), analogPortConnections);
        this.addAllowedConnection(new PortInfoImpl("A4", Direction.input, ["JA4"], "sensorA4"), analogPortConnections);
        this.addAllowedConnection(new PortInfoImpl("A5", Direction.input, ["JA5"], "sensorA5"), analogPortConnections);
        this.addAllowedConnection(new PortInfoImpl("A6", Direction.input, ["JA6"], "sensorA6"), analogPortConnections);
        this.digitalPorts = [
            new PortInfoImpl("D1", Direction.input, ["JD1"], "sensorD1"),
            new PortInfoImpl("D2", Direction.input, ["JD2"], "sensorD2"),
            new PortInfoImpl("F1", Direction.input, ["JF1"], "sensorF1")
        ];
        this.addAllowedConnection(this.digitalPorts[0], [this.sonarSensorInfo()]);
        this.addAllowedConnection(this.digitalPorts[1], [this.sonarSensorInfo()]);
        this.addAllowedConnection(this.digitalPorts[2], [this.motionSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GyroscopePortX", Direction.input, [], "gyroscopeX"), [this.gyroscopeInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GyroscopePortY", Direction.input, [], "gyroscopeY"), [this.gyroscopeInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GyroscopePortZ", Direction.input, [], "gyroscopeZ"), [this.gyroscopeInfo()]);
        this.addAllowedConnection(new PortInfoImpl("AccelerometerPortX", Direction.input, [], "accelerometerX"), [this.accelerometerInfo()]);
        this.addAllowedConnection(new PortInfoImpl("AccelerometerPortY", Direction.input, [], "accelerometerY"), [this.accelerometerInfo()]);
        this.addAllowedConnection(new PortInfoImpl("AccelerometerPortZ", Direction.input, [], "accelerometerZ"), [this.accelerometerInfo()]);
        this.addAllowedConnection(new PortInfoImpl("LedPort", Direction.output), [this.ledInfo()]);
        this.addAllowedConnection(new PortInfoImpl("LineSensorXPort", Direction.input, [], "lineSensorX"), [this.lineSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("LineSensorSizePort", Direction.input, [], "lineSensorSize"), [this.lineSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("LineSensorCrossroadsPort", Direction.input, [], "lineSensorCross"), [this.lineSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("ObjectSensorXPort", Direction.input, [], "objectSensorX"), [this.objectSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("ObjectSensorYPort", Direction.input, [], "objectSensorY"), [this.objectSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("ObjectSensorSizePort", Direction.input, [], "objectSensorSize"), [this.objectSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("ColorSensorRPort", Direction.input, [], "colorSensorR"), [this.colorSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("ColorSensorGPort", Direction.input, [], "colorSensorG"), [this.colorSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("ColorSensorBPort", Direction.input, [], "colorSensorB"), [this.colorSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("ShellPort", Direction.output), [this.shellInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GamepadPad1PosPort", Direction.input, [], "gamepadPad1", ReservedVariableType.vector), [this.gamepadPadInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GamepadPad2PosPort", Direction.input, [], "gamepadPad2", ReservedVariableType.vector), [this.gamepadPadInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GamepadPad1PressedPort", Direction.input, [], "gamepadPad1Pressed"), [this.gamepadPadPressSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GamepadPad2PressedPort", Direction.input, [], "gamepadPad2Pressed"), [this.gamepadPadPressSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GamepadWheelPort", Direction.input, [], "gamepadWheel"), [this.gamepadWheelInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GamepadButton1Port", Direction.input, [], "gamepadButton1"), [this.gamepadButtonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GamepadButton2Port", Direction.input, [], "gamepadButton2"), [this.gamepadButtonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GamepadButton3Port", Direction.input, [], "gamepadButton3"), [this.gamepadButtonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GamepadButton4Port", Direction.input, [], "gamepadButton4"), [this.gamepadButtonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GamepadButton5Port", Direction.input, [], "gamepadButton5"), [this.gamepadButtonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GamepadConnectionIndicatorPort", Direction.input, [], "gamepadConnected"), [this.gamepadConnectionIndicatorInfo()]);
    }
    TrikRobotModelBase.prototype.getConfigurablePorts = function () {
        return _super.prototype.getConfigurablePorts.call(this).concat(this.digitalPorts);
    };
    TrikRobotModelBase.prototype.displayInfo = function () {
        return DeviceInfoImpl.getInstance(Display);
    };
    TrikRobotModelBase.prototype.speakerInfo = function () {
        return DeviceInfoImpl.getInstance(Speaker);
    };
    TrikRobotModelBase.prototype.buttonInfo = function () {
        return DeviceInfoImpl.getInstance(Button);
    };
    TrikRobotModelBase.prototype.powerMotorInfo = function () {
        return DeviceInfoImpl.getInstance(Motor);
    };
    TrikRobotModelBase.prototype.servoMotorInfo = function () {
        return DeviceInfoImpl.getInstance(Motor);
    };
    TrikRobotModelBase.prototype.encoderInfo = function () {
        return DeviceInfoImpl.getInstance(EncoderSensor);
    };
    TrikRobotModelBase.prototype.lightSensorInfo = function () {
        return DeviceInfoImpl.getInstance(LightSensor);
    };
    TrikRobotModelBase.prototype.infraredSensorInfo = function () {
        return DeviceInfoImpl.getInstance(TrikInfraredSensor);
    };
    TrikRobotModelBase.prototype.sonarSensorInfo = function () {
        return DeviceInfoImpl.getInstance(TrikSonarSensor);
    };
    TrikRobotModelBase.prototype.motionSensorInfo = function () {
        return DeviceInfoImpl.getInstance(TrikMotionSensor);
    };
    TrikRobotModelBase.prototype.gyroscopeInfo = function () {
        return DeviceInfoImpl.getInstance(GyroscopeSensor);
    };
    TrikRobotModelBase.prototype.accelerometerInfo = function () {
        return DeviceInfoImpl.getInstance(AccelerometerSensor);
    };
    TrikRobotModelBase.prototype.ledInfo = function () {
        return DeviceInfoImpl.getInstance(TrikLed);
    };
    TrikRobotModelBase.prototype.lineSensorInfo = function () {
        return DeviceInfoImpl.getInstance(TrikLineSensor);
    };
    TrikRobotModelBase.prototype.colorSensorInfo = function () {
        return DeviceInfoImpl.getInstance(TrikColorSensor);
    };
    TrikRobotModelBase.prototype.objectSensorInfo = function () {
        return DeviceInfoImpl.getInstance(TrikObjectSensor);
    };
    TrikRobotModelBase.prototype.shellInfo = function () {
        return DeviceInfoImpl.getInstance(TrikShell);
    };
    TrikRobotModelBase.prototype.gamepadButtonInfo = function () {
        return DeviceInfoImpl.getInstance(TrikGamepadButton);
    };
    TrikRobotModelBase.prototype.gamepadPadInfo = function () {
        return DeviceInfoImpl.getInstance(TrikGamepadPad);
    };
    TrikRobotModelBase.prototype.gamepadPadPressSensorInfo = function () {
        return DeviceInfoImpl.getInstance(TrikGamepadPadPressSensor);
    };
    TrikRobotModelBase.prototype.gamepadWheelInfo = function () {
        return DeviceInfoImpl.getInstance(TrikGamepadWheel);
    };
    TrikRobotModelBase.prototype.gamepadConnectionIndicatorInfo = function () {
        return DeviceInfoImpl.getInstance(TrikGamepadConnectionIndicator);
    };
    return TrikRobotModelBase;
})(CommonRobotModelImpl);
var TrikDisplay = (function (_super) {
    __extends(TrikDisplay, _super);
    function TrikDisplay() {
        _super.apply(this, arguments);
    }
    TrikDisplay.parentType = Display;
    return TrikDisplay;
})(Display);
var TrikPowerMotor = (function (_super) {
    __extends(TrikPowerMotor, _super);
    function TrikPowerMotor() {
        _super.apply(this, arguments);
    }
    TrikPowerMotor.parentType = Motor;
    TrikPowerMotor.name = "power";
    TrikPowerMotor.friendlyName = "Power Motor";
    return TrikPowerMotor;
})(Motor);
var TrikServoMotor = (function (_super) {
    __extends(TrikServoMotor, _super);
    function TrikServoMotor() {
        _super.apply(this, arguments);
    }
    TrikServoMotor.parentType = Motor;
    TrikServoMotor.name = "servo";
    TrikServoMotor.friendlyName = "Servo Motor";
    return TrikServoMotor;
})(Motor);
var TrikSpeaker = (function (_super) {
    __extends(TrikSpeaker, _super);
    function TrikSpeaker() {
        _super.apply(this, arguments);
    }
    TrikSpeaker.parentType = Speaker;
    return TrikSpeaker;
})(Speaker);
var SoundSensor = (function (_super) {
    __extends(SoundSensor, _super);
    function SoundSensor() {
        _super.apply(this, arguments);
    }
    SoundSensor.parentType = ScalarSensor;
    SoundSensor.name = "sound";
    SoundSensor.friendlyName = "Sound sensor";
    return SoundSensor;
})(ScalarSensor);
//# sourceMappingURL=two-d-model-core.js.map