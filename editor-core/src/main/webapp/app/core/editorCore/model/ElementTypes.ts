/// <reference path="NodeType.ts" />

class ElementTypes {

    uncategorisedTypes: Map<NodeType>;
    paletteTypes: PaletteTree;

    constructor() {
        this.uncategorisedTypes = {};
        this.paletteTypes = new PaletteTree();
    }
}