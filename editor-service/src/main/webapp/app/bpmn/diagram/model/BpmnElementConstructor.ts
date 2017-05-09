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
                      properties: Map<String, Property>, id?: string): DiagramNode {
        if (nodeType.getName() === "lane") {
            let lane: Lane = new Lane(nodeType, x, y, width, height, properties, id);
            this.setupEvents(lane);
            return lane;
        } else if (nodeType.getName() === 'pool') {
            var poolNodeType: ContainerNodeType = <ContainerNodeType> this.nodesTypesMap["pooltext"];
            let pool: Pool = new Pool(poolNodeType, x, y, width, height, poolNodeType.getPropertiesMap(), id);
            this.setupEvents(pool);

            let laneNodeType: ContainerNodeType = <ContainerNodeType> this.nodesTypesMap["lane"];
            let lane: Lane = new Lane(laneNodeType, x + pool.getBBox().width - DefaultSize.DEFAULT_NODE_WIDTH, y,
                width, height, laneNodeType.getPropertiesMap(), id);
            this.setupEvents(lane);
            lane.setParentNode(pool, true);

            return pool;
        } else if (nodeType.getName() === 'pooltext') {
            let pool: Pool = new Pool(nodeType, x, y, width, height, properties, id);
            this.setupEvents(pool);
            return pool;
        } else
            return super.createNode(nodeType, x, y, width, height, properties, id);
    }

    private setupEvents(node: DiagramNode) {
        node.getJointObject().on("change:parent", (laneModel: joint.shapes.basic.Generic, parentId: string) =>
            this.onParentChange(laneModel, parentId));
        node.getJointObject().on("change:position", (laneModel: joint.shapes.basic.Generic) =>
            this.onMove(laneModel));
    }

    private onParentChange(model: joint.shapes.basic.Generic, parentId: string) {
        let node: DiagramNode = this.scene.getNodeById(model.id);
        let parent: Pool = this.scene.getNodesMap()[parentId];
        if (parent)
            parent.addBPMNChild(node);
    }

    private onMove(model: joint.shapes.basic.Generic) {
        let node: DiagramNode = this.scene.getNodeById(model.id);
        if (node.getParentNode() && !node.getJointObject().get("parent"))
            (<Pool> node.getParentNode()).removeBPMNChild(node);
    }
}