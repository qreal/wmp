import {NodeType} from "../model/NodeType";
import {ElementTypes} from "../model/ElementTypes";
import {BlocksPaletteView} from "../view/BlocksPaletteView";
import {PaletteTree} from "../model/PaletteTree";
import {SubprogramPaletteView} from "../view/SubprogramPaletteView";
import {SubprogramDiagramNode} from "../model/SubprogramDiagramNode";
import {DiagramScene} from "../model/DiagramScene";
export class PaletteController {

    private subprogramsSelector: string = "#subprograms-navigation";
    private blocksSelector: string = "#blocks-navigation";
    private flowsSelector: string = "#flows-navigation";

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
        $("[data-type='" + paper.getCurrentLinkTypeName() + "']").css("border", "2px solid #00ff00");
        $(".flow-element").mousedown(function () {
            paper.setCurrentLinkType($(this).attr("data-type"));
            $(".flow-element").css("border", "");
            $(this).css("border", "2px solid #00ff00");
        });
    }

    public appendSubprogramsPalette(subprogramDiagramNodes: SubprogramDiagramNode[],
                                    nodeTypesMap: Map<String, NodeType>): void {
        var typeName: string = "Subprogram";
        var paletteView: SubprogramPaletteView = new SubprogramPaletteView(subprogramDiagramNodes,
            nodeTypesMap[typeName].getImage());
        this.appendPaletteContent(this.subprogramsSelector, paletteView.getContent());
    }

    public appendBlocksPalette(paletteTypes: PaletteTree): void {
        var paletteView: BlocksPaletteView = new BlocksPaletteView(paletteTypes, "tree-element");
        this.appendPaletteContent(this.blocksSelector, paletteView.getContent());
    }

    public appendFlowsPalette(paletteTypes: PaletteTree): void {
        var paletteView: BlocksPaletteView = new BlocksPaletteView(paletteTypes, "tree-element flow-element");
        this.appendPaletteContent(this.flowsSelector, paletteView.getContent());
    }

    public searchPaletteReload(event: Event, elementTypes: ElementTypes, nodesTypesMap: Map<String, NodeType>) {
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
        this.clearPaletteContent(this.blocksSelector);
        this.clearPaletteContent(this.flowsSelector);

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