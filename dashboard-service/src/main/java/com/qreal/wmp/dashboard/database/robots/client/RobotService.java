package com.qreal.wmp.dashboard.database.robots.client;

import com.qreal.wmp.dashboard.database.exceptions.AbortedException;
import com.qreal.wmp.dashboard.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.dashboard.database.exceptions.NotFoundException;
import com.qreal.wmp.dashboard.database.robots.model.Robot;
import com.qreal.wmp.thrift.gen.TRobot;
import org.apache.thrift.TException;
import org.jetbrains.annotations.NotNull;

/** RobotDBService interface. */
public interface RobotService {

    /**
     * Saves a robot.
     *
     * @param robot robot to saveRobot (Id must not be set)
     */
    long saveRobot(@NotNull Robot robot) throws AbortedException, ErrorConnectionException, TException;

    /**
     * Registers a robot with specified owner.
     *
     * @param robot    robot to saveRobot (Id must not be set)
     * @param username owner of robot
     */
    void registerByUsername(@NotNull Robot robot, String username) throws AbortedException, ErrorConnectionException,
            TException;

    /**
     * Finds a robot with specified Id.
     *
     * @param id id of robot to find
     */
    @NotNull
    Robot getRobot(long id) throws NotFoundException, ErrorConnectionException, AbortedException, TException;

    /**
     * Tests if the robot with specified Id exists.
     *
     * @param id of robot to test if exists
     */
    boolean isRobotExists(long id) throws ErrorConnectionException, TException;

    /**
     * Deletes a robot.
     *
     * @param id id of robot to deleteRobot
     */
    void deleteRobot(long id) throws AbortedException, ErrorConnectionException, TException;

    /**
     * Updates a robot.
     *
     * @param robot robot to updateRobot (Id must be set correctly)
     */
    void updateRobot(@NotNull TRobot robot) throws AbortedException, ErrorConnectionException, TException;
}