/// <reference path="NodeType.ts" />
/// <reference path="PaletteTypes.ts" />

class ElementTypes {

    uncategorisedTypes: Map<NodeType>;
    paletteTypes: PaletteTypes;

    constructor() {
        this.uncategorisedTypes = new Map<NodeType>();
        this.paletteTypes = new PaletteTypes();
    }
}