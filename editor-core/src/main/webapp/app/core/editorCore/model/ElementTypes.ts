/// <reference path="NodeType.ts" />
/// <reference path="PaletteTypes.ts" />

class ElementTypes {

    uncategorisedTypes: Map<NodeType>;
    paletteTypes: PaletteTypes;

    constructor() {
        this.uncategorisedTypes = {};
        this.paletteTypes = new PaletteTypes();
    }
}