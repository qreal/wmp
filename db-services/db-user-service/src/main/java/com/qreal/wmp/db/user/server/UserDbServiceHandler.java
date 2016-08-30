package com.qreal.wmp.db.user.server;

import com.qreal.wmp.db.user.dao.UserDao;
import com.qreal.wmp.thrift.gen.*;
import org.apache.thrift.TException;
import org.springframework.context.ApplicationContext;

/** Thrift server side handler for UserDBService.*/
public class UserDbServiceHandler implements UserDbService.Iface {

    private final UserDao userDao;

    public UserDbServiceHandler(ApplicationContext context) {
        userDao = (UserDao) context.getBean("userDao");
        assert userDao != null;
    }

    @Override
    public void save(TUser tUser) throws TIdNotDefined {
        if (!tUser.isSetUsername()) {
            throw new TIdNotDefined("User username is null. To save user you should assign username to " +
                    "user.");
        }
        userDao.save(tUser);
    }

    @Override
    public void update(TUser user) throws TIdNotDefined, TNotFound {
        if (!user.isSetUsername()) {
            throw new TIdNotDefined("User username is null. To rewrite user you should specify username.");
        }
        if (!userDao.isUserExist(user.getUsername())) {
            throw new TNotFound(user.getUsername(), "User to rewrite not found.");
        }
        userDao.update(user);
    }

    @Override
    public TUser findByUserName(String username) throws TNotFound {
        TUser tUser = userDao.findByUserName(username);
        if (tUser == null) {
            throw new TNotFound(username, "User to load not found.");
        }
        return tUser;
    }

    @Override
    public boolean isUserExist(String username) {
        return userDao.isUserExist(username);
    }
}
