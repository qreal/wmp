import {MathUtils} from "../../../utils/MathUtils";
import {Runner} from "../Runner";
import {DeviceConfiguration} from "./DeviceConfiguration";
import {RobotItemImpl} from "../items/RobotItemImpl";
import {RobotItem} from "../../../interfaces/engine/items/RobotItem";
import {DisplayWidget} from "./DisplayWidget";
import {Motor} from "../../robotModel/robotParts/Motor";
import {Device} from "../../../interfaces/robotModel/robotParts/Device";
import {TwoDPosition} from "../../../types/TwoDPosition";
import {DeviceInfo} from "../../../interfaces/robotModel/DeviceInfo";
import {SensorsConfiguration} from "./SensorsConfiguration";
import {TwoDRobotModel} from "../../robotModel/TwoDRobotModel";
import {WorldModel} from "../../../interfaces/engine/model/WorldModel";
import {RobotModel} from "../../../interfaces/engine/model/RobotModel";
export class RobotModelImpl implements RobotModel {
    
    private robotItem: RobotItem;
    private twoDRobotModel: TwoDRobotModel;
    private sensorsConfiguration: SensorsConfiguration;
    private worldModel: WorldModel;
    private displayWidget: DisplayWidget;
    private runner: Runner;
    private isInteractive: boolean;
    private deviceConfiguration: DeviceConfiguration;

    constructor(worldModel: WorldModel, twoDRobotModel: TwoDRobotModel, position: TwoDPosition,
        isInteractive: boolean) {
        this.worldModel = worldModel;
        this.twoDRobotModel = twoDRobotModel;
        this.isInteractive = isInteractive;
        this.robotItem = new RobotItemImpl(worldModel, position, twoDRobotModel.getRobotImage(), isInteractive);
        this.sensorsConfiguration = new SensorsConfiguration(this);
        this.displayWidget = new DisplayWidget();
        this.deviceConfiguration = new DeviceConfiguration();
        this.runner = new Runner();
    }

    info(): TwoDRobotModel {
        return this.twoDRobotModel;
    }

    removeSensorItem(portName: string): void {
        this.robotItem.removeSensorItem(portName);
    }

    getSensorsConfiguration(): SensorsConfiguration {
        return this.sensorsConfiguration;
    }

    addSensorItem(portName: string, deviceType: DeviceInfo, isInteractive: boolean, position?: TwoDPosition,
                  direction?: number): void {
        this.robotItem.addSensorItem(portName, deviceType, this.twoDRobotModel.sensorImagePath(deviceType),
            isInteractive, position, direction);
    }

    isModelInteractive(): boolean {
        return this.isInteractive;
    }

    private parsePositionString(positionStr: string): TwoDPosition {
        var splittedStr = positionStr.split(":");
        var x = parseFloat(splittedStr[0]);
        var y = parseFloat(splittedStr[1]);
        return new TwoDPosition(x, y);
    }

    deserialize(xml, offsetX: number, offsetY: number): void {
        var posString = xml.getAttribute('position');
        var pos = this.parsePositionString(posString);
        pos.x += offsetX;
        pos.y += offsetY;
        var direction = parseFloat(xml.getAttribute('direction'));
        this.robotItem.setStartPosition(pos, direction);
        this.robotItem.setOffsetX(offsetX);
        this.robotItem.setOffsetY(offsetY);
        this.robotItem.returnToStart();

        this.sensorsConfiguration.deserialize(xml);

        this.robotItem.show();
    }

    showCheckResult(result) {
        this.stopPlay();
        this.runner.run(this.robotItem, this.displayWidget, result);
    }

    stopPlay(): void {
        this.runner.stop(this.robotItem, this.displayWidget);
        this.robotItem.clearCurrentPosition();
        this.robotItem.returnToStart();
    }

    closeDisplay(): void {
        this.displayWidget.hide();
    }

    showDisplay(): void {
        this.displayWidget.show();
    }

    follow(value: boolean) {
        this.robotItem.follow(value);
    }

    getDeviceByPortName(portName: string): Device {
        return this.deviceConfiguration.getDeviceByPortName(portName);
    }

    nextFragment(): void {
        var angle = MathUtils.toRadians(this.robotItem.getDirection());
        var robotHeight = 50;
        var timeInterval = 1;
        var speedLeft = (<Motor> this.getDeviceByPortName("M3")).getPower() / 70;
        var speedRight = (<Motor> this.getDeviceByPortName("M4")).getPower() / 70;
        var averageSpeed = (speedLeft + speedRight) / 2;
        var deltaX = 0;
        var deltaY = 0;
        if (speedLeft != speedRight) {
            var radius = speedLeft * robotHeight / (speedLeft - speedRight);
            var averageRadius = radius - robotHeight / 2;
            var angularSpeed = 0;
            if (speedLeft == -speedRight) {
                angularSpeed = speedLeft / radius;
            } else {
                angularSpeed = averageSpeed / averageRadius;
            }
            var gammaRadians = timeInterval * angularSpeed;
            angle += gammaRadians;

            deltaX = averageSpeed * Math.cos(angle);
            deltaY = averageSpeed * Math.sin(angle);
        }
        else {
            deltaX = averageSpeed * Math.cos(angle);
            deltaY += averageSpeed * Math.sin(angle);
        }

        this.robotItem.move(deltaX, deltaY, MathUtils.toDegrees(angle));
    }

    setMarkerDown(down: boolean): void {
        this.robotItem.setMarkerDown(down);
    }

    setMarkerColor(color: string): void {
        this.robotItem.setMarkerColor(color);
    }

    clearState(): void {
        this.deviceConfiguration.clearState();
    }
    
    clearCurrentPosition(): void {
        this.robotItem.clearCurrentPosition();
    }
    
    getDisplayWidget(): DisplayWidget {
        return this.displayWidget;    
    }
}