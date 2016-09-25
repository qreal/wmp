package com.qreal.wmp.db.robot.dao;

import com.qreal.wmp.db.robot.client.users.UserService;
import com.qreal.wmp.db.robot.exceptions.AbortedException;
import com.qreal.wmp.db.robot.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.robot.exceptions.NotFoundException;
import com.qreal.wmp.db.robot.model.robot.RobotSerial;
import org.jetbrains.annotations.NotNull;

/** DAO for robotDB.*/
public interface RobotDao {

    /**
     * Saves robot.
     *
     * @param robot robot to saveRobot (Id must not be set).
     */
    long saveRobot(@NotNull RobotSerial robot) throws AbortedException;

    /**
     * Deletes robot.
     *
     * @param robotId robot to deleteRobot (Id must be set correctly).
     */
    void deleteRobot(long robotId) throws AbortedException, ErrorConnectionException;

    /**
     * Finds robot with specified id.
     *
     * @param robotId id of robot to find
     */
    @NotNull
    RobotSerial getRobot(long robotId) throws NotFoundException;

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
    void updateRobot(@NotNull RobotSerial robot) throws AbortedException;

    /** For the sake of testing.*/
    void setUserService(UserService userService);

    /** For the sake of testing.*/
    UserService getUserService();

    /** For the sake of testing.*/
    void rewindUserService();

}
