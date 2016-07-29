package com.qreal.robots.server;

import com.qreal.robots.dao.RobotDao;
import com.qreal.robots.model.robot.RobotSerial;
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
    public long registerRobot(TRobot tRobot) throws TException {
        RobotDao robotDao = (RobotDao) context.getBean("robotDao");
        RobotSerial robotSerial = new RobotSerial(tRobot);
        return robotDao.save(robotSerial);
    }

    @Override
    public TRobot findById(long id) throws TException {
        RobotDao robotDao = (RobotDao) context.getBean("robotDao");
        RobotSerial robot = robotDao.findById(id);
        if (robot != null) {
            return robot.toTRobot();
        }
        return null;
    }

    @Override
    public void deleteRobot(long id) throws TException {
        RobotDao robotDao = (RobotDao) context.getBean("robotDao");
        RobotSerial robot = robotDao.findById(id);
        if (robot != null) {
            robotDao.delete(robot);
        }
    }

    @Override
    public boolean isRobotExists(long id) throws TException {
        RobotDao robotDao = (RobotDao) context.getBean("robotDao");
        return robotDao.isRobotExists(id);
    }

    @Override
    public void updateRobot(TRobot tRobot) throws TException {
        RobotDao robotDao = (RobotDao) context.getBean("robotDao");
        RobotSerial robotSerial = new RobotSerial(tRobot);
        robotDao.updateRobot(robotSerial);
    }
}
