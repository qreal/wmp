package com.qreal.wmp.db.user.dao;

import com.qreal.wmp.db.user.exceptions.AbortedException;
import com.qreal.wmp.db.user.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.user.exceptions.NotFoundException;
import com.qreal.wmp.thrift.gen.TUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * DAO user for UserDB. It operates only with DB and expects correct input. All scheme consistency keeping operations
 * will be performed automatically.
 */
public interface UserDao {
    /**
     * Saves a user.
     * @param user user to save (Id must not be set).
     */
    void saveUser(@NotNull TUser user) throws AbortedException, ErrorConnectionException;

    /**
     * Finds a user by UserName.
     * @param username name of a user to find.
     */
    @Nullable
    TUser findByUserName(String username) throws NotFoundException, ErrorConnectionException;

    /**
     * Updates user state.
     * @param user user to update (Id must be set)
     */
    void updateUser(@NotNull TUser user) throws AbortedException, ErrorConnectionException;

    /**
     * Tests if a user with specified name exists.
     * @param username name of a user to find.
     * @return [description]
     */
    boolean isExistsUser(String username);
}
