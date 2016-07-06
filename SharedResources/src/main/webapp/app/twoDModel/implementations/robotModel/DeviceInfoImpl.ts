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

class DeviceInfoImpl implements DeviceInfo {
    static createdInfos: {string?: DeviceInfo} = {};

    static fromString(str: string): DeviceInfo {
        if (!DeviceInfoImpl.createdInfos[str]) {
            throw new Error("DeviceInfo for " + str + " not found");
        } else {
            return DeviceInfoImpl.createdInfos[str];
        }
    }

    private name: string;
    private friendlyName: string;
    private deviceType;

    constructor(deviceType) {
        this.deviceType = deviceType;
        this.name = deviceType.name;
        this.friendlyName = deviceType.friendlyName;
    }

    static getInstance(deviceType): DeviceInfo {
        if (!DeviceInfoImpl.createdInfos[deviceType.name]) {
            var deviceInfo: DeviceInfo = new DeviceInfoImpl(deviceType);
            DeviceInfoImpl.createdInfos[deviceType.name] = deviceInfo;
            return deviceInfo;
        } else {
            return DeviceInfoImpl.createdInfos[deviceType.name];
        }
    }

    getName(): string {
        return this.name;
    }

    getFriendlyName(): string {
        return this.friendlyName;
    }

    getType() {
        return this.deviceType;
    }

    isA(type): boolean {
        var currentParent = this.deviceType;

        while(currentParent && currentParent !== type) {
            currentParent = currentParent.parentType;
        }

        return currentParent != undefined;
    }
}