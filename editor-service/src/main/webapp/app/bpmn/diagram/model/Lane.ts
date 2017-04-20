import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {Pool} from "./Pool";
import {DiagramContainer} from "core/editorCore/model/DiagramContainer";
import {PropertiesPack} from "core/editorCore/model/PropertiesPack";
import {Property} from "core/editorCore/model/Property";
import {DefaultSize} from "common/constants/DefaultSize";
export class Lane extends DiagramContainer {

    private minWidth: number;
    private minHeight: number;

    constructor(name: string, type: string, x: number, y: number, width: number, height: number,
                properties: Map<String, Property>, imagePath: string, border: any, id?: string,
                notDefaultConstProperties?: PropertiesPack) {
        var minWidth = DefaultSize.DEFAULT_NODE_WIDTH * 8;
        var minHeight = DefaultSize.DEFAULT_NODE_HEIGHT * 2;
        super(name, type, x, y, Math.max(width, minWidth), Math.max(height, minHeight), properties, imagePath,
            border, id, notDefaultConstProperties);
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    public isValidEmbedding(child: DiagramNode) {
        return !(child instanceof Lane) && !(child instanceof Pool);
    }
}