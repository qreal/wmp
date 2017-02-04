import {ScalarSensor} from "./ScalarSensor";
export class EncoderSensor extends ScalarSensor {
    static parentType = ScalarSensor;
    static deviceName : string = "encoder";
    static friendlyName: string = "Encoder";
}