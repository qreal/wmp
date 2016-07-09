package com.qreal.robots.components.database.users.DAO;

import com.qreal.robots.components.authorization.model.auth.User;

public interface UserDAO {

    public void save(User user);

    public User findByUserName(String username);

    public boolean isUserExist(String username);
}
