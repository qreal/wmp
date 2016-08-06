package com.qreal.wmp.db.robot.dao;

import com.qreal.wmp.db.robot.model.robot.RobotSerial;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * DAO for robotDB.
 */
public interface RobotDao {

    /**
     * Saves robot.
     *
     * @param robot robot to save (Id must not be set).
     */
    long save(@NotNull RobotSerial robot);

    /**
     * Deletes robot.
     *
     * @param robot robot to delete (Id must be set correctly).
     */
    void delete(@NotNull RobotSerial robot);

    /**
     * Finds robot with specified id. (or null)
     *
     * @param robotId id of robot to find
     */
    @Nullable
    RobotSerial findById(long robotId);

    /**
     * Tells if robot with specified name exists.
     *
     * @param id id of robot to test if exists.
     * FIXME: https://github.com/qreal/wmp/issues/7
     */
    boolean isRobotExists(long id);

    /**
     * Update robot.
     *
     * @param robot robot to update (Id must be set correctly)
     */
    void updateRobot(@NotNull RobotSerial robot);
}
