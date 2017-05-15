import {PaletteTree} from "./PaletteTree";
import {NodeType} from "./NodeType";
export class ElementTypes {
    blockTypes: PaletteTree;
    flowTypes: PaletteTree;
    containerTypes: PaletteTree;
    linkPatterns: Map<string, joint.dia.Link>;

    constructor() {
        this.blockTypes = new PaletteTree();
        this.flowTypes = new PaletteTree();
        this.containerTypes = new PaletteTree();
        this.linkPatterns = new Map<string, joint.dia.Link>();
    }
}