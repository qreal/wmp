package com.qreal.wmp.editor.database.users.client;

import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.exceptions.NotFoundException;
import com.qreal.wmp.editor.database.users.model.User;
import org.jetbrains.annotations.NotNull;

/** UserDBService interface.*/
public interface UserService {

    /**
     * Saves user (and robots).
     *
     * @param user user to save (Id must not be set).
     */
    void save(@NotNull User user) throws AbortedException, ErrorConnectionException;

    /**
     * Updates user state.
     *
     * @param  user user to update (Id must be set)
     */

    void update(@NotNull User user) throws AbortedException, ErrorConnectionException;

    /**
     * Finds user by UserName.
     *
     * @param username name of user to find
     */
    @NotNull
    User findByUserName(String username) throws NotFoundException, ErrorConnectionException;

    /**
     * Test if user with specified name exists.
     *
     * @param username name of user to test if exists
     */
    boolean isUserExist(String username) throws ErrorConnectionException;
}
