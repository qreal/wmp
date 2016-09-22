package com.qreal.wmp.db.user.test.server;

import com.qreal.wmp.db.user.config.AppInit;
import com.qreal.wmp.db.user.dao.UserDao;
import com.qreal.wmp.db.user.exceptions.Aborted;
import com.qreal.wmp.db.user.exceptions.ErrorConnection;
import com.qreal.wmp.db.user.exceptions.NotFound;
import com.qreal.wmp.db.user.server.UserDbServiceHandler;
import com.qreal.wmp.thrift.gen.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DbServiceHandlerUserTest {

    public UserDbServiceHandler handler;

    @Autowired
    public ApplicationContext context;

    @Before
    public void setMocking() {
        if (handler == null) {
            handler = new UserDbServiceHandler(context);
        }
        handler.setUserDao(mock(UserDao.class));
    }

    @After
    public void deleteMocking() {
        handler.rewindUserDao();
    }

    /** Test saveUser operation for user. */
    @Test
    @Rollback
    public void saveUser_correctInput_userDaoCalled() throws TErrorConnection, TIdNotDefined, TAborted, Aborted,
            ErrorConnection {
        TUser tUser = createUser("username", "password", true);

        handler.save(tUser);

        verify(handler.getUserDao()).saveUser(tUser);
    }

    /** Test saveUser operation for user. */
    @Test
    @Rollback
    public void saveUser_idNotSet_throwsTIdNotDefined()  {
        TUser tUser = createUser(null, "password", true);

        assertThatThrownBy(() -> handler.save(tUser)).isInstanceOf(TIdNotDefined.class);
    }

    /** Test saveUser operation for user. */
    @Test
    @Rollback
    public void saveUser_daoThrowsAborted_throwsTAborted() throws TAborted, TIdNotDefined,
            TErrorConnection, Aborted, ErrorConnection {
        TUser tUser = createUser("username", "password", true);

        doThrow(new Aborted("0", "Exception", "Exception")).when(handler.getUserDao()).saveUser(tUser);

        assertThatThrownBy(() -> handler.save(tUser)).isInstanceOf(TAborted.class);
    }

    /** Test saveUser operation for user. */
    @Test
    @Rollback
    public void saveUser_daoThrowsErrorConnection_throwsTErrorConnection() throws Aborted, ErrorConnection {
        TUser tUser = createUser("username", "password", true);

        doThrow(new ErrorConnection("0", "Exception")).when(handler.getUserDao()).saveUser(tUser);

        assertThatThrownBy(() -> handler.save(tUser)).isInstanceOf(TErrorConnection.class);
    }

    /** Test findByUsername operation for user. */
    @Test
    @Rollback
    public void findByUsername_correctInput_gotUser() throws NotFound, ErrorConnection, TNotFound, TErrorConnection {
        String username = "username";
        TUser tUser = createUser("username", "password", true);

        when(handler.getUserDao().findByUserName(username)).thenReturn(tUser);

        TUser gotTUser = handler.findByUserName(username);

        assertThat(gotTUser).isEqualTo(tUser);
    }

    /** Test findByUsername operation for user. */
    @Test
    @Rollback
    public void findByUsername_daoThrowsNotFound_throwsTNotFound() throws NotFound, ErrorConnection {
        String username = "username";

        when(handler.getUserDao().findByUserName(username)).thenThrow(new NotFound("0", "Exception"));

        assertThatThrownBy(() -> handler.findByUserName(username)).isInstanceOf(TNotFound.class);
    }

    /** Test findByUsername operation for user. */
    @Test
    @Rollback
    public void findByUsername_daoThrowsErrorConnection_throwsTErrorConnection() throws TAborted, TIdNotDefined,
            TErrorConnection, Aborted, ErrorConnection, NotFound, TNotFound {
        String username = "username";

        when(handler.getUserDao().findByUserName(username)).thenThrow(new ErrorConnection("0", "Exception"));

        assertThatThrownBy(() -> handler.findByUserName(username)).isInstanceOf(TErrorConnection.class);
    }

    /** Test updateUser operation for user. */
    @Test
    @Rollback
    public void updateUser_correctInput_userDaoCalled() throws TIdNotDefined, TErrorConnection, TAborted, Aborted,
            ErrorConnection {
        TUser tUser = createUser("username", "password", true);

        handler.update(tUser);

        verify(handler.getUserDao()).updateUser(tUser);
    }

    /** Test updateUser operation for user. */
    @Test
    @Rollback
    public void updateUser_idNotSet_throwsTIdNotDefined()  {
        TUser tUser = createUser(null, "password", true);

        assertThatThrownBy(() -> handler.update(tUser)).isInstanceOf(TIdNotDefined.class);
    }

    /** Test updateUser operation for user. */
    @Test
    @Rollback
    public void updateUser_daoThrowsAborted_throwsTAborted() throws Aborted, ErrorConnection {
        TUser tUser = createUser("username", "password", true);

        doThrow(new Aborted("0", "Exception", "Exception")).when(handler.getUserDao()).updateUser(tUser);

        assertThatThrownBy(() -> handler.update(tUser)).isInstanceOf(TAborted.class);
    }

    /** Test updateUser operation for user. */
    @Test
    @Rollback
    public void updateUser_daoThrowsErrorConnection_throwsTErrorConnection() throws Aborted, ErrorConnection {
        TUser tUser = createUser("username", "password", true);

        doThrow(new ErrorConnection("0", "Exception")).when(handler.getUserDao()).updateUser(tUser);

        assertThatThrownBy(() -> handler.update(tUser)).isInstanceOf(TErrorConnection.class);
    }

    private TUser createUser(String username, String password, boolean enabled) {
        TUser tUser = new TUser();
        tUser.setUsername(username);
        tUser.setPassword(password);
        tUser.setEnabled(enabled);
        return tUser;
    }
}
