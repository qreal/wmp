package com.qreal.wmp.db.robot.dao;

import com.qreal.wmp.db.robot.exceptions.Aborted;
import com.qreal.wmp.db.robot.exceptions.ErrorConnection;
import com.qreal.wmp.db.robot.exceptions.NotFound;
import com.qreal.wmp.db.robot.model.robot.RobotSerial;
import org.jetbrains.annotations.NotNull;

/** DAO for robotDB.*/
public interface RobotDao {

    /**
     * Saves robot.
     *
     * @param robot robot to save (Id must not be set).
     */
    long save(@NotNull RobotSerial robot) throws Aborted;

    /**
     * Deletes robot.
     *
     * @param robotId robot to delete (Id must be set correctly).
     */
    void delete(long robotId) throws Aborted, ErrorConnection;

    /**
     * Finds robot with specified id.
     *
     * @param robotId id of robot to find
     */
    @NotNull
    RobotSerial findById(long robotId) throws NotFound;

    /**
     * Tells if robot with specified name exists.
     *
     * @param id id of robot to test if exists.
     */
    boolean isExistsRobot(long id);

    /**
     * Update robot.
     *
     * @param robot robot to update (Id must be set correctly)
     */
    void updateRobot(@NotNull RobotSerial robot) throws Aborted;
}
