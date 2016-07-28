package com.qreal.robots.dao;


import com.qreal.robots.model.robot.RobotSerial;

/**
 * DAO for robotDB.
 */
public interface RobotDao {

    /**
     * Saves robot
     *
     * @param robot robot to save (Id must not be set).
     */
    long save(RobotSerial robot);

    /**
     * Deletes robot
     *
     * @param robot robot to delete (Id must be ыуе correctly).
     */
    void delete(RobotSerial robot);

    /**
     * Finds robot with specified id.
     *
     * @param robotId id of robot to find
     */
    RobotSerial findById(long robotId);

    /**
     * Tells if robot with specified name exists.
     *
     * @param id id of robot to test if exists
     * @see {@link https://github.com/qreal/wmp/issues/7}
     */
    boolean isRobotExists(long id);
}
