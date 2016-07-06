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

class DropdownPropertyView extends HtmlView {

    private template: string = '' +
        '<tr class="property">' +
        '   <td class="vert-align">{0}</td>' +
        '   <td class="vert-align">' +
        '       <select id="{1}" class="mydropdown" data-type="{2}">' +
        '           {3}' +
        '       </select>' +
        '   </td>' +
        '</tr>';

    constructor(typeName: string, propertyKey: string, property: Property) {
        super();
        var variantsList: Variant[] = VariantListMapper.getVariantList(typeName, propertyKey);
        var options: string = '';
        for (var i = 0; i < variantsList.length; i++) {
            var variant = variantsList[i];
            var selected = "";
            if (variant.getKey() === property.value) {
                selected = 'selected = "selected" ';
            }
            options += '<option ' + selected + 'value="' + variant.getKey() + '">' + variant.getValue() + '</option>';
        }
        this.content = StringUtils.format(this.template, property.name, propertyKey + "Property",
            propertyKey, options);
    }

}