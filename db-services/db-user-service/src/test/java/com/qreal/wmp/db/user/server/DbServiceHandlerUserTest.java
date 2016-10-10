package com.qreal.wmp.db.user.server;

import com.qreal.wmp.db.user.config.AppInit;
import com.qreal.wmp.db.user.dao.UserDao;
import com.qreal.wmp.db.user.exceptions.AbortedException;
import com.qreal.wmp.db.user.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.user.exceptions.NotFoundException;
import com.qreal.wmp.thrift.gen.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
@ActiveProfiles("testHandler")
public class DbServiceHandlerUserTest {

    private UserDbServiceHandler handler;
    
    @Autowired
    private UserDao userDaoMocked;

    @Autowired
    private ApplicationContext context;

    @Before
    public void setMocking() {
        if (handler == null) {
            handler = new UserDbServiceHandler(context);
        }
    }

    @After
    public void deleteMocking() {
        reset(userDaoMocked);
    }

    /** Test saveUser operation for user. */
    @Test
    @Rollback
    public void saveUser_correctInput_userDaoCalled() throws Exception {
        TUser tUser = createUser("username", "password", true);

        handler.save(tUser);

        verify(userDaoMocked).saveUser(tUser);
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
    public void saveUser_daoThrowsAborted_throwsTAborted() throws Exception {
        TUser tUser = createUser("username", "password", true);

        doThrow(new AbortedException("0", "Exception", "Exception")).when(userDaoMocked).saveUser(tUser);

        assertThatThrownBy(() -> handler.save(tUser)).isInstanceOf(TAborted.class);
    }

    /** Test saveUser operation for user. */
    @Test
    @Rollback
    public void saveUser_daoThrowsErrorConnection_throwsTErrorConnection() throws Exception {
        TUser tUser = createUser("username", "password", true);

        doThrow(new ErrorConnectionException("0", "Exception")).when(userDaoMocked).saveUser(tUser);

        assertThatThrownBy(() -> handler.save(tUser)).isInstanceOf(TErrorConnection.class);
    }

    /** Test findByUsername operation for user. */
    @Test
    @Rollback
    public void findByUsername_correctInput_gotUser() throws Exception {
        String username = "username";
        TUser tUser = createUser("username", "password", true);

        when(userDaoMocked.findByUserName(username)).thenReturn(tUser);

        TUser gotTUser = handler.findByUserName(username);

        assertThat(gotTUser).isEqualTo(tUser);
    }

    /** Test findByUsername operation for user. */
    @Test
    @Rollback
    public void findByUsername_daoThrowsNotFound_throwsTNotFound() throws Exception {
        String username = "username";

        when(userDaoMocked.findByUserName(username)).thenThrow(new NotFoundException("0", "Exception"));

        assertThatThrownBy(() -> handler.findByUserName(username)).isInstanceOf(TNotFound.class);
    }

    /** Test findByUsername operation for user. */
    @Test
    @Rollback
    public void findByUsername_daoThrowsErrorConnection_throwsTErrorConnection() throws Exception {
        String username = "username";

        when(userDaoMocked.findByUserName(username)).thenThrow(new ErrorConnectionException("0", "Exception"));

        assertThatThrownBy(() -> handler.findByUserName(username)).isInstanceOf(TErrorConnection.class);
    }

    /** Test updateUser operation for user. */
    @Test
    @Rollback
    public void updateUser_correctInput_userDaoCalled() throws Exception {
        TUser tUser = createUser("username", "password", true);

        handler.update(tUser);

        verify(userDaoMocked).updateUser(tUser);
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
    public void updateUser_daoThrowsAborted_throwsTAborted() throws Exception {
        TUser tUser = createUser("username", "password", true);

        doThrow(new AbortedException("0", "Exception", "Exception")).when(userDaoMocked).updateUser(tUser);

        assertThatThrownBy(() -> handler.update(tUser)).isInstanceOf(TAborted.class);
    }

    /** Test updateUser operation for user. */
    @Test
    @Rollback
    public void updateUser_daoThrowsErrorConnection_throwsTErrorConnection() throws Exception {
        TUser tUser = createUser("username", "password", true);

        doThrow(new ErrorConnectionException("0", "Exception")).when(userDaoMocked).updateUser(tUser);

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
