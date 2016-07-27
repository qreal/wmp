package com.qreal.robots.components.database.robots.service.server;

import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.dashboard.model.robot.Robot;
import com.qreal.robots.components.database.robots.dao.RobotDao;
import com.qreal.robots.components.database.users.service.client.UserService;
import com.qreal.robots.thrift.gen.RobotDbService;
import com.qreal.robots.thrift.gen.TRobot;
import org.apache.thrift.TException;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Thrift server side handler for RobotDBService.
 */
public class RobotDbServiceHandler implements RobotDbService.Iface {

    private AbstractApplicationContext context;

    public RobotDbServiceHandler(AbstractApplicationContext context) {
        this.context = context;
    }

    @Override
    public void registerRobot(TRobot tRobot) throws TException {
        UserService userService = (UserService) context.getBean("userService");
        RobotDao robotDao = (RobotDao) context.getBean("robotDao");
        User user = userService.findByUserName(tRobot.getUsername());

        if (!userRobotExists(user, tRobot.getName())) {
            robotDao.save(new Robot(tRobot, user));
        }
    }

    @Override
    public TRobot findById(long id) throws TException {
        RobotDao robotDao = (RobotDao) context.getBean("robotDao");
        return robotDao.findById(id).toTRobot();
    }

    @Override
    public void deleteRobot(long id) throws TException {
        RobotDao robotDao = (RobotDao) context.getBean("robotDao");
        Robot robot = robotDao.findById(id);
        robotDao.delete(robot);
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
