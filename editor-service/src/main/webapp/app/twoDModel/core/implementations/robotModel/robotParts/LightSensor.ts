/// <reference path="ScalarSensor" />

class LightSensor extends ScalarSensor {
    static parentType = ScalarSensor;
    static name: string = "light";
    static friendlyName: string = "Light sensor";
}