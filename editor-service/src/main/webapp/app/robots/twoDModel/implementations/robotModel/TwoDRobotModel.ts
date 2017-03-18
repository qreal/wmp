import {GeneralConstants} from "../../../../common/constants/GeneralConstants";
import {TrikLineSensor} from "./TrikKit/parts/TrikLineSensor";
import {TrikSonarSensor} from "./TrikKit/parts/TrikSonarSensor";
import {TrikInfraredSensor} from "./TrikKit/parts/TrikInfraredSensor";
import {LightSensor} from "./robotParts/LightSensor";
import {DeviceInfo} from "../../interfaces/robotModel/DeviceInfo";
import {RobotModelInterface} from "../../interfaces/robotModel/RobotModelInterface";
import {CommonRobotModelImpl} from "./CommonRobotModelImpl";
import {PortInfo} from "../../interfaces/robotModel/PortInfo";
export class TwoDRobotModel extends CommonRobotModelImpl {
    private name: string;
    private image: string;
    private realModel: RobotModelInterface;

    constructor(realModel: RobotModelInterface, name: string) {
        super();
        var twoDRobotModel = this;
        this.realModel = realModel;
        this.name = name;
        this.image = GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/trikTwoDRobot.svg";

        realModel.getAvailablePorts().forEach(function(port) {
            twoDRobotModel.addAllowedConnection(port, realModel.getAllowedDevices(port));
        });
    }

    sensorImagePath(deviceType: DeviceInfo): string {
        if (deviceType.isA(LightSensor)) {
            return GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/twoDColorEmpty.svg";
        } else if (deviceType.isA(TrikInfraredSensor)) {
            return GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/twoDIrRangeSensor.svg";
        } else if (deviceType.isA(TrikSonarSensor)) {
            return GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/twoDUsRangeSensor.svg";
        } else if (deviceType.isA(TrikLineSensor)) {
            return GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/twoDVideoModule.svg";
        }

        return null;
    }

    getName(): string {
        return this.name;
    }

    getRobotImage(): string {
        return this.image;
    }

    getConfigurablePorts(): PortInfo[] {
        return this.realModel.getConfigurablePorts();
    }
}