package com.qreal.robots.components.database.robots.service.client;

import com.qreal.robots.components.dashboard.model.robot.Robot;
import com.qreal.robots.thrift.gen.TRobot;

/**
 * RobotDBService interface.
 */
public interface RobotService {

    /**
     * Saves robot.
     *
     * @param robot robot to save (Id must not be set)
     */
    long register(Robot robot);

    /**
     * Register robot with specified owner.
     *
     * @param robot    robot to register (Id must not be set)
     * @param username owner of robot
     */
    void registerByUsername(Robot robot, String username);

    /**
     * Finds robot with specified Id.
     *
     * @param id id of robot to find
     */
    Robot findById(long id);

    /**
     * Test if exists robot with specified Id.
     *
     * @param id of robot to test if exists
     */
    Boolean isRobotExists(long id);

    /**
     * Deletes robot.
     *
     * @param id id of robot to delete
     */
    void delete(long id);

    /**
     * Update robot
     *
     * @param robot robot to update (Id must be set correctly)
     */
    void update(TRobot robot);
}