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
/// <reference path="../model/Property.ts" />
/// <reference path="../../utils/StringUtils.ts" />

class StringPropertyView extends HtmlView {

    private template: string = '' +
        '<tr class="property">' +
        '   <td class="vert-align">{0}</td>' +
        '   <td class="vert-align">' +
        '       <div class="input-group">' +
        '           <input class="{1} property-edit-input form-control" type="text" data-type="{2}" value="{3}">' +
        '       </div>' +
        '   </td>' +
        '</tr>';

    constructor(nodeId: string, propertyKey: string, property: Property) {
        super();
        this.content = StringUtils.format(this.template, property.name, propertyKey + "-" + nodeId,
            propertyKey, property.value);
    }

}