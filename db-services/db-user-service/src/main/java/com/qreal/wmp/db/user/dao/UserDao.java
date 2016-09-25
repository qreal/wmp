package com.qreal.wmp.db.user.dao;

import com.qreal.wmp.db.user.client.diagrams.DiagramService;
import com.qreal.wmp.db.user.client.robots.RobotService;
import com.qreal.wmp.db.user.exceptions.AbortedException;
import com.qreal.wmp.db.user.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.user.exceptions.NotFoundException;
import com.qreal.wmp.thrift.gen.TUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** DAO user for UserDB. It operates only with DB and expects correct input. All scheme consistency keeping operations
 * will be performed automatically.*/
public interface UserDao {
    /**
     * Saves user.
     *
     * @param user user to saveUser (Id must not be set)
     */
    void saveUser(@NotNull TUser user) throws AbortedException, ErrorConnectionException;

    /**
     * Finds user by UserName.
     *
     * @param username name of user to find
     */
    @Nullable
    TUser findByUserName(String username) throws NotFoundException, ErrorConnectionException;

    /**
     * Updates user state.
     *
     * @param user user to updateUser (Id must be set)
     */
    void updateUser(@NotNull TUser user) throws AbortedException, ErrorConnectionException;

    /**
     * Test if user with specified name exists.
     *
     * @param username name of user to test if exists
     * @return [description]
     */
    boolean isExistsUser(String username);

    /** For the sake of testing.*/
    void setRobotService(RobotService robotService);

    /** For the sake of testing.*/
    RobotService getRobotService();

    /** For the sake of testing.*/
    void rewindRobotService();

    /** For the sake of testing.*/
    void setDiagramService(DiagramService diagramService);

    /** For the sake of testing.*/
    DiagramService getDiagramService();

    /** For the sake of testing.*/
    void rewindDiagramService();
}
