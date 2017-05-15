import {SubprogramDiagramNode} from "./SubprogramDiagramNode";
import {Link} from "./Link";
import {DiagramNode} from "./DiagramNode";
export class DiagramParts {

    nodesMap: Map<string, DiagramNode>;
    linksMap: Map<string, Link>;
    subprogramDiagramNodes: SubprogramDiagramNode[] = [];

    constructor(nodesMap?: Map<string, DiagramNode>, linksMap?: Map<string, Link>,
                subprogramDiagramNodes?: SubprogramDiagramNode[]) {
        this.nodesMap = nodesMap || new Map<string, DiagramNode>();
        this.linksMap = linksMap || new Map<string, Link>();
        this.subprogramDiagramNodes = subprogramDiagramNodes || [];
    }
}