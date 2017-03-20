import {DefaultDiagramNode} from "./DefaultDiagramNode";
import {PropertiesPack} from "./PropertiesPack";
import {Property} from "./Property";
export class DiagramContainer extends DefaultDiagramNode {
    constructor(name: string, type: string, x: number, y: number, width: number, height: number,
                properties: Map<String, Property>, imagePath: string, id?: string,
                notDefaultConstProperties?: PropertiesPack) {
        super(name, type, x, y, width, height, properties, imagePath, id, notDefaultConstProperties);
    }
}