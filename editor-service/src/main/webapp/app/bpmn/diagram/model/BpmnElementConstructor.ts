import {ElementConstructor} from "core/editorCore/model/ElementConstructor";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {NodeType} from "core/editorCore/model/NodeType";
import {Property} from "core/editorCore/model/Property";
import {Lane} from "./Lane";
export class BpmnElementConstructor extends ElementConstructor {
    public createNode(nodeType: NodeType, x: number, y: number, width: number, height: number,
                      properties: Map<String, Property>, id?: string): DiagramNode {
        if (nodeType.getName() === "lane")
            return new Lane(nodeType.getShownName(), nodeType.getName(), x, y, width, height, properties, nodeType.getImage(), id);
        else
            return super.createNode(nodeType, x, y, width, height, properties, id);
    }
}