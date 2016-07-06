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

/// <reference path="../../../../interfaces/engine/model/WorldModel.ts" />
/// <reference path="../../../../types/TwoDPosition.ts" />
/// <reference path="../../../../../utils/ColorUtils.ts" />
/// <reference path="../../../../../vendor.d.ts" />

class RegionItem {
    protected shape: RaphaelElement;
    protected defaultColor: string = "#87CEFA";
    protected defaultWidth: number = 200;
    protected defaultHeight: number = 200;
    private worldModel: WorldModel;
    private text : RaphaelElement;

    constructor(worldModel: WorldModel) {
        this.worldModel = worldModel;
    }

    setWidht(width: number): void {
        this.shape.attr({width: width});
    }

    setHeight(height: number): void {
        this.shape.attr({height: height});
    }

    setColor(color: string): void {
        this.shape.attr({stroke: color});
    }

    setOpacity(alpha: number): void {
        this.shape.attr({"stroke-opacity": alpha});
    }

    setPosition(position: TwoDPosition): void {
        this.shape.attr({x: position.x, y: position.y});
    }

    setVisible(visible: boolean): void {
        visible ? this.shape.show() : this.shape.hide();
    }

    isVisible(): boolean {
        return $(this.shape.node).css("display") !== "none";
    }

    getPosition(): TwoDPosition {
        return new TwoDPosition(this.shape.attr("x"), this.shape.attr("y"));
    }

    getWith(): number {
        return this.shape.attr("width");
    }

    getHeight(): number {
        return this.shape.attr("height");
    }

    remove(): void {
        if (this.text) {
            this.text.remove();
        }
        this.shape.remove();
    }

    private deserializePoint(element, xAttr, yAttr, offsetX: number, offsetY: number): TwoDPosition {
        var x = parseFloat(xAttr);
        var y = parseFloat(yAttr);

        if (x !== undefined && y !== undefined) {
            return new TwoDPosition(x + offsetX, y + offsetY);
        }

        return new TwoDPosition(0, 0);
    }

    deserialize(element, offsetX: number, offsetY: number) {
        var color = element.getAttribute("color");
        if (color) {
            var rgbaColor: RGBAColor = ColorUtils.getRBGAColor(color);
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
        if (textX  !== undefined && textY !== undefined) {
            //TODO: set text pos
        }

        var text = element.getAttribute("text");
        if (text) {
            var textPos = this.getPosition();
            // hack for right text position
            setTimeout(() => {
                this.text = this.worldModel.getPaper().text(textPos.x + 5, textPos.y + 10, text).
                    attr({"text-anchor": "start", fill: color, "font-size": 14}).toBack();
            });
        }
    }
}