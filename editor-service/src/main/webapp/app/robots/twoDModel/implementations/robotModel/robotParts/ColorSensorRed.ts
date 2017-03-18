import {ColorSensor} from "./ColorSensor";
export class ColorSensorRed extends ColorSensor {
    static parentType = ColorSensor;
    static deviceName : string = "colorRed";
    static friendlyName: string = "Color sensor (red)";
}