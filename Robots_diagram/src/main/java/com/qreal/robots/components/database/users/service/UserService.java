package com.qreal.robots.components.database.users.service;

import com.qreal.robots.components.authorization.model.auth.User;

public interface UserService {

    public void save(User user);

    public User findByUserName(String username);

    public boolean isUserExist(String username);

}
