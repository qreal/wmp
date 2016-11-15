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