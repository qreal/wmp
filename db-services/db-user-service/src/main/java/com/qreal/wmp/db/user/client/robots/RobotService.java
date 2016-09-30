package com.qreal.wmp.db.user.client.robots;

import com.qreal.wmp.db.user.exceptions.AbortedException;
import com.qreal.wmp.db.user.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.user.exceptions.NotFoundException;
import com.qreal.wmp.thrift.gen.TRobot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** RobotDBService interface.*/
public interface RobotService {
    /**
     * Saves a robot.
     * @param robot robot to save (Id must not be set).
     */
    long register(@NotNull TRobot robot) throws AbortedException, ErrorConnectionException;

    /**
     * Finds a robot with specified Id.
     * @param id id of a robot to find.
     */
    @Nullable TRobot findById(long id) throws NotFoundException, ErrorConnectionException;

    /**
     * Tests if a robot with specified Id exists.
     * @param id of robot to test if exists
     */
    boolean isRobotExists(long id) throws ErrorConnectionException;

    /**
     * Deletes a robot.
     * @param id id of a robot to delete.
     */
    void delete(long id) throws AbortedException, ErrorConnectionException;

    /**
     * Updates a robot (Id must be set).
     * @param robot robot to update (Id must be set correctly)
     */
    void update(@NotNull TRobot robot) throws AbortedException, ErrorConnectionException;
}

