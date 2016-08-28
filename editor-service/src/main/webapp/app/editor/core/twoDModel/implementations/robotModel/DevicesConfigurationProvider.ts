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

/// <reference path="../../interfaces/robotModel/DeviceInfo.ts" />

class DevicesConfigurationProvider {
    private currentConfiguration: {string?: {string?: DeviceInfo}} = {};

    deviceConfigurationChanged(robotModelName: string, portName: string, device: DeviceInfo): void {
        if (!this.currentConfiguration[robotModelName]) {
            this.currentConfiguration[robotModelName] = {};
        }
        if (device == null) {
            if (this.currentConfiguration[robotModelName][portName]) {
                delete this.currentConfiguration[robotModelName][portName];
            }
        } else {
            this.currentConfiguration[robotModelName][portName] = device;
        }
    }

    getCurrentConfiguration(robotModelName: string, portName: string): DeviceInfo {
        if (!this.currentConfiguration[robotModelName] || !this.currentConfiguration[robotModelName][portName]) {
            return null;
        }
        return this.currentConfiguration[robotModelName][portName];
    }
}