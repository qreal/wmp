import {DiagramContainer} from "core/editorCore/model/DiagramContainer";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {Lane} from "./Lane";
import {Property} from "core/editorCore/model/Property";
import {DefaultSize} from "../../../common/constants/DefaultSize";
import {NodeType} from "core/editorCore/model/NodeType";
import sortElements = joint.util.sortElements;
export class Pool extends DiagramContainer {

    private minWidth: number;
    private minHeight: number;
    private isUpdating: boolean;

    constructor(nodeType: NodeType, x: number, y: number, width: number, height: number,
                      properties: Map<String, Property>, id?: string) {
        var minWidth: number = DefaultSize.DEFAULT_NODE_WIDTH * 2;
        var minHeight: number = DefaultSize.DEFAULT_NODE_HEIGHT * 2;
        super(nodeType, x, y, Math.max(width, minWidth), Math.max(height, minHeight), properties, id);
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.isUpdating = false;
    }

    public addBPMNChild(node: DiagramNode) {
        node.setParentNode(this);
        this.update();
    }

    public removeBPMNChild(node: DiagramNode) {
        node.setParentNode(null);
        this.update();
    }

    public update(width?: number) {
        var children: DiagramContainer[] = [];
        this.childrenNodes.forEach((child: DiagramContainer) => children.push(child));
        children.sort((a: DiagramContainer, b: DiagramContainer) => {
            return a.getY() + a.getHeight() - b.getY() - b.getHeight();
        })
        var sumHeight: number = 0;
        var newWidth: number = 0;
        for (var i in children) {
            sumHeight += children[i].getHeight();
            newWidth = Math.max(newWidth, children[i].getWidth());
        }
        sumHeight = Math.max(sumHeight, this.minHeight);
        newWidth = width ? width : newWidth;
        this.resize(this.getBBox().width, sumHeight);

        var curHeight: number = this.getY();
        var leftBorder: number = this.getX() + this.getBBox().width - DefaultSize.DEFAULT_NODE_WIDTH;
        for (var i in children) {
            var child: DiagramNode = children[i];

            var diffX: number = leftBorder - child.getX();
            var diffY: number = curHeight - child.getY();
            if (diffX || diffY)
                child.getJointObject().translate(diffX, diffY);
            curHeight += child.getHeight();

            if (child.getWidth() === newWidth)
                continue;

            if (child instanceof Lane) {
                child.resize(newWidth, child.getHeight());
            } else if (child instanceof Pool) {
                (<Pool> child).update(newWidth - this.getBBox().width + DefaultSize.DEFAULT_NODE_WIDTH);
            }
        }
    }

    public getParentNode(): Pool {
        return (<Pool> super.getParentNode());
    }

    public updateWidth(width: number) {
        if (this.isUpdating)
            return;
        this.isUpdating = true;
        var currentPool: Pool = this;
        var newWidth: number = width;
        while (currentPool.getParentNode()) {
            newWidth += currentPool.getBBox().width - DefaultSize.DEFAULT_NODE_WIDTH;
            currentPool.update();
            currentPool = currentPool.getParentNode();
        }
        currentPool.update(newWidth);
        this.isUpdating = false;
    }

    public getWidth(): number {
        return this.getBBox().width - DefaultSize.DEFAULT_NODE_WIDTH +
            (this.childrenNodes.size ? this.childrenNodes.values().next().value.getWidth() : 0);
    }
    public isValidEmbedding(child: DiagramNode) {
        return child instanceof Lane || child instanceof Pool;
    }
}