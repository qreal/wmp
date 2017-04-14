import {DiagramNode} from "./DiagramNode";
import {DefaultDiagramNode} from "./DefaultDiagramNode";
import {DiagramContainer} from "./DiagramContainer";
import {Link} from "./Link";
import {Property} from "./Property";
import {ContainerNodeType} from "./ContainerNodeType";
import {NodeType} from "./NodeType";
export class ElementConstructor {

    public createLink(): Link {
        return null;
    }

    public createNode(nodeType: NodeType, x: number, y: number, width: number, height: number,
                      properties: Map<String, Property>, id?: string): DiagramNode {
        var name: string = nodeType.getShownName();
        var type: string = nodeType.getName();
        var imagePath: string = nodeType.getImage();
        if (nodeType instanceof ContainerNodeType)
            return new DiagramContainer(name, type, x, y, width, height, properties, imagePath, id);
        else
            return new DefaultDiagramNode(name, type, x, y, width, height, properties, imagePath, id);
    }
}