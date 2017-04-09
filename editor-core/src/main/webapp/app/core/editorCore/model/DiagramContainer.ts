import {DefaultDiagramNode} from "./DefaultDiagramNode";
import {PropertiesPack} from "./PropertiesPack";
import {Property} from "./Property";
import {DiagramNode} from "./DiagramNode";
export class DiagramContainer extends DefaultDiagramNode {

    private childrenNodes: Set<DiagramNode>;

    constructor(name: string, type: string, x: number, y: number, width: number, height: number,
                properties: Map<String, Property>, imagePath: string, id?: string,
                notDefaultConstProperties?: PropertiesPack) {
        super(name, type, x, y, width, height, properties, imagePath, id, notDefaultConstProperties);
        this.childrenNodes = new Set<DiagramNode>();
        this.getJointObject().on("change:embeds", function(a, b) {
            console.log(a);
            console.log(b);
        })
    }

    public getChildrenNodes(): Set<DiagramNode> {
        return this.childrenNodes;
    }

    public addChild(node: DiagramNode) {
        this.childrenNodes.add(node);
    }

    public removeChild(node: DiagramNode) {
        this.childrenNodes.delete(node);
    }
}