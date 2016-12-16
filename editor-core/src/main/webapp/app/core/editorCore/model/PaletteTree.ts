/// <reference path="Map.ts" />
/// <reference path="NodeType.ts" />

class PaletteTree {

    categories: Map<PaletteTree>;
    nodes: NodeType[];
    isShown: boolean;

    constructor() {
        this.categories = {};
        this.nodes = [];
        this.isShown = true;
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