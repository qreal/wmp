/// <reference path="Map.ts" />
/// <reference path="PaletteSubtypes.ts" />

class PaletteTypes {

    categories: Map<Map<PaletteSubtypes>>;

    constructor() {
        this.categories = new Map<Map<PaletteSubtypes>>();
    }

    convertToMap(): Map<NodeType> {
        var nodesMap: Map<NodeType> = new Map<NodeType>();
        for (var category in this.categories) {
            for (var typeName in this.categories[category]) {
                Map.unite(nodesMap, this.categories[category][typeName].convertToMap());
            }
        }
        return nodesMap;
    }
}