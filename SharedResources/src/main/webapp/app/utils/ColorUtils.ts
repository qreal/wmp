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

/// <reference path="RGBAColor.ts" />

class ColorUtils {

    public static getRBGAColor(color: string): RGBAColor {
        if (color.length == 9) {
            var rgb: string = "#" + color.substr(3, color.length - 3);
            var alpha: number = this.alphaHexTo0To1(color.substr(1, 2));
            return new RGBAColor(alpha, rgb);
        } else {
            return new RGBAColor(1, color);
        }
    }

    public static alphaHexTo0To1(alpha: string): number {
        var decAlpha: number = parseInt(alpha, 16);
        return decAlpha / 255;
    }
}