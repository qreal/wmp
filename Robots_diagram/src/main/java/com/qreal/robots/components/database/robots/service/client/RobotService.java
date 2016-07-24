package com.qreal.robots.components.database.robots.service.client;

import com.qreal.robots.components.dashboard.model.robot.Robot;
import org.apache.thrift.TException;

//SaveModelConfig was temporary deleted. You can find it in old repository.

public interface RobotService {

    //todo robot must not contain id
    void register(Robot robot);

    //todo robot must not contain id
    void registerByUsername(Robot robot, String username);

    //TODO WE DO NOT USE NAME AS UNIQUE ID. SO HERE SHOULD BE ONLY ID (NOT NAME)
    Robot findByName(String name);

    //TODO WE DO NOT USE NAME AS UNIQUE ID. SO HERE SHOULD BE ONLY ID (NOT NAME)
    void delete(String name);
}
