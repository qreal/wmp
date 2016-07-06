/*
 * Copyright Vladimir Zakharov
 * Copyright Semen Yuriev
 * Copyright Artemii Bezguzikov
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

/// <reference path="../../model/Gesture.ts" />
/// <reference path="../../../constants/GeneralConstants.d.ts" />
/// <reference path="../../../utils/XmlHttpFactory.ts" />
/// <reference path="../../../vendor.d.ts" />

class GesturesController {

    private paperController: PaperController;
    private paper: DiagramPaper;
    private timer: number;
    private currentTime: number;
    private date: Date;
    private flagAdd: boolean;
    private flagDraw: boolean;
    private pointList: GesturesUtils.Pair[];
    private gesturesMatcher: GesturesMatcher;
    private rightButtonDown;

    constructor(paperController: PaperController, paper: DiagramPaper) {
        this.paperController = paperController;
        this.paper = paper;
        this.date = new Date();
        this.flagDraw = false;
        this.rightButtonDown = false;
        this.pointList = [];
        this.loadGestures();
    }

    public startDrawing(): void {
        var n = this.date.getTime();
        this.currentTime = n;
        this.flagAdd = false;
        clearTimeout(this.timer);
        this.flagDraw = true;
    }

    public onMouseMove(event): void {
        if (!(this.rightButtonDown)) {
            return;
        }

        if (this.flagDraw === false) {
            return;
        }

        var offsetX = (event.pageX - $("#" + this.paper.getId()).offset().left +
            $("#" + this.paper.getId()).scrollLeft());
        var offsetY = (event.pageY - $("#" + this.paper.getId()).offset().top +
            $("#" + this.paper.getId()).scrollTop());

        var pair: GesturesUtils.Pair = new GesturesUtils.Pair(offsetX, offsetY);
        if (this.flagAdd) {

            var currentPair = this.pointList[this.pointList.length - 1];
            var n = this.date.getTime();
            var diff = n - this.currentTime;
            this.currentTime = n;
            pair = this.smoothing(currentPair, new GesturesUtils.Pair(offsetX, offsetY), diff);

            $("#" + this.paper.getId()).line(currentPair.first, currentPair.second, pair.first, pair.second);
        }
        this.flagAdd = true;
        this.pointList.push(pair);
    }

    public onMouseDown(event): void {
        if (event.button === 2) {
            this.rightButtonDown = true;
        }
    }

    public onMouseUp(event): void {
        this.rightButtonDown = false;
        if (this.flagDraw === false) {
            return;
        }
        this.flagDraw = false;
        if (this.paperController.getCurrentElement()) {
            this.finishDraw(event);
        } else {
            this.timer = setTimeout(() => this.finishDraw(event), 1000);
        }
    }

    private finishDraw(event): void {
        if (this.flagDraw === true)
            return;
        var pencil = document.getElementsByClassName('pencil');
        for (var i = pencil.length; i > 0; i--) {
            pencil[i - 1].parentNode.removeChild(pencil[i - 1]);
        }

        var currentElement: DiagramElement = this.paperController.getCurrentElement();

        if (currentElement) {
            this.paperController.createLinkBetweenCurrentAndEventTargetElements(event);
        } else {
            var names: string[] = this.gesturesMatcher.getMatches(this.pointList);
            this.paperController.createNodeInEventPositionFromNames(names, event);
        }
        this.pointList = [];
    }

    private loadGestures(): void {
        var url = GeneralConstants.APP_ROOT_PATH + "resources/gestures.json";
        this.downloadGesturesData(url, this.handleGesturesData.bind(this));
    }

    private downloadGesturesData(url, callback): void {
        var xhr = XmlHttpFactory.createXMLHTTPObject();
        xhr.open('GET', url, true);
        xhr.onreadystatechange = (e) => {
            if (xhr.readyState == 4) {
                if (xhr.status == 200) {
                    var gestures: Gesture[] = callback(xhr);
                    this.gesturesMatcher = new GesturesMatcher(gestures);
                } else {
                    alert("Can't load gestures data");
                }
            }
        }
        xhr.send();
    }

    private handleGesturesData(xhr): Gesture[] {
        var fileData = JSON.parse(xhr.responseText);
        var gestureList = [];
        for (var i = 0; i < fileData.length; i++) {
            gestureList.push(new Gesture(<string> fileData[i].name, <string[]> fileData[i].key,
                <number> fileData[i].factor));
        }
        return gestureList;
    }

    private smoothing(pair1 : GesturesUtils.Pair, pair2 : GesturesUtils.Pair, diff : number) {
        var c = 0.0275; // 'c' is empirical constants
        var b = Math.exp(-c * diff);
        return new GesturesUtils.Pair(pair2.first * b + (1 - b) * pair1.first, pair2.second + (1 - b) * pair1.second);
    }

}

