package com.qreal.robots.database.robots.client;

import com.qreal.robots.database.robots.model.Robot;
import com.qreal.robots.thrift.gen.TRobot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * RobotDBService interface.
 */
public interface RobotService {

    /**
     * Saves robot.
     *
     * @param robot robot to save (Id must not be set)
     */
    long register(@NotNull Robot robot);

    /**
     * Register robot with specified owner.
     *
     * @param robot    robot to register (Id must not be set)
     * @param username owner of robot
     */
    void registerByUsername(@NotNull Robot robot, String username);

    /**
     * Finds robot with specified Id.
     *
     * @param id id of robot to find
     */
    @Nullable
    Robot findById(long id);

    /**
     * Test if exists robot with specified Id.
     *
     * @param id of robot to test if exists
     */
    boolean isRobotExists(long id);

    /**
     * Deletes robot.
     *
     * @param id id of robot to delete
     */
    void delete(long id);

    /**
     * Update robot.
     *
     * @param robot robot to update (Id must be set correctly)
     */
    void update(@NotNull TRobot robot);
}