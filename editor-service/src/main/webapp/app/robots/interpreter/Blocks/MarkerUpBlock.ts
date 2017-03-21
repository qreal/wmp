import {AbstractBlock} from "./AbstractBlock";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {Link} from "core/editorCore/model/Link";
import {RobotModel} from "../../twoDModel/interfaces/engine/model/RobotModel";
export class MarkerUpBlock extends AbstractBlock {

    private robotModels: RobotModel[];
    private EXPECTED_NUMBER_OF_OUTBOUND_LINKS = 1;

    constructor(node: DiagramNode, outboundLinks: Link[], robotModels: RobotModel[]) {
        super(node, outboundLinks);
        this.robotModels = robotModels;
    }

    public run(): void {
        var output = this.node.getName(); + "\n";
        this.checkExpectedNumberOfOutboundLinks(this.EXPECTED_NUMBER_OF_OUTBOUND_LINKS);

        for (var modelId = 0; modelId < this.robotModels.length; modelId++) {
            var model = this.robotModels[modelId];
            model.setMarkerDown(false);
        }

        console.log(output);
    }

    public getNextNodeId(): string {
        return this.outboundLinks[0].getJointObject().get('target').id;
    }
    
}