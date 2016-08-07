package com.qreal.wmp.editor.database.users.client;

import com.qreal.wmp.editor.database.users.model.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** UserDBService interface.*/
public interface UserService {

    /**
     * Saves user (and robots).
     *
     * @param user user to save (Id must not be set).
     */
    void save(@NotNull User user);

    /**
     * Updates user state.
     *
     * @param  user user to update (Id must be set)
     */

    void update(@NotNull User user);

    /**
     * Finds user by UserName.
     *
     * @param username name of user to find
     */
    @Nullable
    User findByUserName(String username);

    /**
     * Test if user with specified name exists.
     *
     * @param username name of user to test if exists
     * @return [description]
     */
    boolean isUserExist(String username);
}
