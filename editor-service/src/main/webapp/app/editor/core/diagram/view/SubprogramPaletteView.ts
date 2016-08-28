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
/// <reference path="SubprogramPaletteElementView.ts" />
/// <reference path="../model/SubprogramDiagramNode.ts" />

class SubprogramPaletteView extends HtmlView {

    constructor(subprogramDiagramNodes: SubprogramDiagramNode[], subprogramImageSrc: string) {
        super();
        var typeName: string = "Subprogram";

        for (var i in subprogramDiagramNodes) {
            var node: SubprogramDiagramNode = subprogramDiagramNodes[i];
            var nodeView: SubprogramPaletteElementView = new SubprogramPaletteElementView(typeName, node.getName(),
                subprogramImageSrc, node.getLogicalId());
            this.content += nodeView.getContent();
        }
    }
}