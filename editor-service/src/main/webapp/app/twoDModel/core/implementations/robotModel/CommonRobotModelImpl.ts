/// <reference path="../../interfaces/robotModel/CommonRobotModel.ts" />
/// <reference path="../../interfaces/robotModel/PortInfo.ts" />
/// <reference path="../../interfaces/robotModel/DeviceInfo.ts" />

class CommonRobotModelImpl implements CommonRobotModel {
    protected ports: PortInfo[] = [];
    protected allowedConnections: {number?: DeviceInfo[]} = {};

    getAvailablePorts(): PortInfo[] {
        return this.ports;
    }

    addAllowedConnection(port: PortInfo, devices: DeviceInfo[]) {
        this.ports.push(port);
        this.allowedConnections[this.ports.indexOf(port)] = devices;
    }

    getConfigurablePorts(): PortInfo[] {
        var result: PortInfo[] = [];
        var robotModel = this;

        robotModel.getAvailablePorts().forEach(function(port) {
            var devices: DeviceInfo[] = robotModel.getAllowedDevices(port);

            if(devices.length > 1) {
                result.push(port);
            }
        });

        return result;
    }

    getAllowedDevices(port: PortInfo): DeviceInfo[] {
        return this.allowedConnections[this.ports.indexOf(port)];
    }
}