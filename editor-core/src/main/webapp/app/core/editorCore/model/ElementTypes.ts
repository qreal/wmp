import {PaletteTree} from "./PaletteTree";
export class ElementTypes {
    blockTypes: PaletteTree;
    flowTypes: PaletteTree;
    linkPatterns: Map<string, joint.dia.Link>;

    constructor() {
        this.blockTypes = new PaletteTree();
        this.flowTypes = new PaletteTree();
        this.linkPatterns = new Map<string, joint.dia.Link>();
    }
}