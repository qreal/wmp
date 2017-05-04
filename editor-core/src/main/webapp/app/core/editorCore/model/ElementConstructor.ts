import {DiagramNode} from "./DiagramNode";
import {DefaultDiagramNode} from "./DefaultDiagramNode";
import {DiagramContainer} from "./DiagramContainer";
import {Link} from "./Link";
import {Property} from "./Property";
import {ContainerNodeType} from "./ContainerNodeType";
import {NodeType} from "./NodeType";
import {DiagramScene} from "./DiagramScene";
export class ElementConstructor {

    protected scene: DiagramScene;
    protected nodesTypesMap: Map<String, NodeType>;

    constructor(scene: DiagramScene, nodesTypesMap?: Map<String, NodeType>) {
        this.scene = scene;
        this.nodesTypesMap = nodesTypesMap;
    }

    public createNode(nodeType: NodeType, x: number, y: number, width: number, height: number,
                      properties: Map<String, Property>, id?: string): DiagramNode {
        var node: DiagramNode;
        if (nodeType instanceof ContainerNodeType)
            node = new DiagramContainer(nodeType, x, y, width, height, properties, id);
        else
            node = new DefaultDiagramNode(nodeType, x, y, width, height, properties, id);
        var nodesMap: Map<String, DiagramNode> = this.scene.getNodesMap();
        node.getJointObject().on("change:parent", (laneModel: joint.shapes.basic.Generic, parentId: string) => {
            if (parent)
                node.setParentNode(nodesMap[parentId]);
        })
        node.getJointObject().on("change:z", (laneModel: joint.shapes.basic.Generic, newZ: number) => {
            var links: Link[] = this.scene.getConnectedLinkObjects(nodesMap[laneModel.id]);
            links.forEach((link: Link) => {
                link.getJointObject().set("z", Math.max(link.getJointObject().get("z"), newZ));
            })
        })
        return node;
    }
}