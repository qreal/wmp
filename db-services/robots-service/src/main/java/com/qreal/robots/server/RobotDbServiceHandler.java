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

    private final RobotDao robotDao;

    public RobotDbServiceHandler(AbstractApplicationContext context) {
        robotDao = (RobotDao) context.getBean("robotDao");
        assert robotDao != null;
    }

    @Override
    public long registerRobot(TRobot tRobot) throws TException {
        return robotDao.save(new RobotSerial(tRobot));
    }

    @Override
    public TRobot findById(long id) throws TException {
        RobotSerial robot = robotDao.findById(id);
        if (robot != null) {
            return robot.toTRobot();
        }
        return null;
    }

    @Override
    public void deleteRobot(long id) throws TException {
        RobotSerial robot = robotDao.findById(id);
        if (robot != null) {
            robotDao.delete(robot);
        }
    }

    @Override
    public boolean isRobotExists(long id) throws TException {
        return robotDao.isRobotExists(id);
    }

    @Override
    public void updateRobot(TRobot tRobot) throws TException {
        RobotSerial robotSerial = new RobotSerial(tRobot);
        robotDao.updateRobot(robotSerial);
    }
}
