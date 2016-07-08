

class MotorsStopBlock extends MotorsBlock {

    constructor(node: DiagramNode, outboundLinks: Link[], robotModels: RobotModel[]) {
        super(node, outboundLinks, robotModels);
    }

    protected getPower(): number {
        return 0;
    }
    
}