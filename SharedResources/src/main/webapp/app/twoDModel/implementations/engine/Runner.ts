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

/// <reference path="model/DisplayWidget.ts" />
/// <reference path="../../interfaces/engine/items/RobotItem.ts" />

class Runner {

    private timeoutId: number;
    private boost: number = 2;

    run(robotItem: RobotItem, displayWidget: DisplayWidget, result: any): void {
        this.parseReport(result.report);
        displayWidget.displayToFront();
        var trajectory = JSON.parse(result.trace);
        var runner: Runner = this;
        var counter: number = 0;
        runner.timeoutId = setTimeout(function nextPoint() {
            var point: any = trajectory[counter];
            runner.doPointActions(robotItem, displayWidget, point);
            if (counter < trajectory.length - 1) {
                counter++;
                var delay: number = (trajectory[counter].timestamp - point.timestamp) / runner.boost;
                if (!delay) {
                    nextPoint();
                } else {
                    runner.timeoutId = setTimeout(nextPoint, delay);
                }
            } else {
                displayWidget.displayToBack();
                displayWidget.reset();
            }
        }, 0);
    }

    stop(robotItem: RobotItem, displayWidget: DisplayWidget) {
        displayWidget.displayToBack();
        if (this.timeoutId) {
            clearTimeout(this.timeoutId);
            this.timeoutId = undefined;
        }
        robotItem.clearCurrentPosition();
        displayWidget.reset();
    }

    private parseReport(report) {
        var messageText = "";
        var level = report.messages[0].level;
        report.messages.forEach( function(message) {
            messageText += message.message + " ";
        });
        if (level === "info") {
            $("#infoAlert").removeClass("alert-danger");
            $("#infoAlert").addClass("alert-success");
        } else {
            if (level === "error") {
                $("#infoAlert").removeClass("alert-success");
                $("#infoAlert").addClass("alert-danger");
            }
        }
        $("#infoAlert").contents().last()[0].textContent = messageText;
        $("#infoAlert").show();
    }

    private doPointActions(robotItem: RobotItem, displayWidget: DisplayWidget, point): void {
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
        } else {
            robotItem.moveToPoint(point.x, point.y, point.rotation);
        }
    }

    private doDisplayActions(displayWidget: DisplayWidget, point): void {
        switch (point.property) {
            case "smiles":
                if (point.value) {
                    displayWidget.drawSmile();
                } else {
                    displayWidget.clearSmile();
                }
                break;
            case "sadSmiles":
                if (point.value) {
                    displayWidget.drawSadSmile();
                } else {
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
    }

    private doLedActions(displayWidget: DisplayWidget, point): void {
        if (point.property === "color") {
            displayWidget.setLedColor(point.value);
        }
    }

    private doMarkerActions(robotItem: RobotItem, point): void {
        switch (point.property) {
            case "isDown":
                robotItem.setMarkerDown(point.value);
                break;
            case "color":
                robotItem.setMarkerColor(point.value);
                break;
            default:
        }
    }

    private doShellActions(point): void {
        switch (point.property) {
            case "lastPhrase":
                this.say(point.value);
                break;
        }
    }

    private say(value: string) {
        var delay: number = 1000;
        var maxLegnth: number = 90;
        if (value.length > maxLegnth) {
            value = value.substr(0, maxLegnth) + "...";
        }
        $("#sayAlert").contents().last()[0].textContent = value;
        $("#sayAlert").show();
        $("#sayAlert").delay(delay).fadeOut(delay, function() {
            $("#sayAlert").hide();
        });
    }

    private drawObjects(displayWidget: DisplayWidget, objects) {
        displayWidget.redraw();
        for (var i = 0; i < objects.length; i++) {
            var object = objects[i];

            switch (object.type) {
                case "ellipse":
                    displayWidget.drawEllipse(object.x, object.y, object.a, object.b, object.color, object.thickness);
                    break;
                case "arc":
                    displayWidget.drawArc(object.x, object.y, object.a, object.b, object.startAngle, object.spanAngle,
                        object.color, object.thickness);
                    break;
                case "rectangle":
                    displayWidget.drawRectangle(object.x, object.y, object.width, object.height,
                        object.color, object.thickness);
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
    }
}