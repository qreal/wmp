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
     * @param user user to save (Id must not be set).
     */
    void save(@NotNull TUser user) throws AbortedException, ErrorConnectionException, TException;

    /**
     * Updates user state.
     * @param user user to update (Id must be set)
     */
    void update(@NotNull TUser user) throws AbortedException, ErrorConnectionException, TException;

    /**
     * Finds a user by UserName.
     * @param username name of user to find
     */
    @NotNull TUser findByUserName(String username) throws NotFoundException, ErrorConnectionException, TException;

    /**
     * Tests if user with specified name exists.
     * @param username name of user to test if exists
     */
    boolean isUserExist(String username) throws ErrorConnectionException, TException;
}
