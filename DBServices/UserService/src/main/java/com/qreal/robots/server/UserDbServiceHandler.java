package com.qreal.robots.server;

import com.qreal.robots.dao.UserDao;
import com.qreal.robots.model.auth.User;
import com.qreal.robots.thrift.gen.TUser;
import com.qreal.robots.thrift.gen.UserDbService;
import org.apache.thrift.TException;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Thrift server side handler for UserDBService.
 */
public class UserDbServiceHandler implements UserDbService.Iface {

    private AbstractApplicationContext context;

    public UserDbServiceHandler(AbstractApplicationContext context) {
        this.context = context;
    }

    @Override
    public void save(TUser tUser) throws TException {
        UserDao userDao = (UserDao) context.getBean("userDao");
        userDao.save(new User(tUser));
    }

    @Override
    public void update(TUser user) throws TException {
        UserDao userDao = (UserDao) context.getBean("userDao");
        userDao.update(new User(user));
    }

    @Override
    public TUser findByUserName(String username) throws TException {
        UserDao userDao = (UserDao) context.getBean("userDao");
        return userDao.findByUserName(username).toTUser();
    }

    @Override
    public boolean isUserExist(String username) throws TException {
        UserDao userDao = (UserDao) context.getBean("userDao");
        return userDao.isUserExist(username);
    }
}
