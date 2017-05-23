import {ElementConstructor} from "core/editorCore/model/ElementConstructor";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {NodeType} from "core/editorCore/model/NodeType";
import {Property} from "core/editorCore/model/Property";
import {Lane} from "./Lane";
import {ContainerNodeType} from "core/editorCore/model/ContainerNodeType";
import {Pool} from "./Pool";
import {DefaultSize} from "../../../common/constants/DefaultSize";
export class BpmnElementConstructor extends ElementConstructor {
    public createNode(nodeType: NodeType, x: number, y: number, width: number, height: number,
                      properties: Map<string, Property>, id?: string): DiagramNode {
        if (nodeType.getName() === "lane") {
            let lane: Lane = new Lane(nodeType, x, y, width, height, properties, id);
            return lane;
        } else if (nodeType.getName() === 'pool') {
            var poolNodeType: ContainerNodeType = <ContainerNodeType> this.nodesTypesMap.get("pooltext");
            let pool: Pool = new Pool(poolNodeType, x, y, width, height, poolNodeType.getPropertiesMap(), id);

            let laneNodeType: ContainerNodeType = <ContainerNodeType> this.nodesTypesMap.get("lane");
            let lane: Lane = new Lane(laneNodeType, x + pool.getBBox().width - DefaultSize.DEFAULT_NODE_WIDTH, y,
                width, height, laneNodeType.getPropertiesMap(), id);
            lane.setParentNode(pool, true);

            return pool;
        } else if (nodeType.getName() === 'pooltext') {
            let pool: Pool = new Pool(nodeType, x, y, width, height, properties, id);
            return pool;
        } else
            return super.createNode(nodeType, x, y, width, height, properties, id);
    }
}