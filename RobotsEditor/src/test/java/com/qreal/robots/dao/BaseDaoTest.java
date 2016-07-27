package com.qreal.robots.dao;

import com.qreal.robots.components.database.users.dao.UserDao;
import com.qreal.robots.components.authorization.model.auth.User;

public class BaseDaoTest {

    public static final String USER_NAME = "user";

    public static final String USER_NAME2 = "user2";

    public static final String USER_NAME3 = "user3";

    public static final String ROBOT_NAME = "robot";

    public static final String ROBOT_NAME2 = "robot2";

    public static final String PASSWORD = "password";

    protected User getAndSaveUser(String username, UserDao userDao) {
        User user = new User(username, PASSWORD, true);
        userDao.save(user);
        return user;
    }

}
