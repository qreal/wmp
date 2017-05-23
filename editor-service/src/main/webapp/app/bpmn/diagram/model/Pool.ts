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
                      properties: Map<string, Property>, id?: string) {
        var minWidth: number = DefaultSize.DEFAULT_NODE_WIDTH * 2;
        var minHeight: number = DefaultSize.DEFAULT_NODE_HEIGHT * 2;
        super(nodeType, x, y, Math.max(width, minWidth), Math.max(height, minHeight), properties, id);
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.isUpdating = false;
    }

    public addChild(node: DiagramNode) {
        super.addChild(node);
        this.update();
    }

    public removeChild(node: DiagramNode) {
        super.removeChild(node);
        this.update();
    }

    public update(width?: number) {

        if (this.isUpdating)
            return;
        this.isUpdating = true;

        var currentPool: Pool = this;
        var newWidth: number = width;
        while (currentPool.getParentNode()) {
            if (width)
                newWidth += currentPool.getWidth() - DefaultSize.DEFAULT_NODE_WIDTH;
            currentPool = currentPool.getParentNode();
        }
        if (currentPool === this) {
            currentPool.updateHeight();
            currentPool.updateWidth(newWidth);
        } else
            currentPool.update(newWidth);

        this.isUpdating = false;
    }

    public getParentNode(): Pool {
        return (<Pool> super.getParentNode());
    }

    public isValidEmbedding(child: DiagramNode) {
        return child instanceof Lane || child instanceof Pool;
    }

    private updateHeight() {
        var sumHeight: number = 0;
        var orderedChildren: DiagramContainer[] = this.rearrangeChildren();

        this.childrenNodes.forEach((child: DiagramContainer) => {
            if (child instanceof Pool)
                child.updateHeight();
            sumHeight += child.getHeight();
        });
        sumHeight = Math.max(sumHeight, this.minHeight);
        this.setSize(this.getWidth(), sumHeight);

        var curHeight: number = this.getY();
        var leftBorder: number = this.getX() + this.getWidth() - DefaultSize.DEFAULT_NODE_WIDTH;
        for (var i in orderedChildren) {
            var child: DiagramNode = orderedChildren[i];

            var diffX: number = leftBorder - child.getX();
            var diffY: number = curHeight - child.getY();
            if (diffX || diffY)
                child.getJointObject().translate(diffX, diffY);
            curHeight += child.getHeight();
        }
    }


    private rearrangeChildren(): DiagramContainer[] {
        var children: DiagramContainer[] = [];
        this.childrenNodes.forEach((child: DiagramContainer) => children.push(child));
        children.sort((a: DiagramContainer, b: DiagramContainer) => {
            return a.getY() + a.getHeight() - b.getY() - b.getHeight();
        });

        return children;
    }

    private updateWidth(width?: number) {
        var lanes: Lane[] = this.getLanes();
        var rightBorder: number = 0;
        if (width)
            rightBorder = width + this.getX() + this.getWidth() - DefaultSize.DEFAULT_NODE_WIDTH;
        else {
            lanes.forEach((lane: Lane) => rightBorder = Math.max(rightBorder, lane.getX() + lane.getWidth()));
        }

        lanes.forEach((lane: Lane) => lane.setSize(rightBorder - lane.getX(), lane.getHeight()));
    }

    private getLanes(): Lane[] {
        var lanes: Lane[] = [];

        this.childrenNodes.forEach((child: DiagramContainer) => {
            if (child instanceof Pool)
                lanes = lanes.concat(child.getLanes());
            if (child instanceof Lane)
                lanes.push(child);
        });

        return lanes;
    }
}