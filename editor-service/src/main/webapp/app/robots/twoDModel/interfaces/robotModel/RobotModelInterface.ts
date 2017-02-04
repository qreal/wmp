import {DeviceInfo} from "./DeviceInfo";
import {PortInfo} from "./PortInfo";
export interface RobotModelInterface {
    getAvailablePorts(): PortInfo[];
    getConfigurablePorts(): PortInfo[];
    getAllowedDevices(port: PortInfo): DeviceInfo[];
}