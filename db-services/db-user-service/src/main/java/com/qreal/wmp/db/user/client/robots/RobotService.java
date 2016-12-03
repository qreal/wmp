package com.qreal.wmp.db.user.client.robots;

import com.qreal.wmp.db.user.exceptions.AbortedException;
import com.qreal.wmp.db.user.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.user.exceptions.NotFoundException;
import com.qreal.wmp.thrift.gen.TRobot;
import org.apache.thrift.TException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** RobotDBService interface.*/
public interface RobotService {
    /**
     * Saves a robot.
     * @param robot robot to saveUser (Id must not be set).
     */
    long saveRobot(@NotNull TRobot robot) throws AbortedException, ErrorConnectionException, TException;

    /**
     * Finds a robot with specified Id.
     * @param id id of a robot to find.
     */
    @Nullable TRobot getRobot(long id) throws NotFoundException, ErrorConnectionException, TException;

    /**
     * Updates a robot (Id must be set).
     * @param robot robot to updateUser (Id must be set correctly)
     */
    void updateRobot(@NotNull TRobot robot) throws AbortedException, ErrorConnectionException, TException;

    /**
     * Deletes a robot.
     * @param id id of a robot to deleteRobot.
     */
    void deleteRobot(long id) throws AbortedException, ErrorConnectionException, TException;

    /**
     * Tests if a robot with specified Id exists.
     * @param id of robot to test if exists
     */
    boolean isRobotExists(long id) throws ErrorConnectionException, TException;
}

