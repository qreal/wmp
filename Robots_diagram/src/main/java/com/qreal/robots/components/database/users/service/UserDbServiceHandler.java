package com.qreal.robots.components.database.users.service;

import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.database.users.DAO.UserDAO;
import com.qreal.robots.components.database.users.thrift.gen.TUser;
import com.qreal.robots.components.database.users.thrift.gen.UserDbService;
import org.apache.thrift.TException;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Created by artemiibezguzikov on 11.07.16.
 */

public class UserDbServiceHandler implements UserDbService.Iface {

    private AbstractApplicationContext context;

    public UserDbServiceHandler(AbstractApplicationContext context) {
        this.context = context;
    }

    @Override
    public void save(TUser user) throws TException {
        UserDAO userDAO = (UserDAO) context.getBean("UserDAO");
        userDAO.save(convertFromTUser(user));
    }

    @Override
    public TUser findByUserName(String username) throws TException {
        UserService userService = (UserService) context.getBean("UserService");
        return convertFromUser(userService.findByUserName(username));
    }

    @Override
    public boolean isUserExist(String username) throws TException {
        UserService userService = (UserService) context.getBean("UserService");
        return userService.isUserExist(username);
    }

    public static User convertFromTUser(TUser tUser) {
        User user = new User(tUser.getUsername(), tUser.getPassword(), tUser.isEnabled());
        return user;
    }

    public static TUser convertFromUser(User user) {
        TUser tUser = new TUser(user.getUsername(), user.getPassword(), user.isEnabled());
        return tUser;
    }
}
