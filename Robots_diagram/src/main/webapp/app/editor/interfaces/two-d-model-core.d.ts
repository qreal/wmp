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

declare enum ReservedVariableType {
    scalar,
    vector
}

declare enum Direction {
    input,
    output
}

declare interface DeviceInfo {
    getName(): string;
    getFriendlyName(): string;
    getType();
    isA(type): boolean;
}

declare interface PortInfo {
    getName(): string;
    getDirection(): Direction;
    getNameAliases(): string[];
    getReservedVariable(): string;
    getReservedVariableType(): ReservedVariableType;
}

declare interface RobotModelInterface {
    getAvailablePorts(): PortInfo[];
    getConfigurablePorts(): PortInfo[];
    getAllowedDevices(port: PortInfo): DeviceInfo[];
}

declare interface CommonRobotModel extends RobotModelInterface {
    addAllowedConnection(port: PortInfo, devices: DeviceInfo[]);
}

declare class CommonRobotModelImpl implements CommonRobotModel {

    protected ports: PortInfo[];
    protected allowedConnections: {number?: DeviceInfo[]};

    getAvailablePorts(): PortInfo[];
    addAllowedConnection(port: PortInfo, devices: DeviceInfo[]);
    getConfigurablePorts(): PortInfo[];
    getAllowedDevices(port: PortInfo): DeviceInfo[];

}

declare class TwoDRobotModel extends CommonRobotModelImpl {

    constructor(realModel: RobotModelInterface, name: string);

    sensorImagePath(deviceType: DeviceInfo): string;
    getName(): string;
    getRobotImage(): string;
    getConfigurablePorts(): PortInfo[];

}

declare class TwoDPosition {
    x: number;
    y: number;

    constructor(x?: number, y?: number);
}

declare interface Device {

}

declare class DeviceImpl implements Device {
    static parentType;
}

declare class Motor extends DeviceImpl {

    public getPower(): number;
    public setPower(power: number): void;

}

declare class DisplayWidget {
    setBackground(color: string): void;
    drawSmile(): void;
    drawSadSmile(): void;
    drawEllipse(x: number, y: number, a: number, b: number, color: string, thickness: number): void;
    drawArc(x: number, y: number, a: number, b: number, startAngle: number, sweepLength: number,
            color: string, thickness: number): void;
    drawRectangle(x: number, y: number, a: number, b: number, color: string, thickness: number): void;
    drawLine(x1: number, y1: number, x2: number, y2: number, color: string, thickness: number): void;
    drawPoint(x: number, y: number, color: string, thickness: number): void;
    drawText(x: number, y: number, text: string, color: string): void;
    clearSmile(): void;
    clearSadSmile(): void;
    setLedColor(color: string): void;
    reset(): void;
    redraw(): void;
    clearScreen(): void;
    show(): void;
    hide(): void;
    displayToFront(): void;
    displayToBack(): void;
}

declare interface RobotModel {
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

declare class DevicesConfigurationProvider {

    deviceConfigurationChanged(robotModelName: string, portName: string, device: DeviceInfo): void;
    getCurrentConfiguration(robotModelName: string, portName: string): DeviceInfo;

}

declare class SensorsConfiguration extends DevicesConfigurationProvider {

    constructor(robotModel: RobotModel);

    addSensor(portName: string, sensorType: DeviceInfo, position?: TwoDPosition, direction?: number): void;
    removeSensor(portName: string): void;
    deserialize(xml): void;
}

declare interface WorldModel {
    setDrawLineMode(): void;
    setDrawWallMode(): void;
    setDrawPencilMode(): void;
    setDrawEllipseMode(): void;
    getDrawMode(): number;
    setNoneMode(): void;
    getPaper(): RaphaelPaper;
    getZoom(): number;
    setCurrentElement(element): void;
    clearPaper(): void;
    deserialize(xml, offsetX: number, offsetY: number): void;
    addRobotItemElement(element: RaphaelElement): void;
    insertBeforeRobots(element: RaphaelElement): void;
    insertAfterRobots(element: RaphaelElement): void;
}

declare interface Settings {
}

declare interface Timeline {
    start(): void;
    stop(): void;
    setSpeedFactor(factor: number): void;
    getSpeedFactor(): number;
    getRobotModels(): RobotModel[];
    addRobotModel(robotModel: RobotModel): void;
}

declare interface Model {
    getWorldModel() : WorldModel;
    getRobotModels() : RobotModel[];
    getSetting() : Settings;
    addRobotModel(robotModel: TwoDRobotModel): void;
    deserialize(xml): void;
    getTimeline(): Timeline;
}

declare interface TwoDModelEngineFacade {

    setDrawLineMode(): void;
    setDrawWallMode(): void;
    setDrawPencilMode(): void;
    setDrawEllipseMode(): void;
    setNoneMode(): void;
    followRobot(value: boolean): void;

}

declare class TwoDModelEngineFacadeImpl implements TwoDModelEngineFacade {

    protected robotModelName: string;
    protected model: Model;

    constructor($scope, $compile, $attrs);

    setDrawLineMode(): void;
    setDrawWallMode(): void;
    setDrawPencilMode(): void;
    setDrawEllipseMode(): void;
    setNoneMode(): void;
    followRobot(value: boolean): void;

}