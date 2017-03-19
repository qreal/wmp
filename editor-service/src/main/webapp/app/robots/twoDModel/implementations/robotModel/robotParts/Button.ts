import {ScalarSensor} from "./ScalarSensor";
export class Button extends ScalarSensor {
    static parentType = ScalarSensor;
    static deviceName: string = "button";
    static friendlyName: string = "Button";
}