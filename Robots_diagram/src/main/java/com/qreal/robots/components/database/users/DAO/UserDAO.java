package com.qreal.robots.components.database.users.DAO;

import com.qreal.robots.components.authorization.model.auth.User;

public interface UserDAO {

    void save(User user);

    User findByUserName(String username);

    boolean isUserExist(String username);
}
