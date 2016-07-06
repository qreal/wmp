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

/// <reference path="RegionItem.ts" />
/// <reference path="../../../../interfaces/engine/model/WorldModel.ts" />
/// <reference path="../../../../types/TwoDPosition.ts" />

class EllipseRegion extends RegionItem {

    constructor(worldModel: WorldModel) {
        super(worldModel);
        this.shape = worldModel.getPaper().ellipse(0, 0, 0, 0);
        this.setColor(this.defaultColor);
        this.setWidht(this.defaultWidth);
        this.setHeight(this.defaultHeight);
        this.shape.toBack();
    }

    setPosition(position: TwoDPosition): void {
        this.shape.attr({cx: position.x + this.getWith(), cy: position.y + this.getHeight()});
    }

    setWidht(width: number): void {
        this.shape.attr({rx: width / 2});
    }

    setHeight(height: number): void {
        this.shape.attr({ry: height / 2});
    }

    getPosition(): TwoDPosition {
        return new TwoDPosition(this.shape.attr("cx"), this.shape.attr("cy"));
    }

    getWith(): number {
        return this.shape.attr("rx");
    }

    getHeight(): number {
        return this.shape.attr("ry");
    }
}