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

/// <reference path="../../model/ElementTypes.ts" />
/// <reference path="../parsers/TypesParser.ts" />
/// <reference path="../../../constants/GeneralConstants.d.ts" />
/// <reference path="../../../vendor.d.ts" />

class ElementsTypeLoader {

    load(callback: (elementTypes: ElementTypes) => void, kit?: string, task?: string): void {
        var typesParser: TypesParser = new TypesParser();

        $.ajax({
            type: 'POST',
            url: 'getTypes/' + ((task) ? task : ""),
            dataType: 'json',
            data: {
                'kit': ((kit) ? kit : GeneralConstants.DEFAULT_KIT)
            },
            success: (response) => {
                callback(typesParser.parse(response));
            },
            error: function (response, status, error) {
                alert("Element types loading error: " + status + " " + error);
            }
        });
    }

}