package com.qreal.robots.components.database.robots.DAO;

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
     * Returns random robot with specified name.
     *
     * @param robotName name of robot to find (not unique id)
     * @see {@link https://github.com/qreal/wmp/issues/7}
     */
    Robot findByName(String robotName);

    /**
     * Tells if robot with specified name exists.
     *
     * @param robotName name of robot to test if exists
     * @see {@link https://github.com/qreal/wmp/issues/7}
     */
    boolean isRobotExists(String robotName);
}
