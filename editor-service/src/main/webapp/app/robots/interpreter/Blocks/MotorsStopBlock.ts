import {MotorsBlock} from "./MotorsBlock";
import {Link} from "core/editorCore/model/Link";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {RobotModel} from "../../twoDModel/interfaces/engine/model/RobotModel";
export class MotorsStopBlock extends MotorsBlock {

    constructor(node: DiagramNode, outboundLinks: Link[], robotModels: RobotModel[]) {
        super(node, outboundLinks, robotModels);
    }

    protected getPower(): number {
        return 0;
    }
    
}