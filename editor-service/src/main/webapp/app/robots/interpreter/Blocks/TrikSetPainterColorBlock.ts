import {Interpreter} from "../Interpreter";
import {AbstractBlock} from "./AbstractBlock";
import {Link} from "core/editorCore/model/Link";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
export class TrikSetPainterColorBlock extends AbstractBlock {

    private interpreter: Interpreter;
    private EXPECTED_NUMBER_OF_OUTBOUND_LINKS = 1;

    constructor(node: DiagramNode, outboundLinks: Link[], interpreter: Interpreter) {
        super(node, outboundLinks);
        this.interpreter = interpreter;
    }

    public run(): void {
        var output = this.node.getName(); + "\n";
        this.checkExpectedNumberOfOutboundLinks(this.EXPECTED_NUMBER_OF_OUTBOUND_LINKS);
        
        var color: string = this.node.getChangeableProperties().get("Color").value;
        this.interpreter.addOrChangeEnvironmentVariable("painterColor", color);
        
        console.log(output);
    }

    public getNextNodeId(): string {
        return this.outboundLinks[0].getJointObject().get('target').id;
    }
        
}