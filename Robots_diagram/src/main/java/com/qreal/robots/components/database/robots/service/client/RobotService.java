package com.qreal.robots.components.database.robots.service.client;

import com.qreal.robots.components.dashboard.model.robot.Robot;

//SaveModelConfig was temporary deleted. You can find it in old repository.

public interface RobotService {

    void register(Robot robot);

    void registerByUsername(Robot robot, String username);

    Robot findById(long id);

    void delete(long id);
}
