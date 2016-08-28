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

/// <reference path="../../robotModel/DeviceInfo.ts" />
/// <reference path="../../../types/TwoDPosition.ts" />
/// <reference path="../../../implementations/robotModel/TwoDRobotModel.ts" />
/// <reference path="../../../implementations/engine/model/SensorsConfiguration.ts" />

interface RobotModel {
    info(): TwoDRobotModel;
    removeSensorItem(portName: string): void;
    addSensorItem(portName: string, deviceType: DeviceInfo, isInteractive: boolean, position?: TwoDPosition,
                  direction?: number): void;
    isModelInteractive(): boolean;
    getSensorsConfiguration(): SensorsConfiguration;
    deserialize(xml, offsetX: number, offsetY: number): void;
    showCheckResult(result);
    stopPlay(): void;
    closeDisplay(): void;
    showDisplay(): void;
    follow(value: boolean): void;
    getDeviceByPortName(portName: string): Device;
    nextFragment(): void;
    setMarkerDown(down: boolean): void;
    setMarkerColor(color: string): void;
    clearState(): void;
    clearCurrentPosition(): void;
    getDisplayWidget(): DisplayWidget;
}