/// <reference path="Map.ts" />
/// <reference path="DiagramNode.ts" />
/// <reference path="Link.ts" />
/// <reference path="SubprogramDiagramNode.ts" />
/// <reference path="../../../vendor.d.ts" />

class DiagramParts {

    nodesMap: Map<DiagramNode>;
    linksMap: Map<Link>;
    subprogramDiagramNodes: SubprogramDiagramNode[] = [];

    constructor(nodesMap?: Map<DiagramNode>, linksMap?: Map<Link>,
                subprogramDiagramNodes?: SubprogramDiagramNode[]) {
        this.nodesMap = nodesMap || new Map<DiagramNode>();
        this.linksMap = linksMap || new Map<Link>();
        this.subprogramDiagramNodes = subprogramDiagramNodes || [];
    }
}