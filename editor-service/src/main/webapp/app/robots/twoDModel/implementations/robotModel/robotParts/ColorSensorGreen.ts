import {ColorSensor} from "./ColorSensor";
export class ColorSensorGreen extends ColorSensor {
    static parentType = ColorSensor;
    static deviceName : string = "colorGreen";
    static friendlyName: string = "Color sensor (green)";
}