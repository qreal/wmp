import {Motor} from "../../robotParts/Motor";
export class TrikServoMotor extends Motor {
    static parentType = Motor;
    static deviceName = "servo";
    static friendlyName = "Servo Motor";
}