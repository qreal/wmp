import {PropertiesPack} from "./PropertiesPack";
import {Property} from "./Property";
import {DefaultDiagramNode} from "./DefaultDiagramNode";
import {NodeType} from "./NodeType";
export class SubprogramNode extends DefaultDiagramNode {

    private subprogramDiagramId: string;
    private textObject: joint.shapes.basic.Text;

    constructor(nodeType: NodeType, x: number, y: number, width: number, height: number, properties: Map<string, Property>,
                subprogramDiagramId: string, id?: string, notDefaultConstProperties?: PropertiesPack) {
        super(nodeType, x, y, width, height, properties, id, notDefaultConstProperties);
        this.subprogramDiagramId = subprogramDiagramId;

        var fontSize: number = 16;
        var name: string = nodeType.getShownName();
        var width: number = (0.5 * name.length) * fontSize;
        var height: number = (name.split('\n').length) * fontSize;
        this.textObject = new  joint.shapes.basic.Text({
            position: { x: x - 10, y: y - 20 },
            size: { width: width, height: height },
            attrs: {
                text: {
                    text: name,
                    style: {'pointer-events':'none'}
                },
            },
        });
    }

    getSubprogramDiagramId(): string {
        return this.subprogramDiagramId;
    }

    getTextObject(): joint.shapes.basic.Text {
        return this.textObject;
    }

    setPosition(x: number, y: number, zoom: number, cellView: joint.dia.CellView): void {
        super.setPosition(x, y, zoom, cellView);
        this.textObject.position(x - 10, y - 20);
    }

}