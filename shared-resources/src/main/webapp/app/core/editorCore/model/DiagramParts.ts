/// <reference path="Map.ts" />
/// <reference path="DiagramNode.ts" />
/// <reference path="Link.ts" />
/// <reference path="RobotsDiagramNode.ts" />
/// <reference path="SubprogramDiagramNode.ts" />
/// <reference path="../../../vendor.d.ts" />

class DiagramParts {

    nodesMap: Map<DiagramNode>;
    linksMap: Map<Link>;
    robotsDiagramNode: RobotsDiagramNode;
    subprogramDiagramNodes: SubprogramDiagramNode[] = [];

    constructor(nodesMap?: Map<DiagramNode>, linksMap?: Map<Link>, robotsDiagramNode?: RobotsDiagramNode,
                subprogramDiagramNodes?: SubprogramDiagramNode[]) {
        this.nodesMap = nodesMap || {};
        this.linksMap = linksMap || {};
        this.robotsDiagramNode = robotsDiagramNode;
        this.subprogramDiagramNodes = subprogramDiagramNodes || [];
    }
}