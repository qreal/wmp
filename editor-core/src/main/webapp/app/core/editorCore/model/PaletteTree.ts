import {NodeType} from "./NodeType";
import {Map} from "./Map";
export class PaletteTree {

    categories: Map<PaletteTree>;
    nodes: NodeType[];

    constructor() {
        this.categories = {};
        this.nodes = [];
    }

    convertToMap(): Map<NodeType> {
        var nodesMap: Map<NodeType> = {};
        for (var category in this.categories)
            $.extend(nodesMap, this.categories[category].convertToMap());
        for (var i in this.nodes)
            nodesMap[this.nodes[i].getName()] = this.nodes[i];
        return nodesMap;
    }
}