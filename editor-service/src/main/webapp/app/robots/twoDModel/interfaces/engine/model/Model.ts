import {WorldModel} from "./WorldModel";
import {Timeline} from "./Timeline";
import {TwoDRobotModel} from "../../../implementations/robotModel/TwoDRobotModel";
import {Settings} from "./Settings";
import {RobotModel} from "./RobotModel";
export interface Model {
    getWorldModel() : WorldModel;
    getRobotModels() : RobotModel[];
    getSetting() : Settings;
    addRobotModel(robotModel: TwoDRobotModel): void;
    deserialize(xml): void;
    getTimeline(): Timeline;
}