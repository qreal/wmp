/// <reference path="NodeType.ts" />

class ElementTypes {

    uncategorisedTypes: Map<NodeType>;
    blockTypes: PaletteTree;
    flowTypes: PaletteTree;

    constructor() {
        this.uncategorisedTypes = {};
        this.blockTypes = new PaletteTree();
        this.flowTypes = new PaletteTree();
    }
}