package com.qreal.wmp.db.robot.client.users;

import com.qreal.wmp.db.robot.exceptions.AbortedException;
import com.qreal.wmp.db.robot.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.robot.exceptions.NotFoundException;
import com.qreal.wmp.thrift.gen.TUser;
import org.apache.thrift.TException;
import org.jetbrains.annotations.NotNull;

/** UserDBService interface.*/
public interface UserService {
    /**
     * Saves a user.
     * @param user user to saveUser (Id must not be set).
     */
    void saveUser(@NotNull TUser user) throws AbortedException, ErrorConnectionException, TException;

    /**
     * Updates user state.
     * @param user user to updateUser (Id must be set)
     */
    void updateUser(@NotNull TUser user) throws AbortedException, ErrorConnectionException, TException;

    /**
     * Finds a user by UserName.
     * @param username name of user to find
     */
    @NotNull TUser getUser(String username) throws NotFoundException, ErrorConnectionException, TException;

    /**
     * Tests if user with specified name exists.
     * @param username name of user to test if exists
     */
    boolean isUserExists(String username) throws ErrorConnectionException, TException;
}
