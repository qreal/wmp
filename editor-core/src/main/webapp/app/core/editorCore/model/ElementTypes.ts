/// <reference path="NodeType.ts" />

class ElementTypes {

    blockTypes: PaletteTree;
    flowTypes: PaletteTree;
    containerTypes: PaletteTree;
    linkPatterns: Map<joint.dia.Link>;

    constructor() {
        this.blockTypes = new PaletteTree();
        this.flowTypes = new PaletteTree();
        this.containerTypes = new PaletteTree();
        this.linkPatterns = {};
    }
}