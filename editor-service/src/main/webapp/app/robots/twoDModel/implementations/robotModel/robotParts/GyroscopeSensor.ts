import {ScalarSensor} from "./ScalarSensor";
export class GyroscopeSensor extends ScalarSensor {
    static parentType = ScalarSensor;
    static deviceName : string = "gyroscope";
    static friendlyName: string = "Gyroscope";
}