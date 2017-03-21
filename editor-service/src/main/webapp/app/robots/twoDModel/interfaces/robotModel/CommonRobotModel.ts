import {DeviceInfo} from "./DeviceInfo";
import {PortInfo} from "./PortInfo";
import {RobotModelInterface} from "./RobotModelInterface";
export interface CommonRobotModel extends RobotModelInterface {
    addAllowedConnection(port: PortInfo, devices: DeviceInfo[]);
}