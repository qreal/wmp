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

/// <reference path="DefaultDiagramNode.ts" />
/// <reference path="Map.ts" />
/// <reference path="Property.ts" />
/// <reference path="PropertiesPack.ts" />
/// <reference path="../../vendor.d.ts" />

class SubprogramNode extends DefaultDiagramNode {

    private subprogramDiagramId: string;
    private textObject: joint.shapes.basic.Text;

    constructor(name: string, type: string, x: number, y: number, properties: Map<Property>, imagePath: string,
                subprogramDiagramId: string, id?: string, notDefaultConstProperties?: PropertiesPack) {
        super(name, type, x, y, properties, imagePath, id, notDefaultConstProperties);
        this.subprogramDiagramId = subprogramDiagramId;

        var fontSize: number = 16;
        var width: number = (0.5 * name.length) * fontSize;
        var height: number = (name.split('\n').length) * fontSize;
        this.textObject = new  joint.shapes.basic.Text({
            position: { x: x - 10, y: y - 20 },
            size: { width: width, height: height },
            attrs: {
                text: {
                    text: name,
                    style: {'pointer-events':'none'}
                },
            },
        });
    }

    getSubprogramDiagramId(): string {
        return this.subprogramDiagramId;
    }

    getTextObject(): joint.shapes.basic.Text {
        return this.textObject;
    }

    setPosition(x: number, y: number, zoom: number): void {
        super.setPosition(x, y, zoom);
        this.textObject.position(x - 10, y - 20);
    }

}