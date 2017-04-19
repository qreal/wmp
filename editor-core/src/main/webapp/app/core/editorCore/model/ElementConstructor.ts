import {DiagramNode} from "./DiagramNode";
import {DefaultDiagramNode} from "./DefaultDiagramNode";
import {DiagramContainer} from "./DiagramContainer";
import {Link} from "./Link";
import {Property} from "./Property";
import {ContainerNodeType} from "./ContainerNodeType";
import {NodeType} from "./NodeType";
export class ElementConstructor {

    protected nodesMap: Map<String, DiagramNode>;
    protected nodesTypesMap: Map<String, NodeType>

    constructor(nodesMap?: Map<String, DiagramNode>, nodesTypesMap?: Map<String, NodeType>) {
        this.nodesMap = nodesMap;
        this.nodesTypesMap = nodesTypesMap;
    }

    public createNode(nodeType: NodeType, x: number, y: number, width: number, height: number,
                      properties: Map<String, Property>, id?: string): DiagramNode {
        var name: string = nodeType.getShownName();
        var type: string = nodeType.getName();
        var imagePath: string = nodeType.getImage();
        var node: DiagramNode;
        if (nodeType instanceof ContainerNodeType)
            node = new DiagramContainer(name, type, x, y, width, height, properties, imagePath, nodeType.getBorder(), id);
        else
            node = new DefaultDiagramNode(name, type, x, y, width, height, properties, imagePath, id);
        var nodesMap: Map<String, DiagramNode> = this.nodesMap;
        node.getJointObject().on("change:parent", (laneModel: joint.shapes.basic.Generic, parentId: string) => {
            if (parent)
                node.setParentNode(nodesMap[parentId]);
        })
        return node;
    }
}