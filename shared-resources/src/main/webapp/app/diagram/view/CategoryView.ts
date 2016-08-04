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
/// <reference path="PaletteElementView.ts" />
/// <reference path="../model/Map.ts" />
/// <reference path="../model/NodeType.ts" />
/// <reference path="../../utils/StringUtils.ts" />

class CategoryView extends HtmlView {

    private template: string = '' +
        '<li>' +
        '   <p>{0}</p>' +
        '   <ul>{1}</ul>' +
        '</li>';

    constructor(categoryName: string, category: Map<NodeType>) {
        super();
        var elementsContent: string = '';
        for (var typeName in category) {
            var nodeType: NodeType = category[typeName];
            var nodeName: string = nodeType.getName();
            var paletteElementView: PaletteElementView = new PaletteElementView(typeName, nodeName, nodeType.getImage());
            elementsContent += paletteElementView.getContent();
        }
        this.content = StringUtils.format(this.template, categoryName, elementsContent);
    }

}