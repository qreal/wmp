package com.qreal.robots.components.database.robots.service.client;

import com.qreal.robots.components.dashboard.model.robot.Robot;
import org.apache.thrift.TException;

//SaveModelConfig was temporary deleted. You can find it in old repository.

public interface RobotService {

    public String register(Robot robot) throws TException;

    public String registerByUsername(Robot robot, String username) throws TException;

    public Robot findByName(String name) throws TException;

    public String delete(String name) throws TException;
}
