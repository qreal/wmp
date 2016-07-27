package com.qreal.robots.components.database.users.dao;

import com.qreal.robots.components.authorization.model.auth.User;

/**
 * DAO for userDB.
 */
public interface UserDAO {

    /**
     * Saves user.
     *
     * @param user user to save (Id must not be set)
     */
    void save(User user);

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
