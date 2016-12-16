/// <reference path="Map.ts" />
/// <reference path="PaletteSubtypes.ts" />

class PaletteTypes {

    categories: Map<Map<PaletteSubtypes>>;

    constructor() {
        this.categories = {};
    }

    convertToMap(): Map<NodeType> {
        var nodesMap: Map<NodeType> = {};
        for (var category in this.categories) {
            for (var typeName in this.categories[category]) {
                $.extend(nodesMap, this.categories[category][typeName].convertToMap());
            }
        }
        return nodesMap;
    }
}