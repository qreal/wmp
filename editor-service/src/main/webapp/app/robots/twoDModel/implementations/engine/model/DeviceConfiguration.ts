import {Device} from "../../../interfaces/robotModel/robotParts/Device";
import {Motor} from "../../robotModel/robotParts/Motor";
export class DeviceConfiguration {

    private configurationMap: {[portName: string]: Device};

    constructor() {
        this.configurationMap = {};
        this.configurationMap["M1"] = new Motor();
        this.configurationMap["M2"] = new Motor();
        this.configurationMap["M3"] = new Motor();
        this.configurationMap["M4"] = new Motor();
    }

    public getConfigurationMap(): {[portName: string]: Device} {
        return this.configurationMap;
    }

    public getDeviceByPortName(portName: string): Device {
        return this.configurationMap[portName];
    }

    public setDeviceToPort(portName: string, device: Device) {
        this.configurationMap[portName] = device;
    }

    public clearState(): void {
        for (var portName in this.configurationMap) {
            if (this.configurationMap[portName] instanceof Motor) {
                var motor: Motor = <Motor> this.configurationMap[portName];
                motor.setPower(0);
            }
        }
    }

}