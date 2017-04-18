import {DiagramContainer} from "core/editorCore/model/DiagramContainer";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {Lane} from "./Lane";
import {PropertiesPack} from "core/editorCore/model/PropertiesPack";
import {Property} from "core/editorCore/model/Property";
import {ContainerNodeType} from "core/editorCore/model/ContainerNodeType";
import {NodeType} from "core/editorCore/model/NodeType";
export class Pool extends DiagramContainer {

    constructor(name: string, type: string, x: number, y: number, width: number, height: number, properties: Map<String, Property>,
                imagePath: string, border: any, id?: string, notDefaultConstProperties?: PropertiesPack) {
        var heightScale = 4;
        super(name, type, x, y, width, height * heightScale, properties, imagePath, border, id, notDefaultConstProperties);
    }

    public addLane(lane: Lane) {
        var diffHeight = lane.getJointObject().getBBox().height;
        var bBox = this.getJointObject().getBBox();
        this.getJointObject().resize(bBox.width, bBox.height + diffHeight);
        lane.setParentNode(this);
    }

    public removeLane(lane: Lane) {
        var diffHeight = lane.getJointObject().getBBox().height;
        var bBox = this.getJointObject().getBBox();
        this.getJointObject().resize(bBox.width, bBox.height - diffHeight);
        lane.setParentNode(null);
    }

    public isValidEmbedding(child: DiagramNode) {
        return child instanceof Lane || child instanceof Pool;
    }
}