import {PrintTextBlock} from "./Blocks/PrintTextBlock";
import {TrikSetBackgroundBlock} from "./Blocks/TrikSetBackgroundBlock";
import {TrikDrawRectBlock} from "./Blocks/TrikDrawRectBlock";
import {TrikDrawLineBlock} from "./Blocks/TrikDrawLineBlock";
import {TrikDrawPixelBlock} from "./Blocks/TrikDrawPixelBlock";
import {ClearScreenBlock} from "./Blocks/ClearScreenBlock";
import {TrikDrawEllipseBlock} from "./Blocks/TrikDrawEllipseBlock";
import {TrikSetPainterWidthBlock} from "./Blocks/TrikSetPainterWidthBlock";
import {TrikSetPainterColorBlock} from "./Blocks/TrikSetPainterColorBlock";
import {TrikSmileBlock} from "./Blocks/TrikSmileBlock";
import {TrikSadSmileBlock} from "./Blocks/TrikSadSmileBlock";
import {EndIfBlock} from "./Blocks/EndIfBlock";
import {RandomInitBlock} from "./Blocks/RandomInitBlock";
import {VariableInitBlock} from "./Blocks/VariableInitBlock";
import {MarkerDownBlock} from "./Blocks/MarkerDownBlock";
import {MarkerUpBlock} from "./Blocks/MarkerUpBlock";
import {SwitchBlock} from "./Blocks/SwitchBlock";
import {TimerBlock} from "./Blocks/TimerBlock";
import {MotorsStopBlock} from "./Blocks/MotorsStopBlock";
import {MotorsBackwardBlock} from "./Blocks/MotorsBackwardBlock";
import {MotorsForwardBlock} from "./Blocks/MotorsForwardBlock";
import {FunctionBlock} from "./Blocks/FunctionBlock";
import {IfBlock} from "./Blocks/IfBlock";
import {FinalBlock} from "./Blocks/FinalBlock";
import {InitialBlock} from "./Blocks/InitialBlock";
import {AbstractBlock} from "./Blocks/AbstractBlock";
import {Interpreter} from "./Interpreter";
import {Link} from "core/editorCore/model/Link";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {RobotModel} from "../twoDModel/interfaces/engine/model/RobotModel";
export class BlockFactory {

    public createBlock(node: DiagramNode, outboundLinks: Link[], interpreter: Interpreter, 
                       robotModels: RobotModel[]): AbstractBlock {
        switch (node.getType()) {
            case "InitialNode":
                return new InitialBlock(node, outboundLinks);
            case "FinalNode":
                return new FinalBlock(node, outboundLinks);
            case "IfBlock":
                return new IfBlock(node, outboundLinks, interpreter);
            case "Function":
                return new FunctionBlock(node, outboundLinks, interpreter);                
            case "TrikV6EnginesForward":
                return new MotorsForwardBlock(node, outboundLinks, robotModels, interpreter);
            case "TrikV6EnginesBackward":
                return new MotorsBackwardBlock(node, outboundLinks, robotModels, interpreter);
            case "TrikV6EnginesStop":
                return new MotorsStopBlock(node, outboundLinks, robotModels);
            case "Timer":
                return new TimerBlock(node, outboundLinks, interpreter);
            case "SwitchBlock":
                return new SwitchBlock(node, outboundLinks, interpreter);
            case "MarkerUp":
                return new MarkerUpBlock(node, outboundLinks, robotModels);
            case "MarkerDown":
                return new MarkerDownBlock(node, outboundLinks, robotModels);
            case "VariableInit":
                return new VariableInitBlock(node, outboundLinks, interpreter);
            case "Randomizer":
                return new RandomInitBlock(node, outboundLinks, interpreter);
            case "FiBlock":
                return new EndIfBlock(node, outboundLinks);
            case "TrikSadSmile":
                return new TrikSadSmileBlock(node, outboundLinks, robotModels);
            case "TrikSmile":
                return new TrikSmileBlock(node, outboundLinks, robotModels);
            case "TrikSetPainterColor":
                return new TrikSetPainterColorBlock(node, outboundLinks, interpreter);
            case "TrikSetPainterWidth":
                return new TrikSetPainterWidthBlock(node, outboundLinks, interpreter);
            case "TrikDrawEllipse":
                return new TrikDrawEllipseBlock(node, outboundLinks, interpreter, robotModels);
            case "ClearScreen":
                return new ClearScreenBlock(node, outboundLinks, robotModels);
            case "TrikDrawPixel":
                return new TrikDrawPixelBlock(node, outboundLinks, interpreter, robotModels);
            case "TrikDrawLine":
                return new TrikDrawLineBlock(node, outboundLinks, interpreter, robotModels);
            case "TrikDrawRect":
                return new TrikDrawRectBlock(node, outboundLinks, interpreter, robotModels);
            case "TrikSetBackground":
                return new TrikSetBackgroundBlock(node, outboundLinks, robotModels);
            case "PrintText":
                return new PrintTextBlock(node, outboundLinks, interpreter, robotModels);
            default:
                throw new Error("Block " + node.getType() + " is not supported");
        }
    }

}