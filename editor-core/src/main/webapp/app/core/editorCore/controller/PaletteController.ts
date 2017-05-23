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
    private paper: DiagramScene;
    private elementTypes: ElementTypes;
    private nodesTypesMap: Map<string, NodeType>;

    public init(paper: DiagramScene, elementTypes: ElementTypes, nodesTypesMap: Map<string, NodeType>) {
        this.paper = paper;
        this.elementTypes = elementTypes;
        this.nodesTypesMap = nodesTypesMap;
        this.reload();
    }

    public reload() {
        this.clearPaletteContent(this.blocksSelector);
        this.clearPaletteContent(this.flowsSelector);

        this.appendBlocksPalette(this.elementTypes.blockTypes);
        this.appendFlowsPalette(this.elementTypes.flowTypes);
        this.initClick();
        this.initDraggable();
    }

    public searchPaletteReload(event: Event) {
        var searchPatterns: string[] = (<any> event.target).value.split(" ").map((str) => str.toLowerCase());

        for (var [name, nodeType] of this.nodesTypesMap) {
            var notFound: boolean;
            for (var i in searchPatterns) {
                notFound = name.indexOf(searchPatterns[i]) == -1;
                if (notFound)
                    break;
            }
            nodeType.setSearchVisibility(!notFound);
        }
        this.reload();
    }

    private initDraggable(): void {
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

    private initClick(): void {
        $("[data-type='" + this.paper.getCurrentLinkTypeName() + "']").css("border", "2px solid #00ff00");
        var paper: DiagramScene = this.paper;
        $(".flow-element").mousedown(function () {
            paper.setCurrentLinkType($(this).attr("data-type"));
            $(".flow-element").css("border", "");
            $(this).css("border", "2px solid #00ff00");
        });
    }

    public appendSubprogramsPalette(subprogramDiagramNodes: SubprogramDiagramNode[],
                                    nodeTypesMap: Map<string, NodeType>): void {
        var typeName: string = "Subprogram";
        var paletteView: SubprogramPaletteView = new SubprogramPaletteView(subprogramDiagramNodes,
            nodeTypesMap.get(typeName).getImage());
        this.appendPaletteContent(this.subprogramsSelector, paletteView.getContent());
    }

    private appendBlocksPalette(paletteTypes: PaletteTree): void {
        var paletteView: BlocksPaletteView = new BlocksPaletteView(paletteTypes, "tree-element");
        this.appendPaletteContent(this.blocksSelector, paletteView.getContent());
    }

    private appendFlowsPalette(paletteTypes: PaletteTree): void {
        var paletteView: BlocksPaletteView = new BlocksPaletteView(paletteTypes, "tree-element flow-element");
        this.appendPaletteContent(this.flowsSelector, paletteView.getContent());
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