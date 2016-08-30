package com.qreal.wmp.db.robot.server;

import com.qreal.wmp.db.robot.dao.RobotDao;
import com.qreal.wmp.db.robot.model.robot.RobotSerial;
import com.qreal.wmp.thrift.gen.*;
import org.springframework.context.ApplicationContext;

/** Thrift server side handler for RobotDBService.*/
public class RobotDbServiceHandler implements RobotDbService.Iface {

    private final RobotDao robotDao;

    public RobotDbServiceHandler(ApplicationContext context) {
        robotDao = (RobotDao) context.getBean("robotDao");
        assert robotDao != null;
    }

    @Override
    public long registerRobot(TRobot tRobot) throws TIdAlreadyDefined {
        if (tRobot.isSetId()) {
            throw new TIdAlreadyDefined("Robot id not null. To save robot you should not assign id to robot.");
        }
        return robotDao.save(new RobotSerial(tRobot));
    }

    @Override
    public TRobot findById(long robotId) throws TNotFound {
        RobotSerial robot = robotDao.findById(robotId);
        if (robot == null) {
            throw new TNotFound(String.valueOf(robotId), "Robot not found.");
        }
        return robot.toTRobot();
    }

    @Override
    public void deleteRobot(long robotId) throws TNotFound {
        if (!robotDao.isExistsRobot(robotId)) {
            throw new TNotFound(String.valueOf(robotId), "Robot to delete not found.");
        }
        robotDao.delete(robotId);
    }

    @Override
    public boolean isRobotExists(long id) {
        return robotDao.isExistsRobot(id);
    }

    @Override
    public void updateRobot(TRobot tRobot) throws TIdNotDefined, TNotFound {
        if (!tRobot.isSetId()) {
            throw new TIdNotDefined("Robot id is null. To rewrite robot you should specify id.");
        }
        if (!robotDao.isExistsRobot(tRobot.getId())) {
            throw new TNotFound(String.valueOf(tRobot.getId()), "Robot to rewrite not found.");
        }
        robotDao.updateRobot(new RobotSerial(tRobot));
    }
}
