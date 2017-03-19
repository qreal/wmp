import {DeviceImpl} from "./DeviceImpl";
export class Speaker extends DeviceImpl {
    static parentType = DeviceImpl;
    static deviceName : string = "speaker";
    static friendlyName: string = "Speaker";
}