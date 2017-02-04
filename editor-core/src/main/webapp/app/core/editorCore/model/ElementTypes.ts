import {PaletteTree} from "./PaletteTree";
import {Map} from "./Map";
import {NodeType} from "./NodeType";
export class ElementTypes {

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