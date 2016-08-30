package com.qreal.wmp.db.robot.client.users;

import com.qreal.wmp.db.robot.client.exceptions.NotFound;
import com.qreal.wmp.thrift.gen.TUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** UserDBService interface.*/
public interface UserService {
    /**
     * Saves user.
     *
     * @param user user to save (Id must not be set).
     */
    void save(@NotNull TUser user);

    /**
     * Updates user state.
     *
     * @param user user to update (Id must be set)
     */

    void update(@NotNull TUser user);

    /**
     * Finds user by UserName. (Or null)
     *
     * @param username name of user to find
     */
    @Nullable
    TUser findByUserName(String username) throws NotFound;

    /**
     * Test if user with specified name exists.
     *
     * @param username name of user to test if exists
     * @return [description]
     */
    boolean isUserExist(String username);
}
