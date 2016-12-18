/// <reference path="NodeType.ts" />

class ElementTypes {

    uncategorisedTypes: Map<NodeType>;
    blockTypes: PaletteTree;
    flowTypes: PaletteTree;
    linkPatterns: Map<joint.dia.Link>

    constructor() {
        this.uncategorisedTypes = {};
        this.blockTypes = new PaletteTree();
        this.flowTypes = new PaletteTree();
        this.linkPatterns = {};
    }
}