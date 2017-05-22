import {DefaultDiagramNode} from "./DefaultDiagramNode";
import {PropertiesPack} from "./PropertiesPack";
import {Property} from "./Property";
import {DiagramNode} from "./DiagramNode";
import {NodeType} from "./NodeType";
export class DiagramContainer extends DefaultDiagramNode {

    protected childrenNodes: Set<DiagramNode>;

    constructor(nodeType: NodeType, x: number, y: number, width: number, height: number,
                properties: Map<string, Property>, id?: string, notDefaultConstProperties?: PropertiesPack) {
        super(nodeType, x, y, width, height, properties, id, notDefaultConstProperties);
        this.childrenNodes = new Set<DiagramNode>();
    }

    public getChildrenNodes(): Set<DiagramNode> {
        return this.childrenNodes;
    }

    public addChild(node: DiagramNode) {
        if (this.childrenNodes.has(node))
            return;
        this.childrenNodes.add(node);
        node.setParentNode(this);
    }

    public removeChild(node: DiagramNode) {
        if (!this.childrenNodes.has(node))
            return;
        this.childrenNodes.delete(node);
        node.setParentNode(null);
    }

    public isValidEmbedding(child: DiagramNode): boolean {
        return true;
    }
}
