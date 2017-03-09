package com.qreal.wmp.db.robot.dao;

import com.qreal.wmp.db.robot.exceptions.AbortedException;
import com.qreal.wmp.db.robot.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.robot.exceptions.NotFoundException;
import com.qreal.wmp.db.robot.model.robot.RobotSerial;
import org.jetbrains.annotations.NotNull;

/** DAO for robotDB. */
public interface RobotDao {
    /**
     * Saves a robot.
     * @param robot robot to saveUser (Id must not be set).
     */
    long saveRobot(@NotNull RobotSerial robot) throws AbortedException;

    /**
     * Finds a robot with specified id.
     * @param robotId id of robot to find
     */
    @NotNull
    RobotSerial getRobot(long robotId) throws NotFoundException;

    /**
     * Update a robot.
     * @param robot robot to updateUser (Id must be set correctly)
     */
    void updateRobot(@NotNull RobotSerial robot) throws AbortedException;

    /**
     * Deletes a robot.
     * @param robotId robot to delete (Id must be set correctly).
     */
    void deleteRobot(long robotId) throws AbortedException, ErrorConnectionException;

    /**
     * Tells if a robot with specified name exists.
     * @param id id of a robot to test if exists.
     */
    boolean isExistsRobot(long id);
}
