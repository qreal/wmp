import {AbstractBlock} from "./AbstractBlock";
import {Link} from "core/editorCore/model/Link";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
export class FinalBlock extends AbstractBlock {

    private EXPECTED_NUMBER_OF_OUTBOUND_LINKS = 0;

    constructor(node: DiagramNode, outboundLinks: Link[]) {
        super(node, outboundLinks);
    }

    public run(): void {
        var output = this.node.getName(); + "\n";
        this.checkExpectedNumberOfOutboundLinks(this.EXPECTED_NUMBER_OF_OUTBOUND_LINKS);
        console.log(output);
    }
    
    public getNextNodeId(): string {
        return null;
    }
    
}