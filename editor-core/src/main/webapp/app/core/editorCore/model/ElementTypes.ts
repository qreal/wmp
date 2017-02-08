import {PaletteTree} from "./PaletteTree";
//import {Map} from "./Map";
import {NodeType} from "./NodeType";
export class ElementTypes {

    uncategorisedTypes: Map<String, NodeType>;
    blockTypes: PaletteTree;
    flowTypes: PaletteTree;
    linkPatterns: Map<String, joint.dia.Link>;

    constructor() {
        this.uncategorisedTypes = new Map<String, NodeType>();
        this.blockTypes = new PaletteTree();
        this.flowTypes = new PaletteTree();
        this.linkPatterns = new Map<String, joint.dia.Link>();
    }
}