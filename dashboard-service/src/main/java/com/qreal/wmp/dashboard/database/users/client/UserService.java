package com.qreal.wmp.dashboard.database.users.client;

import com.qreal.wmp.dashboard.database.exceptions.Aborted;
import com.qreal.wmp.dashboard.database.exceptions.ErrorConnection;
import com.qreal.wmp.dashboard.database.exceptions.NotFound;
import com.qreal.wmp.dashboard.database.users.model.User;
import org.jetbrains.annotations.NotNull;

/** UserDBService interface.*/
public interface UserService {

    /**
     * Saves user (and robots).
     *
     * @param user user to save (Id must not be set).
     */
    void save(@NotNull User user) throws Aborted, ErrorConnection;

    /**
     * Updates user state.
     *
     * @param  user user to update (Id must be set)
     */

    void update(@NotNull User user) throws Aborted, ErrorConnection;

    /**
     * Finds user by UserName.
     *
     * @param username name of user to find
     */
    @NotNull
    User findByUserName(String username) throws NotFound, ErrorConnection;

    /**
     * Test if user with specified name exists.
     *
     * @param username name of user to test if exists
     */
    boolean isUserExist(String username) throws ErrorConnection;
}
