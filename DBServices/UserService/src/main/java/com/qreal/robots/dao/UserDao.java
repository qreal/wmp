package com.qreal.robots.dao;


import com.qreal.robots.model.auth.User;

/**
 * DAO for userDB.
 */
public interface UserDao {

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
     * Updates user state
     *
     * @param  user user to update (Id must be set)
     */

    void update(User user);


    /**
     * Test if user with specified name exists.
     *
     * @param username name of user to test if exists
     * @return [description]
     */
    boolean isUserExist(String username);
}
