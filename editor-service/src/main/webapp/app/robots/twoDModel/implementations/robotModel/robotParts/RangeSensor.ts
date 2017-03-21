import {ScalarSensor} from "./ScalarSensor";
export class RangeSensor extends ScalarSensor {
    static parentType = ScalarSensor;
    static deviceName : string = "sonar";
    static friendlyName: string = "Range sensor";
}