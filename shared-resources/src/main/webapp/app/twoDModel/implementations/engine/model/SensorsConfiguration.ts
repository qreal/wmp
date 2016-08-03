/*
 * Copyright Vladimir Zakharov 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/// <reference path="../../robotModel/DevicesConfigurationProvider.ts" />
/// <reference path="../../robotModel/robotParts/TouchSensor.ts" />
/// <reference path="../../robotModel/robotParts/ColorSensor.ts" />
/// <reference path="../../robotModel/robotParts/LightSensor.ts" />
/// <reference path="../../robotModel/robotParts/RangeSensor.ts" />
/// <reference path="../../robotModel/robotParts/VectorSensor.ts" />
/// <reference path="../../../interfaces/engine/model/RobotModel.ts" />
/// <reference path="../../../interfaces/robotModel/DeviceInfo.ts" />
/// <reference path="../../../types/TwoDPosition.ts" />

class SensorsConfiguration extends DevicesConfigurationProvider {

    private robotModel: RobotModel;
    private robotModelName: string;

    constructor(robotModel: RobotModel) {
        super();
        this.robotModel = robotModel;
        this.robotModelName = robotModel.info().getName();
    }

    addSensor(portName: string, sensorType: DeviceInfo, position?: TwoDPosition, direction?: number): void {
        if (this.getCurrentConfiguration(this.robotModelName, portName)) {
            this.removeSensor(portName);
        }
        this.deviceConfigurationChanged(this.robotModel.info().getName(), portName, sensorType);
        if (this.isSensorHaveView(sensorType)) {
            this.robotModel.addSensorItem(portName, sensorType, this.robotModel.isModelInteractive(),
                position, direction);
        }
    }

    removeSensor(portName: string): void {
        var sensor = this.getCurrentConfiguration(this.robotModelName, portName);
        if (sensor) {
            if (this.isSensorHaveView(sensor)) {
                this.robotModel.removeSensorItem(portName);
            }
            this.deviceConfigurationChanged(this.robotModelName, portName, null);
        }
    }

    deserialize(xml): void {
        var sensors = xml.getElementsByTagName("sensor");
        for (var i = 0; i < sensors.length; i++) {
            if (!sensors[i].getAttribute('type')) {
                continue;
            }
            var portName: string = sensors[i].getAttribute('port').split("###")[0];
            var typeSplittedStr = sensors[i].getAttribute('type').split("::");
            var device: DeviceInfo = DeviceInfoImpl.fromString(typeSplittedStr[typeSplittedStr.length - 1]);

            var posString = sensors[i].getAttribute('position');
            var pos = this.parsePositionString(posString);
            var direction: number = parseFloat(sensors[i].getAttribute('direction'));

            this.addSensor(portName, device, pos, direction);
        }
    }

    private parsePositionString(positionStr: string): TwoDPosition {
        var splittedStr = positionStr.split(":");
        var x = parseFloat(splittedStr[0]);
        var y = parseFloat(splittedStr[1]);
        return new TwoDPosition(x, y);
    }

    private isSensorHaveView(sensorType: DeviceInfo): boolean {
        return sensorType.isA(TouchSensor)
            || sensorType.isA(ColorSensor)
            || sensorType.isA(LightSensor)
            || sensorType.isA(RangeSensor)
            || sensorType.isA(VectorSensor);
    }
}