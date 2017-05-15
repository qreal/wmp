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
    protected nodesTypesMap: Map<string, NodeType>;

    constructor(scene: DiagramScene, nodesTypesMap?: Map<string, NodeType>) {
        this.scene = scene;
        this.nodesTypesMap = nodesTypesMap;
    }

    public createLink(jointObject: joint.dia.Link, name: string, type: string, properties: Map<string, Property>): Link {
        return new Link(jointObject, name, type, properties);
    }

    public createNode(nodeType: NodeType, x: number, y: number, width: number, height: number,
                      properties: Map<string, Property>, id?: string): DiagramNode {
        var node: DiagramNode;
        if (nodeType instanceof ContainerNodeType)
            node = new DiagramContainer(nodeType, x, y, width, height, properties, id);
        else
            node = new DefaultDiagramNode(nodeType, x, y, width, height, properties, id);
        var nodesMap: Map<string, DiagramNode> = this.scene.getNodesMap();
        node.getJointObject().on("change:parent", (laneModel: joint.shapes.basic.Generic, parentId: string) => {
            if (parent)
                node.setParentNode(<DiagramContainer> nodesMap.get(parentId));
        });
        node.getJointObject().on("change:z", (laneModel: joint.shapes.basic.Generic, newZ: number) => {
            var links: Link[] = this.scene.getConnectedLinkObjects(nodesMap.get(laneModel.id));
            links.forEach((link: Link) => {
                link.getJointObject().set("z", Math.max(link.getJointObject().get("z"), newZ));
            })
        });
        return node;
    }
}