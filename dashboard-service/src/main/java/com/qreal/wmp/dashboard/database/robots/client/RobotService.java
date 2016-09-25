package com.qreal.wmp.dashboard.database.robots.client;

import com.qreal.wmp.dashboard.database.exceptions.AbortedException;
import com.qreal.wmp.dashboard.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.dashboard.database.exceptions.NotFoundException;
import com.qreal.wmp.dashboard.database.robots.model.Robot;
import com.qreal.wmp.thrift.gen.TRobot;
import org.jetbrains.annotations.NotNull;

/** RobotDBService interface.*/
public interface RobotService {

    /**
     * Saves robot.
     *
     * @param robot robot to save (Id must not be set)
     */
    long register(@NotNull Robot robot) throws AbortedException, ErrorConnectionException;

    /**
     * Register robot with specified owner.
     *
     * @param robot    robot to register (Id must not be set)
     * @param username owner of robot
     */
    void registerByUsername(@NotNull Robot robot, String username) throws AbortedException, ErrorConnectionException;

    /**
     * Finds robot with specified Id.
     *
     * @param id id of robot to find
     */
    @NotNull
    Robot findById(long id) throws NotFoundException, ErrorConnectionException;

    /**
     * Test if exists robot with specified Id.
     *
     * @param id of robot to test if exists
     */
    boolean isRobotExists(long id) throws ErrorConnectionException;

    /**
     * Deletes robot.
     *
     * @param id id of robot to delete
     */
    void delete(long id) throws AbortedException, ErrorConnectionException;

    /**
     * Update robot.
     *
     * @param robot robot to update (Id must be set correctly)
     */
    void update(@NotNull TRobot robot) throws AbortedException, ErrorConnectionException;
}