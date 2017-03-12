import {NodeType} from "./NodeType";
export class PaletteTree {

    categories: Map<String, PaletteTree>;
    nodes: NodeType[];

    constructor() {
        this.categories = new Map<String, PaletteTree>();
        this.nodes = [];
    }

    convertToMap(): Map<String, NodeType> {
        var nodesMap: Map<String, NodeType> = new Map<String, NodeType>();
        for (var category in this.categories)
            $.extend(nodesMap, this.categories[category].convertToMap());
        for (var i in this.nodes)
            nodesMap[this.nodes[i].getName()] = this.nodes[i];
        return nodesMap;
    }
}