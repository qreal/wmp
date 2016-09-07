package com.qreal.wmp.db.user.dao;

import com.qreal.wmp.db.user.exceptions.Aborted;
import com.qreal.wmp.db.user.exceptions.ErrorConnection;
import com.qreal.wmp.db.user.exceptions.NotFound;
import com.qreal.wmp.thrift.gen.TUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** DAO user for UserDB. It operates only with DB and expects correct input. All scheme consistency keeping operations
 * will be performed automatically.*/
public interface UserDao {
    /**
     * Saves user.
     *
     * @param user user to save (Id must not be set)
     */
    void save(@NotNull TUser user) throws Aborted, ErrorConnection;

    /**
     * Finds user by UserName.
     *
     * @param username name of user to find
     */
    @Nullable
    TUser findByUserName(String username) throws NotFound, ErrorConnection;

    /**
     * Updates user state.
     *
     * @param user user to update (Id must be set)
     */
    void update(@NotNull TUser user) throws Aborted, ErrorConnection;

    /**
     * Test if user with specified name exists.
     *
     * @param username name of user to test if exists
     * @return [description]
     */
    boolean isExistsUser(String username);
}
