import {ScalarSensor} from "./ScalarSensor";
export class ColorSensor extends ScalarSensor {
    static parentType = ScalarSensor;
    static deviceName : string = "color";
    static friendlyName:string = "Color sensor";
}