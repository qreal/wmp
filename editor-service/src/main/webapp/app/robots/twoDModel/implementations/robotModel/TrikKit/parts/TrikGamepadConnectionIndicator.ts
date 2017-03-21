import {ScalarSensor} from "../../robotParts/ScalarSensor";
export class TrikGamepadConnectionIndicator extends ScalarSensor {
    static parentType = ScalarSensor;
    static deviceName = "gamepadConnectionIndicator";
    static friendlyName = "Android Gamepad Connection Indicator";
}