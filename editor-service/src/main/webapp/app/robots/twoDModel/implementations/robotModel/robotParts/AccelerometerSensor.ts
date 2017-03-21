import {ScalarSensor} from "./ScalarSensor";
export class AccelerometerSensor extends ScalarSensor {
    static parentType = ScalarSensor;
    static deviceName: string = "accelerometer";
    static friendlyName: string = "Accelerometer";
}