import {ElementConstructor} from "core/editorCore/model/ElementConstructor";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {NodeType} from "core/editorCore/model/NodeType";
import {Property} from "core/editorCore/model/Property";
import {Lane} from "./Lane";
import {ContainerNodeType} from "core/editorCore/model/ContainerNodeType";
import {Pool} from "./Pool";
import {DiagramContainer} from "core/editorCore/model/DiagramContainer";
export class BpmnElementConstructor extends ElementConstructor {
    public createNode(nodeType: NodeType, x: number, y: number, width: number, height: number,
                      properties: Map<String, Property>, id?: string): DiagramNode {

        if (nodeType.getName() === "lane") {
            let lane: Lane = new Lane(nodeType.getShownName(), nodeType.getName(), x, y, width, height, properties,
                nodeType.getImage(), (<ContainerNodeType> nodeType).getBorder(), id);
            lane.getJointObject().on("change:parent", (laneModel: joint.shapes.basic.Generic, parentId: string) =>
                this.onParentChange(laneModel, parentId));
            return lane;

        } else if (nodeType.getName() === 'pool') {

            var poolNodeType: ContainerNodeType = <ContainerNodeType> this.nodesTypesMap["pooltext"];
            let pool: Pool = new Pool(poolNodeType.getShownName(), poolNodeType.getName(), x, y, width, height, properties,
                poolNodeType.getImage(), poolNodeType.getBorder(), id);

            let laneNodeType: ContainerNodeType = <ContainerNodeType> this.nodesTypesMap["lane"];
            let lane: Lane = new Lane(laneNodeType.getShownName(), laneNodeType.getName(), x + width, y, width, height, properties,
                laneNodeType.getImage(), laneNodeType.getBorder(), id);
            lane.setParentNode(pool, true);

            lane.getJointObject().on("change:parent", (laneModel: joint.shapes.basic.Generic, parentId: string) =>
                this.onParentChange(laneModel, parentId));

            return pool;

        } else if (nodeType.getName() === 'pooltext') {
            let pool: Pool = new Pool(nodeType.getShownName(), nodeType.getName(), x, y, width, height, properties,
                nodeType.getImage(), (<ContainerNodeType> nodeType).getBorder(), id);
            return pool;
        } else
            return super.createNode(nodeType, x, y, width, height, properties, id);
    }

    private onParentChange(laneModel: joint.shapes.basic.Generic, parentId: string) {

        let lane: Lane = this.nodesMap[laneModel.id];
        let parent: DiagramContainer = this.nodesMap[parentId];

        let oldParent: DiagramContainer = lane.getParentNode();
        if (oldParent instanceof Pool) {
            oldParent.removeLane(lane);
            lane.setParentNode(oldParent);
        }

        if (parent instanceof Pool)
            parent.addLane(lane);
        else if (parent instanceof Lane)
            (<Pool> parent.getParentNode()).addLane(lane);
        else if (parent)
            lane.setParentNode(parent);
    }
}