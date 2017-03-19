import {Parser} from "../Parser";
import {Interpreter} from "../Interpreter";
import {AbstractBlock} from "./AbstractBlock";
import {Link} from "core/editorCore/model/Link";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {RobotModel} from "../../twoDModel/interfaces/engine/model/RobotModel";
export class PrintTextBlock extends AbstractBlock {

    private interpreter: Interpreter;
    private robotModels: RobotModel[];
    private EXPECTED_NUMBER_OF_OUTBOUND_LINKS = 1;

    constructor(node: DiagramNode, outboundLinks: Link[], interpreter: Interpreter, robotModels: RobotModel[]) {
        super(node, outboundLinks);
        this.interpreter = interpreter;
        this.robotModels = robotModels;
    }

    public run(): void {
        var output = this.node.getName(); + "\n";
        this.checkExpectedNumberOfOutboundLinks(this.EXPECTED_NUMBER_OF_OUTBOUND_LINKS);

        var properties = this.node.getChangeableProperties();
        var parser = new Parser();
        var x = parser.parseExpression(properties["XCoordinateText"].value, this.interpreter);
        var y = parser.parseExpression(properties["YCoordinateText"].value, this.interpreter);
        
        var text = properties["PrintText"].value;

        for (var modelId = 0; modelId < this.robotModels.length; modelId++) {
            var model = this.robotModels[modelId];
            model.getDisplayWidget().drawText(x, y, text, this.interpreter.getEnvironmentVariable("painterColor"));
        }

        console.log(output);
    }

    public getNextNodeId(): string {
        return this.outboundLinks[0].getJointObject().get('target').id;
    }
    
}