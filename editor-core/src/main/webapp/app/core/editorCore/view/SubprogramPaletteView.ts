/// <reference path="HtmlView.ts" />
/// <reference path="SubprogramPaletteElementView.ts" />
/// <reference path="../../../core/editorCore/model/SubprogramDiagramNode.ts" />

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