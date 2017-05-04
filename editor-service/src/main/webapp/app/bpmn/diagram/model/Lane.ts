import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {Pool} from "./Pool";
import {DiagramContainer} from "core/editorCore/model/DiagramContainer";
import {PropertiesPack} from "core/editorCore/model/PropertiesPack";
import {Property} from "core/editorCore/model/Property";
import {DefaultSize} from "common/constants/DefaultSize";
import {NodeType} from "core/editorCore/model/NodeType";
export class Lane extends DiagramContainer {

    private minWidth: number;
    private minHeight: number;

    constructor(nodeType: NodeType, x: number, y: number, width: number, height: number,
                properties: Map<String, Property>, id?: string) {
        var minWidth = DefaultSize.DEFAULT_NODE_WIDTH * 8;
        var minHeight = DefaultSize.DEFAULT_NODE_HEIGHT * 2;
        super(nodeType, x, y, Math.max(width, minWidth), Math.max(height, minHeight), properties, id);
        this.minWidth = minWidth;
        this.minHeight = minHeight;

        this.getJointObject().on("change:size", (laneModel: joint.shapes.basic.Generic) => {
            var parent: Pool = this.getParentNode();
            if (parent)
                parent.updateWidth(laneModel.getBBox().width);
        });
    }

    public getParentNode(): Pool {
        return (<Pool> super.getParentNode());
    }

    public isValidEmbedding(child: DiagramNode) {
        return !(child instanceof Lane) && !(child instanceof Pool);
    }
}