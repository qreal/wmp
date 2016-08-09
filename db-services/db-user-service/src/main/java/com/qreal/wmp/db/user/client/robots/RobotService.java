package com.qreal.wmp.db.user.client.robots;

import com.qreal.wmp.thrift.gen.TRobot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** RobotDBService interface.*/
public interface RobotService {

    /**
     * Saves robot.
     *
     * @param robot robot to save (Id must not be set)
     */
    long register(@NotNull TRobot robot);

    /**
     * Finds robot with specified Id. (or null)
     *
     * @param id id of robot to find
     */
    @Nullable
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
     * Update robot. (Id must be set)
     *
     * @param robot robot to update (Id must be set correctly)
     */
    void update(@NotNull TRobot robot);
}