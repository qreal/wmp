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

/// <reference path="Map.ts" />
/// <reference path="Property.ts" />
/// <reference path="../../vendor.d.ts" />

class PropertyEditElement {

    private static propertyTemplate = "" +
        "<span>{3}:</span> " +
        "<input class='{0} property-edit-input' data-id='{1}' data-type='{2}' " +
        "style='border: dashed 1px; padding-left: 2px; margin-bottom: 1px' value='{4}'>" +
        "<br>";

    private static template: string = "" +
        "<div class='property-edit-element' style='position: absolute; text-align: left; z-index: 1;'>" +
        "   {0}" +
        "</div>";

    private htmlElement;

    constructor(logicalId: string, jointObjectId: string, properties: Map<Property>) {
        var propertiesHtml: string = "";

        for (var propertyKey in properties) {
            var property: Property = properties[propertyKey];
            if (property.type === "string") {
                propertiesHtml += StringUtils.format(PropertyEditElement.propertyTemplate,
                    propertyKey + "-" + logicalId, jointObjectId, propertyKey, property.name, property.value);
                break;
            }
        }

        this.htmlElement = $(StringUtils.format(PropertyEditElement.template, propertiesHtml));
        this.initInputSize();
        this.initInputAutosize();
    }

    public getHtmlElement() {
        return this.htmlElement;
    }

    public setPosition(x: number, y: number): void {
        this.htmlElement.css({ left: x - 25, top: y + 55 });
    }

    private initInputSize(): void {
        this.htmlElement.find('input').each(function(index) {
                $(this).css("width", StringUtils.getInputStringSize(this));
            }
        );

    }

    private initInputAutosize(): void {
        this.htmlElement.find('input').on('input', function(event) {
            $(this).trigger('autosize');
        });

        this.htmlElement.find('input').on('autosize', function(event) {
            $(this).css("width", StringUtils.getInputStringSize(this));
        });
    }

}