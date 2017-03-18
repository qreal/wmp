import {Timeline} from "../../../interfaces/engine/model/Timeline";
import {RobotModel} from "../../../interfaces/engine/model/RobotModel";
export class TimelineImpl implements Timeline {

    private fps: number = 60;
    private defaultFrameLength: number = 1000 / this.fps;

    private slowSpeedFactor: number = 2;
    private normalSpeedFactor: number = 5;
    private fastSpeedFactor: number = 10;
    private immediateSpeedFactor: number = 100000000;

    private speedFactor: number = 1;
    private cyclesCount: number;
    private intervalId: number;
    private isActive : boolean;

    private robotModels: RobotModel[] = [];

    constructor() {
        this.setActive(false);
    }

    /**
     * Start timer and robot's movement
     */
    start(): void {
        if (this.isActive) {
            return;
        }

        this.setActive(true);
        var timeline = this;
        this.cyclesCount = 0;
        this.robotModels[0].getDisplayWidget().displayToFront();
        this.intervalId = setInterval(function() { timeline.onTimer(timeline); }, this.defaultFrameLength);
    }

    /**
     * Stop timer and robot's movement
     */
    stop(): void {
        this.setActive(false);
        this.robotModels[0].getDisplayWidget().displayToBack();
        this.robotModels[0].getDisplayWidget().reset();
        clearInterval(this.intervalId);
        this.robotModels.forEach((model) => { model.clearState(); });
    }

    /**
     * Call method for robotModel to recalculate parameters and to draw new state on the paper by timer tick
     * @param timeline
     */
    onTimer(timeline: Timeline): void {
        if (!this.isActive) {
            return;
        }

        this.cyclesCount++;
        if (this.cyclesCount >= this.speedFactor) {
            timeline.getRobotModels().forEach(function(model) {
                model.nextFragment();
            });
            this.cyclesCount = 0;
        }
    }

    setActive(value : boolean) : void{
        this.isActive = value;
    }

    setSpeedFactor(factor: number): void {
        this.speedFactor = factor;
    }

    getSpeedFactor(): number {
        return this.speedFactor;
    }

    getRobotModels(): RobotModel[] {
        return this.robotModels;
    }

    addRobotModel(robotModel: RobotModel): void {
        this.robotModels.push(robotModel);
    }
}