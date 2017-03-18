import {ColorSensor} from "./ColorSensor";
export class ColorSensorBlue extends ColorSensor {
    static parentType = ColorSensor;
    static deviceName : string = "colorBlue";
    static friendlyName: string = "Color sensor (blue)";
}