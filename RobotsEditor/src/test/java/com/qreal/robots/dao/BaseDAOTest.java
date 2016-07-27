package com.qreal.robots.dao;

import com.qreal.robots.components.database.users.dao.UserDAO;
import com.qreal.robots.components.authorization.model.auth.User;

public class BaseDAOTest {

    public static final String USER_NAME = "user";

    public static final String USER_NAME2 = "user2";

    public static final String USER_NAME3 = "user3";

    public static final String ROBOT_NAME = "robot";

    public static final String ROBOT_NAME2 = "robot2";

    public static final String PASSWORD = "password";

    protected User getAndSaveUser(String username, UserDAO userDAO) {
        User user = new User(username, PASSWORD, true);
        userDAO.save(user);
        return user;
    }

}
