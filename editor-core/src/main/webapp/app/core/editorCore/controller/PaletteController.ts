/// <reference path="../model/SubprogramDiagramNode.ts" />
/// <reference path="../model/NodeType.ts" />
/// <reference path="../model/Map.ts" />
/// <reference path="../view/SubprogramPaletteView.ts" />
/// <reference path="../view/BlocksPaletteView.ts" />
/// <reference path="../../../vendor.d.ts" />

class PaletteController {

    public initDraggable(): void {
        $(".tree-element").draggable({
            helper: function () {
                var clone =  $(this).find('.element-img').clone();
                clone.css('position','fixed');
                clone.css('z-index', '1000');
                return clone;
            },
            cursorAt: {
                top: 15,
                left: 15
            },
            revert: "invalid"
        });
    }

    public initClick(paper: DiagramScene): void {
        $(".flow-element").click(function () {
            paper.setCurrentLinkType($(this).attr("data-type"));
        });
    }

    public appendSubprogramsPalette(subprogramDiagramNodes: SubprogramDiagramNode[],
                                    nodeTypesMap: Map<NodeType>): void {
        var typeName: string = "Subprogram";
        var paletteView: SubprogramPaletteView = new SubprogramPaletteView(subprogramDiagramNodes,
            nodeTypesMap[typeName].getImage());
        this.appendPaletteContent("#subprograms-navigation", paletteView.getContent());
    }

    public appendBlocksPalette(paletteTypes: PaletteTree): void {
        var paletteView: BlocksPaletteView = new BlocksPaletteView(paletteTypes, "tree-element");
        this.appendPaletteContent("#blocks-navigation", paletteView.getContent());
    }

    public appendFlowsPalette(paletteTypes: PaletteTree): void {
        var paletteView: BlocksPaletteView = new BlocksPaletteView(paletteTypes, "flow-element");
        this.appendPaletteContent("#flows-navigation", paletteView.getContent());
    }

    public clearBlocksPalette(): void {
        $("#blocks-navigation").empty();
    }

    public searchPaletteReload(event: Event, elementTypes: ElementTypes, nodesTypesMap: Map<NodeType>) {
        var searchPatterns: string[] = (<any> event.target).value.split(" ").map((str) => str.toLowerCase());

        for (var name in nodesTypesMap) {
            var notFound: Boolean = false;
            for (var i in searchPatterns) {
                notFound = name.indexOf(searchPatterns[i]) == -1;
                if (notFound)
                    break;
            }
            nodesTypesMap[name].setVisibility(!notFound);
        }
        this.clearPaletteContent("#blocks-navigation");
        this.clearPaletteContent("#flows-navigation");

        this.appendBlocksPalette(elementTypes.blockTypes);
        this.appendFlowsPalette(elementTypes.flowTypes);
    }

    private appendPaletteContent(selector: string, content: string): void {
        $(selector).append(content);

        $(selector).treeview({
            persist: "location"
        });
    }

    private clearPaletteContent(selector: string): void {
        $(selector).empty();
    }

}