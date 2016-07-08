

abstract class MotorsDirectionBlock extends MotorsBlock {

    private interpreter: Interpreter;
    
    constructor(node: DiagramNode, outboundLinks: Link[], robotModels: RobotModel[], interpreter: Interpreter) {
        super(node, outboundLinks, robotModels);
        this.interpreter = interpreter;
    }
    
    protected getPowerProperty(): number {
        var properties = this.node.getChangeableProperties();
        var parser = new Parser();
        var power = parser.parseExpression(properties["Power"].value, this.interpreter);
        if (power < -100 || power > 100) {
            throw new Error("Error: incorrect power value in " + this.node.getName() +
                " (must be between -100 and 100)");
        }
        return power;
    }

}