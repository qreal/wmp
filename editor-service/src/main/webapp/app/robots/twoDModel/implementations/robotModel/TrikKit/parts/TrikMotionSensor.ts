import {ScalarSensor} from "../../robotParts/ScalarSensor";
export class TrikMotionSensor extends ScalarSensor {
    static parentType = ScalarSensor;
    static deviceName = "motion";
    static friendlyName = "Motion Sensor";
}