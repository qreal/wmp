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

/// <reference path="HtmlView.ts" />
/// <reference path="../controller/VariantListMapper.ts" />
/// <reference path="../model/Property.ts" />
/// <reference path="../../utils/StringUtils.ts" />

class CheckboxPropertyView extends HtmlView {

    private template: string = '' +
        '<tr class="property">' +
        '   <td class="vert-align">{0}</td>' +
        '   <td class="vert-align">' +
        '       <div id="{1}" class="checkbox" data-type="{2}" data-true="{3}" data-false="{4}">' +
        '           <label class="active"><input type="checkbox" {5}>{6}</label>' +
        '       </div>' +
        '   </td>' +
        '</tr>';

    constructor(typeName: string, propertyKey: string, property: Property) {
        super();
        var variantsList: Variant[] = VariantListMapper.getVariantList(typeName, propertyKey);
        var dataTrue: string;
        var dataFalse: string;
        for (var i = 0; i < variantsList.length; i++) {
            if (variantsList[i].getKey() === "true") {
                dataTrue = variantsList[i].getValue();
            }
            if (variantsList[i].getKey() === "false") {
                dataFalse = variantsList[i].getValue();
            }
        }

        var visibleValue: string;
        if (property.value === variantsList[0].getKey()) {
            visibleValue = variantsList[0].getValue();
        } else {
            visibleValue = variantsList[1].getValue();
        }
        var state: string = "";
        if (property.value === "true") {
            state = "checked";
        }

        this.content = StringUtils.format(this.template, property.name, propertyKey + "Property",
            propertyKey, dataTrue, dataFalse, state, visibleValue);
    }

}