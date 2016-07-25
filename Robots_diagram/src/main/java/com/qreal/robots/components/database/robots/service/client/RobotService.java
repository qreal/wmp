package com.qreal.robots.components.database.robots.service.client;

import com.qreal.robots.components.dashboard.model.robot.Robot;
import org.apache.thrift.TException;

//SaveModelConfig was temporary deleted. You can find it in old repository.

public interface RobotService {

    //todo robot must not contain id
    void register(Robot robot);

    //todo robot must not contain id
    void registerByUsername(Robot robot, String username);

    //todo we do not use name as unique id. so here should be only id (not name)
    Robot findByName(String name);

    //todo we do not use name as unique id. so here should be only id (not name)
    void delete(String name);
}
