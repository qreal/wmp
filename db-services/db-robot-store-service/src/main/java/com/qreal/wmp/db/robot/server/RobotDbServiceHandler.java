package com.qreal.wmp.db.robot.server;

import com.qreal.wmp.db.robot.dao.RobotDao;
import com.qreal.wmp.db.robot.exceptions.AbortedException;
import com.qreal.wmp.db.robot.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.robot.exceptions.NotFoundException;
import com.qreal.wmp.db.robot.model.robot.RobotSerial;
import com.qreal.wmp.thrift.gen.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/** Thrift server-side handler for RobotDBService.*/
public class RobotDbServiceHandler implements RobotDbService.Iface {
    @Autowired
    private RobotDao robotDao;

    public RobotDbServiceHandler(ApplicationContext context) {
        robotDao = (RobotDao) context.getBean("robotDao");
        assert robotDao != null;
    }

    @Override
    public long registerRobot(TRobot tRobot) throws TAborted, TIdAlreadyDefined {
        long id = 0;
        if (tRobot.isSetId()) {
            throw new TIdAlreadyDefined("Robot id not null. To save a robot you should not assign it an Id.");
        }
        try {
            id = robotDao.saveRobot(new RobotSerial(tRobot));
        } catch (AbortedException e) {
            throw new TAborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
        }
        return id;
    }

    @Override
    public TRobot findById(long robotId) throws TNotFound {
        RobotSerial robot = null;
        try {
            robot = robotDao.getRobot(robotId);
        } catch (NotFoundException e) {
            throw new TNotFound(String.valueOf(robotId), "Robot not found.");
        }
        return robot.toTRobot();
    }

    @Override
    public void deleteRobot(long robotId) throws TAborted, TErrorConnection {
        try {
            robotDao.deleteRobot(robotId);
        } catch (ErrorConnectionException e) {
            throw new TErrorConnection(e.getClientName(), e.getMessage());
        } catch (AbortedException e) {
            throw new TAborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
        }
    }

    @Override
    public boolean isRobotExists(long id) {
        return robotDao.isExistsRobot(id);
    }

    @Override
    public void updateRobot(TRobot tRobot) throws TAborted, TIdNotDefined {
        if (!tRobot.isSetId()) {
            throw new TIdNotDefined("Robot Id is null. To rewrite robot you should specify its id.");
        }
        try {
            robotDao.updateRobot(new RobotSerial(tRobot));
        } catch (AbortedException e) {
            throw new TAborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
        }
    }
}
