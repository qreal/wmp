/*
 * Copyright Semen Yuriev
 * Copyright Artemii Bezguzikov
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

/// <reference path="GesturesUtils.ts" />
/// <reference path="../../model/Gesture.ts" />

class GesturesMatcher {

    private prevKey: number;
    private gestureMatrixSize = 9;
    private gestures: Gesture[];

    constructor(gestures: Gesture[]) {
        this.gestures = gestures;
    }

    public getMatches(pointList: GesturesUtils.Pair[]): string[] {
        var gestureSizes: GesturesUtils.Pair = this.getGestureSizes(pointList);
        var key = this.makeKey(pointList, gestureSizes.first, gestureSizes.second);
        for (var i = 0; i < this.gestures.length; i++) {
            var curr = this.gestures[i];
            this.prevKey = i - 1;
            var curRes = this.gestureDistance(this.gestures[i].key, key) / Math.min(this.gestures[i].key.length, key.length);

            while (this.prevKey >= 0 && this.gestureDistance(this.gestures[this.prevKey].key, key) /
            Math.min(this.gestures[this.prevKey].key.length, key.length) > curRes) {
                this.gestures[this.prevKey + 1] = this.gestures[this.prevKey];
                this.gestures[this.prevKey] = curr;
                this.prevKey--;
            }
        }
        this.prevKey = 0;
        while (this.prevKey < this.gestures.length)
        {
            var factor = this.gestureDistance(this.gestures[this.prevKey].key, key) /
                Math.min(this.gestures[this.prevKey].key.length, key.length);
            if (factor > this.gestures[this.prevKey].factor) {
                break;
            }

            this.prevKey++;
        }

        var names: string[] = [];
        for (var i = 0; i < this.prevKey; ++i) {
            names[i] = this.gestures[i].name;
        }

        return names;
    }

    private getGestureSizes(pointList: GesturesUtils.Pair[]): GesturesUtils.Pair {
        var width: number = 0;
        var height: number = 0;

        if (pointList.length === 0) {
            pointList[0] = new GesturesUtils.Pair(0, 0);
        }
        var minX = pointList[0].first;
        var minY = pointList[0].second;

        for (var i = 1; i < pointList.length; i++) {
            if (pointList[i].first < minX) {
                minX = pointList[i].first;
            }
            if (pointList[i].second < minY) {
                minY = pointList[i].second;
            }
        }

        for (var i = 0; i < pointList.length; i++) {
            pointList[i].first -= minX;
            pointList[i].second -= minY;
            if (pointList[i].first + 1 > width) {
                width = pointList[i].first + 1;
            }
            if (pointList[i].second + 1 > height) {
                height = pointList[i].second + 1;
            }
        }
        var maxX = 0;
        var maxY = 0;
        if (width > height) {
            maxY = this.gestureMatrixSize - 1;
            var ratio = this.gestureMatrixSize / height;
            for (var i = 0; i < pointList.length; i++) {
                pointList[i].first *= ratio;
                pointList[i].second *= ratio;
                if (pointList[i].first > maxX) {
                    maxX = pointList[i].first;
                }
            }
        } else {
            maxX = this.gestureMatrixSize - 1;
            var ratio = this.gestureMatrixSize / width;
            for (var i = 0; i < pointList.length; i++) {
                pointList[i].first *= ratio;
                pointList[i].second *= ratio;
                if (pointList[i].second > maxY) {
                    maxY = pointList[i].second;
                }
            }
        }
        width = maxX + 1;
        height = maxY + 1;
        return new GesturesUtils.Pair(width, height);
    }

    private makeKey(pointList: GesturesUtils.Pair[], width: number, height: number) {
        var key = [];
        var index = 0;
        var firstCell = this.getSymbol(pointList[0], width, height);
        key[index] = firstCell;
        index++;
        for (var i = 1; i < pointList.length; i++) {
            var secondCell = this.getSymbol(pointList[i], width, height);
            if (secondCell != firstCell) {
                firstCell = secondCell;
                key[index] = firstCell;
                index++;
            }
        }
        key.sort();
        for (var i = key.length - 2; i >= 0; i--) {
            if (key[i] === key[i + 1]) {
                key.splice(i, 1);
            }
        }
        return key;
    }

    private getSymbol(pair: GesturesUtils.Pair, width: number, height: number) {
        var columnNames = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'];
        return columnNames[Math.floor(pair.first * this.gestureMatrixSize / width)] +
            (Math.floor(pair.second * this.gestureMatrixSize / height));
    }

    // Calculate  distance between gestures s1 and s2
    private gestureDistance(s1, s2) {
        var ans = 0;

        for (var i = 0; i < s1.length; i++) {
            var minDist = 1000;
            for (var j = 0; j < s2.length; j++) {
                var d1 = Math.abs(s1[i].charCodeAt(0) - s2[j].charCodeAt(0));
                var d2 = Math.abs(s1[i][1] - s2[j][1]);
                if (d1 + d2 < minDist)
                    minDist = d1 + d2;
            }
            ans += minDist;
        }

        for (var i = 0; i < s2.length; i++) {
            var minDist = 1000;
            for (var j = 0; j < s1.length; j++) {
                var d1 = Math.abs(s2[i].charCodeAt(0) - s1[j].charCodeAt(0));
                var d2 = Math.abs(s2[i][1] - s1[j][1]);
                if (d1 + d2 < minDist)
                    minDist = d1 + d2;
            }
            ans += minDist;
        }
        return ans / 2;
    }
}