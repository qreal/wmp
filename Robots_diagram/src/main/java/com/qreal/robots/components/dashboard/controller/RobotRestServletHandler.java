package com.qreal.robots.components.dashboard.controller;

import com.qreal.robots.common.utils.AuthenticatedUser;
import com.qreal.robots.components.dashboard.model.robot.Robot;
import com.qreal.robots.thrift.gen.RobotServiceThrift;
import com.qreal.robots.components.database.robots.service.client.RobotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;

public class RobotRestServletHandler implements RobotServiceThrift.Iface {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private AbstractApplicationContext context;

    public RobotRestServletHandler(AbstractApplicationContext context) {
        this.context = context;
    }

    @Override
    public boolean registerRobot(String robotName, String ssid) throws org.apache.thrift.TException {
        logger.trace("RobotRestServlet got register request with parameters: robotName = {}, ssid = {}", robotName, ssid);
        RobotService robotService = (RobotService) context.getBean("robotService");
        robotService.registerByUsername(new Robot(robotName, ssid), AuthenticatedUser.getUserName());
        logger.trace("RobotRestServlet registered robot {} with ssid {}.", robotName, ssid);
        return true;
    }

    @Override
    public boolean deleteRobot(String robotName) throws org.apache.thrift.TException {
        logger.trace("RobotRestServlet got delete request with parameters: robotName = {}", robotName);
        RobotService robotService = (RobotService) context.getBean("robotService");
        robotService.delete(robotName);
        logger.trace("RobotRestServlet deleted robot {}", robotName);
        return true;
    }
}