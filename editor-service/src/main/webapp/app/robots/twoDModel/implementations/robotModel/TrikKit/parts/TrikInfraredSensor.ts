import {RangeSensor} from "../../robotParts/RangeSensor";
export class TrikInfraredSensor extends RangeSensor {
    static parentType = RangeSensor;
    static deviceName = "infrared";
    static friendlyName = "Infrared Sensor";
}