package com.qreal.wmp.db.user.server;

import com.qreal.wmp.db.user.dao.UserDao;
import com.qreal.wmp.db.user.exceptions.AbortedException;
import com.qreal.wmp.db.user.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.user.exceptions.NotFoundException;
import com.qreal.wmp.thrift.gen.*;
import org.springframework.context.ApplicationContext;

/** Thrift server side handler for UserDBService.*/
public class UserDbServiceHandler implements UserDbService.Iface {

    private UserDao userDao;

    public UserDbServiceHandler(ApplicationContext context) {
        userDao = (UserDao) context.getBean("userDao");
        assert userDao != null;
    }

    @Override
    public void save(TUser tUser) throws TIdNotDefined, TAborted, TErrorConnection {
        if (!tUser.isSetUsername()) {
            throw new TIdNotDefined("User username is null. To saveUser user you should assign username to user.");
        }
        try {
            userDao.saveUser(tUser);
        } catch (ErrorConnectionException e) {
            throw new TErrorConnection(e.getNameClient(), e.getMessage());
        } catch (AbortedException e) {
            throw new TAborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
        }
    }

    @Override
    public void update(TUser user) throws TAborted, TIdNotDefined, TErrorConnection {
        if (!user.isSetUsername()) {
            throw new TIdNotDefined("User username is null. To rewrite user you should specify username.");
        }
        try {
            userDao.updateUser(user);
        } catch (ErrorConnectionException e) {
            throw new TErrorConnection(e.getNameClient(), e.getMessage());
        } catch (AbortedException e) {
            throw new TAborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
        }
    }

    @Override
    public TUser findByUserName(String username) throws TNotFound, TErrorConnection {
        TUser tUser = null;
        try {
            tUser = userDao.findByUserName(username);
        } catch (ErrorConnectionException e) {
            throw new TErrorConnection(e.getNameClient(), e.getMessage());
        } catch (NotFoundException e) {
            throw new TNotFound(username, "User with specified username not found");
        }
        return tUser;
    }

    @Override
    public boolean isUserExist(String username) {
        return userDao.isExistsUser(username);
    }
}
