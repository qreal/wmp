package com.qreal.wmp.db.robot.client.users;

import com.qreal.wmp.db.robot.exceptions.Aborted;
import com.qreal.wmp.db.robot.exceptions.ErrorConnection;
import com.qreal.wmp.db.robot.exceptions.NotFound;
import com.qreal.wmp.thrift.gen.TUser;
import org.jetbrains.annotations.NotNull;

/** UserDBService interface.*/
public interface UserService {
    /**
     * Saves user.
     *
     * @param user user to save (Id must not be set).
     */
    void save(@NotNull TUser user) throws Aborted, ErrorConnection;

    /**
     * Updates user state.
     *
     * @param user user to update (Id must be set)
     */
    void update(@NotNull TUser user) throws Aborted, ErrorConnection;

    /**
     * Finds user by UserName.
     *
     * @param username name of user to find
     */
    @NotNull
    TUser findByUserName(String username) throws NotFound, ErrorConnection;

    /**
     * Test if user with specified name exists.
     *
     * @param username name of user to test if exists
     */
    boolean isUserExist(String username) throws ErrorConnection;
}
