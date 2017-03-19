import {PaletteTree} from "./PaletteTree";
import {NodeType} from "./NodeType";
export class ElementTypes {
    blockTypes: PaletteTree;
    flowTypes: PaletteTree;
    containerTypes: PaletteTree;
    linkPatterns: Map<String, joint.dia.Link>;

    constructor() {
        this.blockTypes = new PaletteTree();
        this.flowTypes = new PaletteTree();
        this.containerTypes = new PaletteTree();
        this.linkPatterns = new Map<String, joint.dia.Link>();
    }
}