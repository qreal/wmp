interface Timeline {
    start(): void;
    stop(): void;
    setSpeedFactor(factor: number): void;
    getSpeedFactor(): number;
    getRobotModels(): RobotModel[];
    addRobotModel(robotModel: RobotModel): void;
}