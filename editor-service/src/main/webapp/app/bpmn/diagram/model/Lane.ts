import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {Pool} from "./Pool";
import {DiagramContainer} from "core/editorCore/model/DiagramContainer";
import {PropertiesPack} from "core/editorCore/model/PropertiesPack";
import {Property} from "core/editorCore/model/Property";
export class Lane extends DiagramContainer {

    constructor(name: string, type: string, x: number, y: number, width: number, height: number,
                properties: Map<String, Property>, imagePath: string, border: any, id?: string,
                notDefaultConstProperties?: PropertiesPack) {
        var widthScale = 8;
        var heightScale = 2;
        super(name, type, x, y, width * widthScale, height * heightScale, properties,
            imagePath, border, id, notDefaultConstProperties);
    }

    public isValidEmbedding(child: DiagramNode) {
        return true;
    }
}