package com.qreal.robots.components.database.robots.service.server;

import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.dashboard.model.robot.Robot;
import com.qreal.robots.components.database.robots.DAO.RobotDAO;
import com.qreal.robots.components.database.robots.thrift.gen.RobotDbService;
import com.qreal.robots.components.database.robots.thrift.gen.TRobot;
import com.qreal.robots.components.database.users.service.client.UserService;
import org.apache.thrift.TException;
import org.springframework.context.support.AbstractApplicationContext;

public class RobotDbServiceHandler implements RobotDbService.Iface{

    private AbstractApplicationContext context;

    public RobotDbServiceHandler(AbstractApplicationContext context) {
        this.context = context;
    }

    public static Robot convertToRobot(TRobot tRobot, User user) {
        return new Robot(tRobot.getName(), tRobot.getSsid(), user);
    }

    public static TRobot convertFromRobot(Robot robot) {
        return new TRobot(robot.getName(), robot.getSsid(), robot.getOwner().getUsername());
    }

    @Override
    public void registerRobot(TRobot tRobot) throws TException {
        UserService userService = (UserService) context.getBean("UserService");
        RobotDAO robotDAO = (RobotDAO) context.getBean("RobotDAO");
        User user = null;
        try {
            user = userService.findByUserName(tRobot.getUsername());
        } catch (TException e) {
            e.printStackTrace();
        }

        if (!userRobotExists(user, tRobot.getName())) {
            robotDAO.save(convertToRobot(tRobot, user));
        }
    }

    @Override
    public TRobot findByName(String name) throws TException {
        RobotDAO robotDAO = (RobotDAO) context.getBean("RobotDAO");
        return convertFromRobot(robotDAO.findByName(name));
    }

    @Override
    public void deleteRobot(String name) throws TException {
        RobotDAO robotDAO = (RobotDAO) context.getBean("RobotDAO");
        Robot robot = robotDAO.findByName(name);
        robotDAO.delete(robot);
    }

    private boolean userRobotExists(User user, String name) {
        for (Robot robot : user.getRobots()) {
            if (robot.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }


}