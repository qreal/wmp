package com.qreal.robots.components.dashboard.controller;

import com.qreal.robots.common.utils.AuthenticatedUser;
import com.qreal.robots.components.dashboard.model.robot.Robot;
import com.qreal.robots.components.database.robots.service.client.RobotService;
import com.qreal.robots.thrift.gen.RobotServiceThrift;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Thrift RobotRest controller.
 * RPC functions: registerRobot, deleteRobot
 */
public class RobotRestServletHandler implements RobotServiceThrift.Iface {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private AbstractApplicationContext context;

    public RobotRestServletHandler(AbstractApplicationContext context) {
        this.context = context;
    }


    /**
     * RPC function registering robot.
     *
     * @param robotName name of robot
     * @param ssid ssid of robot's wifi
     */
    @Override
    public boolean registerRobot(String robotName, String ssid) throws org.apache.thrift.TException {
        logger.trace("RobotRestServlet got register request with parameters: robotName = {}, ssid = {}", robotName,
                ssid);
        RobotService robotService = (RobotService) context.getBean("robotService");
        robotService.registerByUsername(new Robot(robotName, ssid), AuthenticatedUser.getUserName());
        logger.trace("RobotRestServlet registered robot {} with ssid {}.", robotName, ssid);
        return true;
    }

    /**
     * RPC function deleting robot.
     *
     * @param robotName name of robot
     * @see {@link https://github.com/qreal/wmp/issues/7}
     */
    @Override
    public boolean deleteRobot(long robotId) throws org.apache.thrift.TException {
        logger.trace("RobotRestServlet got delete request with parameters: robotId = {}", robotId);
        RobotService robotService = (RobotService) context.getBean("robotService");
        robotService.delete(robotId);
        logger.trace("RobotRestServlet deleted robot with id = {}", robotId);
        return true;
    }
}
