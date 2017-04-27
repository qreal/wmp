import {ElementConstructor} from "core/editorCore/model/ElementConstructor";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {NodeType} from "core/editorCore/model/NodeType";
import {Property} from "core/editorCore/model/Property";
import {Lane} from "./Lane";
import {ContainerNodeType} from "core/editorCore/model/ContainerNodeType";
import {Pool} from "./Pool";
import {DiagramContainer} from "core/editorCore/model/DiagramContainer";
import {DefaultSize} from "../../../common/constants/DefaultSize";
export class BpmnElementConstructor extends ElementConstructor {
    public createNode(nodeType: NodeType, x: number, y: number, width: number, height: number,
                      properties: Map<String, Property>, id?: string): DiagramNode {

        if (nodeType.getName() === "lane") {
            let lane: Lane = new Lane(nodeType.getShownName(), nodeType.getName(), x, y, width, height, properties,
                nodeType.getImage(), (<ContainerNodeType> nodeType).getBorder(), id);
            this.setupLane(lane);
            return lane;

        } else if (nodeType.getName() === 'pool') {

            var poolNodeType: ContainerNodeType = <ContainerNodeType> this.nodesTypesMap["pooltext"];
            let pool: Pool = new Pool(poolNodeType.getShownName(), poolNodeType.getName(), x, y, width, height, properties,
                poolNodeType.getImage(), poolNodeType.getBorder(), id);

            let laneNodeType: ContainerNodeType = <ContainerNodeType> this.nodesTypesMap["lane"];
            let lane: Lane = new Lane(laneNodeType.getShownName(), laneNodeType.getName(), x + pool.getBBox().width
                - DefaultSize.DEFAULT_NODE_WIDTH, y, width, height, properties, laneNodeType.getImage(), laneNodeType.getBorder(), id);
            this.setupLane(lane);
            lane.setParentNode(pool, true);

            return pool;

        } else if (nodeType.getName() === 'pooltext') {
            let pool: Pool = new Pool(nodeType.getShownName(), nodeType.getName(), x, y, width, height, properties,
                nodeType.getImage(), (<ContainerNodeType> nodeType).getBorder(), id);
            return pool;
        } else
            return super.createNode(nodeType, x, y, width, height, properties, id);
    }

    private setupLane(lane: Lane) {
        lane.getJointObject().on("change:parent", (laneModel: joint.shapes.basic.Generic, parentId: string) =>
            this.onParentChange(laneModel, parentId));
        lane.getJointObject().on("change:position", (laneModel: joint.shapes.basic.Generic) =>
            this.onLaneMove(laneModel));
    }

    private onParentChange(laneModel: joint.shapes.basic.Generic, parentId: string) {

        let lane: Lane = this.nodesMap[laneModel.id];
        let parent: Pool = this.nodesMap[parentId];
        if (parent)
            parent.addLane(lane);
    }

    private onLaneMove(laneModel: joint.shapes.basic.Generic) {
        let lane: Lane = this.nodesMap[laneModel.id];
        if (lane.getParentNode() && !lane.getJointObject().get("parent"))
            lane.getParentNode().removeLane(lane);
    }
}