import {ColorSensor} from "./ColorSensor";
export class ColorSensorPassive extends ColorSensor {
    static parentType = ColorSensor;
    static deviceName : string = "colorNone";
    static friendlyName: string = "Color sensor (passive)";
}