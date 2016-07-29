package com.qreal.robots.client;

import com.qreal.robots.thrift.gen.TUser;

/**
 * UserDBService interface.
 */
public interface UserService {

    /**
     * Saves user
     *
     * @param user user to save (Id must not be set).
     */
    void save(TUser user);

    /**
     * Updates user state
     *
     * @param  user user to update (Id must be set)
     */

    void update(TUser user);

    /**
     * Finds user by UserName.
     *
     * @param username name of user to find
     */
    TUser findByUserName(String username);

    /**
     * Test if user with specified name exists.
     *
     * @param username name of user to test if exists
     * @return [description]
     */
    boolean isUserExist(String username);
}
