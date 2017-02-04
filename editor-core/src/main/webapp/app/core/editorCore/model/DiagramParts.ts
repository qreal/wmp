import {SubprogramDiagramNode} from "./SubprogramDiagramNode";
import {Link} from "./Link";
import {Map} from "./Map";
import {DiagramNode} from "./DiagramNode";
export class DiagramParts {

    nodesMap: Map<DiagramNode>;
    linksMap: Map<Link>;
    subprogramDiagramNodes: SubprogramDiagramNode[] = [];

    constructor(nodesMap?: Map<DiagramNode>, linksMap?: Map<Link>,
                subprogramDiagramNodes?: SubprogramDiagramNode[]) {
        this.nodesMap = nodesMap || {};
        this.linksMap = linksMap || {};
        this.subprogramDiagramNodes = subprogramDiagramNodes || [];
    }
}