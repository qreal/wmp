import {ColorSensor} from "./ColorSensor";
export class ColorSensorFull extends ColorSensor {
    static parentType = ColorSensor;
    static deviceName : string = "colorRecognition";
    static friendlyName: string = "Color sensor (full)";
}