abstract class MotorsBlock extends AbstractBlock {
    
    private robotModels: RobotModel[];
    private EXPECTED_NUMBER_OF_OUTBOUND_LINKS = 1;

    constructor(node: DiagramNode, outboundLinks: Link[], robotModels: RobotModel[]) {
        super(node, outboundLinks);
        this.robotModels = robotModels;
    }

    public run(): void {
        var output = this.node.getName(); + "\n";
        this.checkExpectedNumberOfOutboundLinks(this.EXPECTED_NUMBER_OF_OUTBOUND_LINKS);
        var properties = this.node.getChangeableProperties();
        var ports = properties["Ports"].value.replace(/ /g,'').split(",");
        
        var power: number = this.getPower();
        output += "Ports: " + ports + "\n" + "Power: " + power + "\n";

        var model = this.robotModels[0];
        for (var i = 0; i < ports.length; i++) {
            var motor: Motor = <Motor> model.getDeviceByPortName(ports[i]);
            if (motor) {
                motor.setPower(power);
            } else {
                throw new Error("Error: Incorrect port name " + ports[i] + " in " + this.node.getName());
            }
        }

        console.log(output);
    }

    public getNextNodeId(): string {
        return this.outboundLinks[0].getJointObject().get('target').id;
    }
    
    protected abstract getPower(): number;
    
}
