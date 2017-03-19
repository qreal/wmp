import {ScalarSensor} from "./ScalarSensor";
export class LightSensor extends ScalarSensor {
    static parentType = ScalarSensor;
    static deviceName : string = "light";
    static friendlyName: string = "Light sensor";
}