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

/// <reference path="../model/Property.ts" />
/// <reference path="../view/HtmlView.ts" />
/// <reference path="../view/StringPropertyView.ts" />
/// <reference path="../view/CheckboxPropertyView.ts" />
/// <reference path="../view/DropdownPropertyView.ts" />
/// <reference path="../view/SpinnerPropertyView.ts" />
/// <reference path="../../vendor.d.ts" />

class PropertyViewFactory {

    public createView(nodeId: string, typeName: string, propertyKey: string, property: Property): HtmlView {
        switch (property.type) {
            case "string":
            case "combobox":
                return new StringPropertyView(nodeId, propertyKey, property);
            case "checkbox":
                return new CheckboxPropertyView(typeName, propertyKey, property);
            case "dropdown":
                return new DropdownPropertyView(typeName, propertyKey, property)
            case "spinner":
                return new SpinnerPropertyView(propertyKey, property);
        }
    }
}