import {Interpreter} from "../Interpreter";
import {MotorsDirectionBlock} from "./MotorsDirectionBlock";
import {Link} from "core/editorCore/model/Link";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {RobotModel} from "../../twoDModel/interfaces/engine/model/RobotModel";
export class MotorsBackwardBlock extends MotorsDirectionBlock {

    constructor(node: DiagramNode, outboundLinks: Link[], robotModels: RobotModel[], interpreter: Interpreter) {
        super(node, outboundLinks, robotModels, interpreter);
    }
    
    protected getPower(): number {
        return -this.getPowerProperty();
    }

}