/// <reference path="Map.ts" />
/// <reference path="NodeType.ts" />

class PaletteTypes {

    categories: Map<Map<NodeType>>;

    constructor() {
        this.categories = new Map<Map<NodeType>>();
    }
}