package com.qreal.wmp.dashboard.database.robots.client;

import com.qreal.wmp.dashboard.database.exceptions.AbortedException;
import com.qreal.wmp.dashboard.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.dashboard.database.exceptions.NotFoundException;
import com.qreal.wmp.dashboard.database.robots.model.Robot;
import com.qreal.wmp.thrift.gen.TRobot;
import org.jetbrains.annotations.NotNull;

/** RobotDBService interface. */
public interface RobotService {

    /**
     * Saves a robot.
     *
     * @param robot robot to save (Id must not be set)
     */
    long register(@NotNull Robot robot) throws AbortedException, ErrorConnectionException;

    /**
     * Registers a robot with specified owner.
     *
     * @param robot    robot to register (Id must not be set)
     * @param username owner of robot
     */
    void registerByUsername(@NotNull Robot robot, String username) throws AbortedException, ErrorConnectionException;

    /**
     * Finds a robot with specified Id.
     *
     * @param id id of robot to find
     */
    @NotNull
    Robot findById(long id) throws NotFoundException, ErrorConnectionException;

    /**
     * Tests if the robot with specified Id exists.
     *
     * @param id of robot to test if exists
     */
    boolean isRobotExists(long id) throws ErrorConnectionException;

    /**
     * Deletes a robot.
     *
     * @param id id of robot to delete
     */
    void delete(long id) throws AbortedException, ErrorConnectionException;

    /**
     * Updates a robot.
     *
     * @param robot robot to update (Id must be set correctly)
     */
    void update(@NotNull TRobot robot) throws AbortedException, ErrorConnectionException;
}