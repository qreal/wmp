import {DiagramContainer} from "core/editorCore/model/DiagramContainer";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {Lane} from "./Lane";
import {PropertiesPack} from "core/editorCore/model/PropertiesPack";
import {Property} from "core/editorCore/model/Property";
import {DefaultSize} from "../../../common/constants/DefaultSize";
import sortElements = joint.util.sortElements;
export class Pool extends DiagramContainer {

    private minWidth: number;
    private minHeight: number;

    constructor(name: string, type: string, x: number, y: number, width: number, height: number, properties: Map<String, Property>,
                imagePath: string, border: any, id?: string, notDefaultConstProperties?: PropertiesPack) {
        var minWidth = DefaultSize.DEFAULT_NODE_WIDTH * 2;
        var minHeight = DefaultSize.DEFAULT_NODE_HEIGHT * 2;
        super(name, type, x, y, Math.max(width, minWidth), Math.max(height, minHeight), properties, imagePath,
            border, id, notDefaultConstProperties);
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    public addLane(lane: Lane) {
        lane.setParentNode(this);
        this.update();
    }

    public removeLane(lane: Lane) {
        lane.setParentNode(null);
        this.update();
    }

    public update() {
        var lanes: Lane[] = []
        this.childrenNodes.forEach((lane: Lane) => lanes.push(lane));
        lanes.sort((a: Lane, b: Lane) => {
            return a.getBBox().y + a.getBBox().height - b.getBBox().y - b.getBBox().height;
        })
        var sumHeight = 0;
        var maxWidth = 0;
        for (var i in lanes) {
            sumHeight += lanes[i].getBBox().height;
            maxWidth = Math.max(maxWidth, lanes[i].getBBox().width);
        }
        sumHeight = Math.max(sumHeight, this.minHeight);
        this.resize(this.getBBox().width, sumHeight);

        var curHeight = this.getBBox().y;
        var leftBorder = this.getBBox().x + this.getBBox().width - DefaultSize.DEFAULT_NODE_WIDTH;
        for (var i in lanes) {
            lanes[i].getJointObject().translate(leftBorder - lanes[i].getBBox().x, curHeight - lanes[i].getBBox().y);
            lanes[i].resize(maxWidth, lanes[i].getBBox().height);
            curHeight += lanes[i].getBBox().height;
        }
    }

    public updateWidth(newWidth: number) {
        this.childrenNodes.forEach((lane: Lane) => lane.resize(newWidth, lane.getBBox().height));
        this.update();
    }

    public isValidEmbedding(child: DiagramNode) {
        return child instanceof Lane;
    }
}