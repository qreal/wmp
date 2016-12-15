/// <reference path="Map.ts" />
/// <reference path="NodeType.ts" />

class PaletteSubtypes {

    categories: Map<NodeType[]>;

    constructor() {
        this.categories = new Map<NodeType[]>();
    }

    convertToMap(): Map<NodeType> {
        var nodesMap: Map<NodeType> = new Map<NodeType>();
        for (var category in this.categories) {
            for (var i in this.categories[category]) {
                var nodeType = this.categories[category][i];
                nodesMap[nodeType.getName()] = nodeType;
            }
        }
        return nodesMap;
    }
}