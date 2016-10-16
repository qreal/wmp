/// <reference path="PortInfo.ts" />
/// <reference path="DeviceInfo.ts" />

interface RobotModelInterface {
    getAvailablePorts(): PortInfo[];
    getConfigurablePorts(): PortInfo[];
    getAllowedDevices(port: PortInfo): DeviceInfo[];
}