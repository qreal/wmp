package com.qreal.wmp.db.user.server;

import com.qreal.wmp.db.user.dao.UserDao;
import com.qreal.wmp.db.user.exceptions.Aborted;
import com.qreal.wmp.db.user.exceptions.ErrorConnection;
import com.qreal.wmp.db.user.exceptions.NotFound;
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
        } catch (ErrorConnection e) {
            throw new TErrorConnection(e.getNameClient(), e.getMessage());
        } catch (Aborted e) {
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
        } catch (ErrorConnection e) {
            throw new TErrorConnection(e.getNameClient(), e.getMessage());
        } catch (Aborted e) {
            throw new TAborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
        }
    }

    @Override
    public TUser findByUserName(String username) throws TNotFound, TErrorConnection {
        TUser tUser = null;
        try {
            tUser = userDao.findByUserName(username);
        } catch (ErrorConnection e) {
            throw new TErrorConnection(e.getNameClient(), e.getMessage());
        } catch (NotFound e) {
            throw new TNotFound(username, "User with specified username not found");
        }
        return tUser;
    }

    @Override
    public boolean isUserExist(String username) {
        return userDao.isExistsUser(username);
    }

    /** Only for sake of testing. */
    public UserDao getUserDao() {
        return userDao;
    }

    /** Only for sake of testing. */
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /** Only for sake of testing. */
    public void rewindUserDao() {
        this.userDao = null;
    }
}
