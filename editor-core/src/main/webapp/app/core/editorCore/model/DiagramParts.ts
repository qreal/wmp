import {SubprogramDiagramNode} from "./SubprogramDiagramNode";
import {Link} from "./Link";
import {DiagramNode} from "./DiagramNode";
export class DiagramParts {

    nodesMap: Map<String, DiagramNode>;
    linksMap: Map<String, Link>;
    subprogramDiagramNodes: SubprogramDiagramNode[] = [];

    constructor(nodesMap?: Map<String, DiagramNode>, linksMap?: Map<String, Link>,
                subprogramDiagramNodes?: SubprogramDiagramNode[]) {
        this.nodesMap = nodesMap || new Map<String, DiagramNode>();
        this.linksMap = linksMap || new Map<String, Link>();
        this.subprogramDiagramNodes = subprogramDiagramNodes || [];
    }
}