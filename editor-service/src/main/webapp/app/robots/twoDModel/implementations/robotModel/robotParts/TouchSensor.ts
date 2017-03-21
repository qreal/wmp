import {ScalarSensor} from "./ScalarSensor";
export class TouchSensor extends ScalarSensor {
    static parentType = ScalarSensor;
    static deviceName : string = "touch";
    static friendlyName: string = "Touch sensor";
}