package com.qreal.robots.client;


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
    long register(TRobot robot);

    /**
     * Finds robot with specified Id.
     *
     * @param id id of robot to find
     */
    TRobot findById(long id);

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
     * Update robot
     *
     * @param robot robot to update (Id must be set correctly)
     */
    void update(TRobot robot);
}