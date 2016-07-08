class SwitchBlock extends ConditionBlock {

    private interpreter: Interpreter;
    private parsedResult: string;
    private MIN_NUMBER_OF_OUTBOUND_LINKS = 1;
    private MAX_NUMBER_OF_OUTBOUND_LINKS = 32767;

    constructor(node: DiagramNode, outboundLinks: Link[], interpreter: Interpreter) {
        super(node, outboundLinks);
        this.interpreter = interpreter;
    }

    public run(): void {
        var output = this.node.getName(); + "\n";
        this.checkRangeNumberOfOutboundLinks(this.MIN_NUMBER_OF_OUTBOUND_LINKS, this.MAX_NUMBER_OF_OUTBOUND_LINKS);

        var expression: string = this.node.getChangeableProperties()["Expression"].value;
        var parser = new Parser();
        this.parsedResult = parser.parseExpression(expression, this.interpreter).toString();
        console.log(output);
    }

    public getNextNodeId(): string {
        var nextNodeId: string;
        var otherwiseNodeId: string;
        for (var i = 0; i < this.outboundLinks.length; i++) {
            var link: Link = this.outboundLinks[i];
            var messageOnLink = this.getGuard(link);
            if (messageOnLink === this.parsedResult) {
                nextNodeId = link.getJointObject().get('target').id;
            }
            if (messageOnLink === "") {
                otherwiseNodeId = link.getJointObject().get('target').id;
            }
        }
        
        if (!nextNodeId && !otherwiseNodeId) {
            throw new Error("Next link after " + this.node.getName() + " was not found");
        }
        
        return (nextNodeId) ? nextNodeId : otherwiseNodeId;
    }
    
}
