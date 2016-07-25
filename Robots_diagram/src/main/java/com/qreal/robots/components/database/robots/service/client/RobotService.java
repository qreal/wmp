package com.qreal.robots.components.database.robots.service.client;

import com.qreal.robots.components.dashboard.model.robot.Robot;

/**
 * RobotDBService interface.
 */
public interface RobotService {

    /**
     * Saves robot.
     *
     * @param robot robot to save (Id must not be set)
     */
    void register(Robot robot);

    /**
     * Register robot with specified owner.
     *
     * @param robot    robot to register (Id must not be set)
     * @param username owner of robot
     */
    void registerByUsername(Robot robot, String username);

    /**
     * Returns random robot with specified name.
     *
     * @param name name of robot to find (not unique id)
     * @see {@link https://github.com/qreal/wmp/issues/7}
     */
    Robot findByName(String name);

    /**
     * Deletes robot.
     *
     * @param name robot to delete (Id must be ыуе correctly)
     */
    void delete(String name);
}
