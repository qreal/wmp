package com.qreal.wmp.editor.database.users.client;

import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.exceptions.NotFoundException;
import com.qreal.wmp.editor.database.users.model.User;
import org.apache.thrift.TException;
import org.jetbrains.annotations.NotNull;

/** UserDBService interface.*/
public interface UserService {
    /**
     * Saves a user (and robots).
     * @param user user to saveUser (Id must not be set).
     */
    void saveUser(@NotNull User user) throws AbortedException, ErrorConnectionException, TException;

    /**
     * Updates user state.
     ** @param  user user to updateUser (Id must be set).
     */
    void updateUser(@NotNull User user) throws AbortedException, ErrorConnectionException, TException;

    /**
     * Finds user by UserName.
     * @param username name of a user to find.
     */
    @NotNull User getUser(String username) throws NotFoundException, ErrorConnectionException, TException;

    /**
     * Tests if a user with specified name exists.
     * @param username name of a user to find.
     */
    boolean isUserExists(String username) throws ErrorConnectionException, TException;
}
