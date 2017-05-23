import {Interpreter} from "../Interpreter";
import {AbstractBlock} from "./AbstractBlock";
import {Parser} from "../Parser";
import {Link} from "core/editorCore/model/Link";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
export class TrikSetPainterWidthBlock extends AbstractBlock {

    private interpreter: Interpreter;
    private EXPECTED_NUMBER_OF_OUTBOUND_LINKS = 1;

    constructor(node: DiagramNode, outboundLinks: Link[], interpreter: Interpreter) {
        super(node, outboundLinks);
        this.interpreter = interpreter;
    }

    public run(): void {
        var output = this.node.getName(); + "\n";
        this.checkExpectedNumberOfOutboundLinks(this.EXPECTED_NUMBER_OF_OUTBOUND_LINKS);
        
        var properties = this.node.getChangeableProperties();
        var parser = new Parser();
        var width = parser.parseExpression(properties.get("Width").value, this.interpreter);
        this.interpreter.addOrChangeEnvironmentVariable("painterWidth", width);
        
        console.log(output);
    }

    public getNextNodeId(): string {
        return this.outboundLinks[0].getJointObject().get('target').id;
    }

}