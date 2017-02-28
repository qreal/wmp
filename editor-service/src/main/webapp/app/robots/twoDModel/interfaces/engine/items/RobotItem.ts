import {AbstractItem} from "./AbstractItem";
import {TwoDPosition} from "../../../types/TwoDPosition";
import {DeviceInfo} from "../../robotModel/DeviceInfo";
export interface RobotItem extends AbstractItem {
    getWidth(): number;
    getHeight(): number;
    getStartPosition(): TwoDPosition;
    getDirection(): number;
    getCenter(): TwoDPosition;
    removeSensorItem(portName: string): void;
    addSensorItem(portName: string, deviceType: DeviceInfo, pathToImage: string, isInteractive: boolean,
                  position?: TwoDPosition, direction?: number): void;
    setStartPosition(position: TwoDPosition, direction: number): void;
    clearCurrentPosition(): void;
    hide(): void;
    show(): void;
    moveSensors(deltaX: number, deltaY: number, direction: number, centerX: number, centerY: number): void;
    setOffsetX(offsetX: number): void;
    setOffsetY(offsetY: number): void;
    moveToPoint(x: number, y: number, rotation: number): void;
    move(deltaX: number, deltaY: number, direction: number): void;
    setMarkerDown(down: boolean): void;
    setMarkerColor(color: string): void;
    follow(value: boolean): void;
    returnToStart(): void;
}