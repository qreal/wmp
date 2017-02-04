import {DisplayWidget} from "../../../implementations/engine/model/DisplayWidget";
import {Device} from "../../robotModel/robotParts/Device";
import {SensorsConfiguration} from "../../../implementations/engine/model/SensorsConfiguration";
import {TwoDPosition} from "../../../types/TwoDPosition";
import {DeviceInfo} from "../../robotModel/DeviceInfo";
import {TwoDRobotModel} from "../../../implementations/robotModel/TwoDRobotModel";
export interface RobotModel {
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