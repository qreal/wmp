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

/// <reference path="CommonRobotModelImpl.ts" />
/// <reference path="robotParts/LightSensor.ts" />
/// <reference path="TrikKit/parts/TrikInfraredSensor.ts" />
/// <reference path="TrikKit/parts/TrikSonarSensor.ts" />
/// <reference path="TrikKit/parts/TrikLineSensor.ts" />
/// <reference path="../../interfaces/robotModel/RobotModelInterface.ts" />
/// <reference path="../../interfaces/robotModel/PortInfo.ts" />
/// <reference path="../../../constants/GeneralConstants.d.ts" />

class TwoDRobotModel extends CommonRobotModelImpl {
    private name: string;
    private image: string;
    private realModel: RobotModelInterface;

    constructor(realModel: RobotModelInterface, name: string) {
        super();
        var twoDRobotModel = this;
        this.realModel = realModel;
        this.name = name;
        this.image = GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/trikTwoDRobot.svg"

        realModel.getAvailablePorts().forEach(function(port) {
            twoDRobotModel.addAllowedConnection(port, realModel.getAllowedDevices(port));
        });
    }

    sensorImagePath(deviceType: DeviceInfo): string {
        if (deviceType.isA(LightSensor)) {
            return GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/twoDColorEmpty.svg";
        } else if (deviceType.isA(TrikInfraredSensor)) {
            return GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/twoDIrRangeSensor.svg";
        } else if (deviceType.isA(TrikSonarSensor)) {
            return GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/twoDUsRangeSensor.svg";
        } else if (deviceType.isA(TrikLineSensor)) {
            return GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/twoDVideoModule.svg";
        }

        return null;
    }

    getName(): string {
        return this.name;
    }

    getRobotImage(): string {
        return this.image;
    }

    getConfigurablePorts(): PortInfo[] {
        return this.realModel.getConfigurablePorts();
    }
}