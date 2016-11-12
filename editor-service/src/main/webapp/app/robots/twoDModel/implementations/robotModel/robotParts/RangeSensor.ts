/// <reference path="ScalarSensor" />

class RangeSensor extends ScalarSensor {
    static parentType = ScalarSensor;
    static name: string = "sonar";
    static friendlyName: string = "Range sensor";
}