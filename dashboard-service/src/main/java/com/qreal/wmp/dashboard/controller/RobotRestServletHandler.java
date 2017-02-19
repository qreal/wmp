package com.qreal.wmp.dashboard.controller;

import com.qreal.wmp.dashboard.common.utils.AuthenticatedUser;
import com.qreal.wmp.dashboard.database.exceptions.AbortedException;
import com.qreal.wmp.dashboard.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.dashboard.database.robots.client.RobotService;
import com.qreal.wmp.dashboard.database.robots.model.Robot;
import com.qreal.wmp.thrift.gen.RobotServiceThrift;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * Thrift RobotRest controller.
 * RPC functions: registerRobot, deleteRobot
 */
public class RobotRestServletHandler implements RobotServiceThrift.Iface {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private ApplicationContext context;

    public RobotRestServletHandler(ApplicationContext context) {
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
        logger.trace("RobotRestServlet got saveRobot request with parameters: robotName = {}, ssid = {}", robotName,
                ssid);
        RobotService robotService = (RobotService) context.getBean("robotService");
        try {
            robotService.registerByUsername(new Robot(robotName, ssid), AuthenticatedUser.getUserName());
        } catch (AbortedException e) {
            //TODO Here we should send exception to client side.
            logger.error("registerRobot method encountered exception Aborted. Robot was not registered", e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should send exception to client side.
            logger.error("registerRobot method encountered exception ErrorConnection. Robot was not registered", e);
        }
        logger.trace("RobotRestServlet registered robot {} with ssid {}.", robotName, ssid);
        return true;
    }

    /**
     * RPC function deleting robot.
     *
     * @param robotId name of robot
     * @see {@link https://github.com/qreal/wmp/issues/7}
     */
    @Override
    public boolean deleteRobot(long robotId) throws org.apache.thrift.TException {
        logger.trace("RobotRestServlet got deleteRobot request with parameters: robotId = {}", robotId);
        RobotService robotService = (RobotService) context.getBean("robotService");
        try {
            robotService.deleteRobot(robotId);
        } catch (AbortedException e) {
            //TODO Here we should send exception to client side.
            logger.error("deleteRobot method encountered exception Aborted. Robot was not deleted", e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should send exception to client side.
            logger.error("deleteRobot method encountered exception ErrorConnection. Robot was not deleted", e);
        }
        logger.trace("RobotRestServlet deleted robot with id = {}", robotId);
        return true;
    }
}

