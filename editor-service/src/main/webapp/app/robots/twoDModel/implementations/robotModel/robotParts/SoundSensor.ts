import {ScalarSensor} from "./ScalarSensor";
export class SoundSensor extends ScalarSensor {
    static parentType = ScalarSensor;
    static deviceName : string = "sound";
    static friendlyName: string = "Sound sensor";
}