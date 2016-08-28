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

/// <reference path="../../interfaces/robotModel/CommonRobotModel.ts" />
/// <reference path="../../interfaces/robotModel/PortInfo.ts" />
/// <reference path="../../interfaces/robotModel/DeviceInfo.ts" />

class CommonRobotModelImpl implements CommonRobotModel {
    protected ports: PortInfo[] = [];
    protected allowedConnections: {number?: DeviceInfo[]} = {};

    getAvailablePorts(): PortInfo[] {
        return this.ports;
    }

    addAllowedConnection(port: PortInfo, devices: DeviceInfo[]) {
        this.ports.push(port);
        this.allowedConnections[this.ports.indexOf(port)] = devices;
    }

    getConfigurablePorts(): PortInfo[] {
        var result: PortInfo[] = [];
        var robotModel = this;

        robotModel.getAvailablePorts().forEach(function(port) {
            var devices: DeviceInfo[] = robotModel.getAllowedDevices(port);

            if(devices.length > 1) {
                result.push(port);
            }
        });

        return result;
    }

    getAllowedDevices(port: PortInfo): DeviceInfo[] {
        return this.allowedConnections[this.ports.indexOf(port)];
    }
}