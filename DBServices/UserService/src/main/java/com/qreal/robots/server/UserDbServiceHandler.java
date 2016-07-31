package com.qreal.robots.server;

import com.qreal.robots.dao.UserDao;
import com.qreal.robots.thrift.gen.TUser;
import com.qreal.robots.thrift.gen.UserDbService;
import org.apache.thrift.TException;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Thrift server side handler for UserDBService.
 */
public class UserDbServiceHandler implements UserDbService.Iface {

    private final AbstractApplicationContext context;

    private final UserDao userDao;

    public UserDbServiceHandler(AbstractApplicationContext context) {
        this.context = context;

        userDao = (UserDao) context.getBean("userDao");
        assert userDao != null;
    }

    @Override
    public void save(TUser tUser) throws TException {
        userDao.save(tUser);
    }

    @Override
    public void update(TUser user) throws TException {
        userDao.update(user);
    }

    @Override
    public TUser findByUserName(String username) throws TException {
        return userDao.findByUserName(username);
    }

    @Override
    public boolean isUserExist(String username) throws TException {
        return userDao.isUserExist(username);
    }
}
