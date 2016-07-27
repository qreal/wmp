package com.qreal.robots.components.database.robots.dao;

import com.qreal.robots.components.dashboard.model.robot.Robot;

/**
 * DAO for robotDB.
 */
public interface RobotDAO {

    /**
     * Saves robot
     *
     * @param robot robot to save (Id must not be set).
     */
    void save(Robot robot);

    /**
     * Deletes robot
     *
     * @param robot robot to delete (Id must be ыуе correctly).
     */
    void delete(Robot robot);

    /**
     * Finds robot with specified id.
     *
     * @param robotId id of robot to find
     */
    Robot findById(long robotId);

    /**
     * Tells if robot with specified name exists.
     *
     * @param robotName name of robot to test if exists
     * @see {@link https://github.com/qreal/wmp/issues/7}
     */
    boolean isRobotExists(String robotName);
}
