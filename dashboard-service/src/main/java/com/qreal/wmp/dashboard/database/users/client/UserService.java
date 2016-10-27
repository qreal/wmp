package com.qreal.wmp.dashboard.database.users.client;

import com.qreal.wmp.dashboard.database.exceptions.AbortedException;
import com.qreal.wmp.dashboard.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.dashboard.database.exceptions.NotFoundException;
import com.qreal.wmp.dashboard.database.users.model.User;
import org.apache.thrift.TException;
import org.jetbrains.annotations.NotNull;

/** UserDBService interface.*/
public interface UserService {
    /**
     * Saves user (and robots).
     * @param user user to saveUser (Id must not be set).
     */
    void saveUser(@NotNull User user) throws AbortedException, ErrorConnectionException, TException;

    /**
     * Updates user state.
     * @param  user user to updateUser (Id must be set)
     */
    void updateUser(@NotNull User user) throws AbortedException, ErrorConnectionException, TException;

    /**
     * Finds user by UserName.
     * @param username name of user to find
     */
    @NotNull User getUser(String username) throws NotFoundException, ErrorConnectionException, TException;

    /**
     * Test if user with specified name exists.
     * @param username name of user to test if exists
     */
    boolean isUserExists(String username) throws ErrorConnectionException, TException;
}