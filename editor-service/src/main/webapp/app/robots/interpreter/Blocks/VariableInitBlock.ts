import {Parser} from "../Parser";
import {AbstractBlock} from "./AbstractBlock";
import {Interpreter} from "../Interpreter";
import {Link} from "core/editorCore/model/Link";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
export class VariableInitBlock extends AbstractBlock {

    private interpreter: Interpreter;
    private EXPECTED_NUMBER_OF_OUTBOUND_LINKS = 1;

    constructor(node: DiagramNode, outboundLinks: Link[], interpreter: Interpreter) {
        super(node, outboundLinks);
        this.interpreter = interpreter;
    }

    public run(): void {
        var output = this.node.getName() + "\n";
        this.checkExpectedNumberOfOutboundLinks(this.EXPECTED_NUMBER_OF_OUTBOUND_LINKS);

        var properties = this.node.getChangeableProperties();
        var variableName = properties.get("variable").value;
        var variableValue = properties.get("value").value;

        var parser: Parser = new Parser();
        this.interpreter.addOrChangeUserVariable(variableName, parser.parseExpression(variableValue, this.interpreter));

        console.log(output);
    }

    public getNextNodeId(): string {
        return this.outboundLinks[0].getJointObject().get('target').id;
    }
    
}