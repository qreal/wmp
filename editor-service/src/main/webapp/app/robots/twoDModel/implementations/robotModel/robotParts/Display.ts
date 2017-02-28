import {DeviceImpl} from "./DeviceImpl";
export class Display extends DeviceImpl {
    static parentType = DeviceImpl;
    static deviceName : string = "display";
    static friendlyName: string = "Display";
}