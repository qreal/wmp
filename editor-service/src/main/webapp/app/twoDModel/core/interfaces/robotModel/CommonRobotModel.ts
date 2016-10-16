/// <reference path="RobotModelInterface.ts" />
/// <reference path="DeviceInfo.ts" />
/// <reference path="PortInfo.ts" />

interface CommonRobotModel extends RobotModelInterface {
    addAllowedConnection(port: PortInfo, devices: DeviceInfo[]);
}