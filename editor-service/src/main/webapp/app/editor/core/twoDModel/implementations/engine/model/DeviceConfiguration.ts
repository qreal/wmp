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

class DeviceConfiguration {

    private configurationMap: {[portName: string]: Device};

    constructor() {
        this.configurationMap = {};
        this.configurationMap["M1"] = new Motor();
        this.configurationMap["M2"] = new Motor();
        this.configurationMap["M3"] = new Motor();
        this.configurationMap["M4"] = new Motor();
    }

    public getConfigurationMap(): {[portName: string]: Device} {
        return this.configurationMap;
    }

    public getDeviceByPortName(portName: string): Device {
        return this.configurationMap[portName];
    }

    public setDeviceToPort(portName: string, device: Device) {
        this.configurationMap[portName] = device;
    }

    public clearState(): void {
        for (var portName in this.configurationMap) {
            if (this.configurationMap[portName] instanceof Motor) {
                var motor: Motor = <Motor> this.configurationMap[portName];
                motor.setPower(0);
            }
        }
    }

}