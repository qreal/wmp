package com.qreal.robots.components.database.users.service.client;

import com.qreal.robots.components.authorization.model.auth.User;

/**
 * UserDBService interface.
 */
public interface UserService {

    /**
     * Saves user
     *
     * @param user user to save (Id must not be set).
     */
    void save(User user);

    /**
     * Updates user state
     *
     * @param  user user to update (Id must be set)
     */

    void update(User user);

    /**
     * Finds user by UserName.
     *
     * @param username name of user to find
     */
    User findByUserName(String username);

    /**
     * Test if user with specified name exists.
     *
     * @param username name of user to test if exists
     * @return [description]
     */
    boolean isUserExist(String username);
}
