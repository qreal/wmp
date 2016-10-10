package com.qreal.wmp.db.user.dao;

import com.qreal.wmp.db.user.client.diagrams.DiagramService;
import com.qreal.wmp.db.user.client.robots.RobotService;
import com.qreal.wmp.db.user.config.AppInit;
import com.qreal.wmp.db.user.exceptions.AbortedException;
import com.qreal.wmp.db.user.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.user.exceptions.NotFoundException;
import com.qreal.wmp.thrift.gen.TRobot;
import com.qreal.wmp.thrift.gen.TUser;
import com.qreal.wmp.thrift.gen.TUserRole;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
@ActiveProfiles("testDao")
public class DaoUserTest {
    @Autowired
    private UserDao userDao;

    @Resource(name = "mockedRobotService")
    private RobotService robotServiceMocked;

    @Resource(name = "mockedDiagramService")
    private DiagramService diagramServiceMocked;

    @After
    public void deleteMocking() {
        reset(robotServiceMocked);
        reset(diagramServiceMocked);
    }

    /** Test saveUser operation for user. */
    @Test
    @Rollback
    public void saveUser_correctInput_savesUserInDb() throws Exception {
        TUser tUser = createUser("username", "password", true);
        addRoleToUser(tUser, "ROLE_USER");
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", "ssid", tUser.getUsername());
        addRobotToUser(tRobot, tUser);

        when(robotServiceMocked.register(tRobot)).thenReturn(idRobot);
        userDao.saveUser(tUser);

        when(robotServiceMocked.findById(idRobot)).thenReturn(tRobot);
        TUser gotUser = userDao.findByUserName("username");
        //To add id we need to recalculate hash. So we need to create whole new object
        updateUserRolesIds(tUser, gotUser);

        assertThat(gotUser).isEqualTo(tUser);
    }

    /** Test saveUser operation for user. */
    @Test
    @Rollback
    public void saveUser_correctInput_createsRootFolder() throws Exception {
        TUser tUser = createUser("username", "password", true);
        addRoleToUser(tUser, "ROLE_USER");
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", "ssid", tUser.getUsername());
        addRobotToUser(tRobot, tUser);

        when(robotServiceMocked.register(tRobot)).thenReturn(idRobot);
        userDao.saveUser(tUser);

        when(robotServiceMocked.findById(idRobot)).thenReturn(tRobot);
        TUser gotUser = userDao.findByUserName("username");

        //To add id we need to recalculate hash. So we need to create whole new object
        updateUserRolesIds(tUser, gotUser);

        verify(diagramServiceMocked).createRootFolder(tUser.getUsername());
    }

    /** Test saveUser operation for user. */
    @Test
    @Rollback
    public void saveUser_correctInput_registerRobot() throws Exception {
        TUser tUser = createUser("username", "password", true);
        addRoleToUser(tUser, "ROLE_USER");
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", "ssid", tUser.getUsername());
        addRobotToUser(tRobot, tUser);

        when(robotServiceMocked.register(tRobot)).thenReturn(idRobot);
        userDao.saveUser(tUser);

        when(robotServiceMocked.findById(idRobot)).thenReturn(tRobot);
        TUser gotUser = userDao.findByUserName("username");

        //To add id we need to recalculate hash. So we need to create whole new object
        updateUserRolesIds(tUser, gotUser);

        verify(robotServiceMocked).register(tRobot);
    }

    /** Test saveUser operation for user. */
    @Test
    @Rollback
    public void saveUser_clientRobotsThrowsAborted_throwsAborted() throws Exception {
        TUser tUser = createUser("username", "password", true);
        addRoleToUser(tUser, "ROLE_USER");
        TRobot tRobot = createRobot("robot", "ssid", tUser.getUsername());
        addRobotToUser(tRobot, tUser);

        when(robotServiceMocked.register(tRobot)).thenThrow(new AbortedException("0", "Exception", "Exception"));

        assertThatThrownBy(() -> userDao.saveUser(tUser)).isInstanceOf(AbortedException.class);
    }

    /** Test saveUser operation for user. */
    @Test
    @Rollback
    public void saveUser_clientDiagramThrowsAborted_throwsAborted() throws Exception {
        TUser tUser = createUser("username", "password", true);
        addRoleToUser(tUser, "ROLE_USER");
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", "ssid", tUser.getUsername());
        addRobotToUser(tRobot, tUser);

        when(robotServiceMocked.register(tRobot)).thenReturn(idRobot);

        doThrow(new AbortedException("0", "Exception", "Exception")).when(diagramServiceMocked).
                createRootFolder(tUser.getUsername());

        assertThatThrownBy(() -> userDao.saveUser(tUser)).isInstanceOf(AbortedException.class);
    }

    /** Test saveUser operation for user. */
    @Test
    @Rollback
    public void saveUser_clientRobotsThrowsErrorConnection_throwsErrorConnection() throws Exception {
        TUser tUser = createUser("username", "password", true);
        addRoleToUser(tUser, "ROLE_USER");
        TRobot tRobot = createRobot("robot", "ssid", tUser.getUsername());
        addRobotToUser(tRobot, tUser);

        when(robotServiceMocked.register(tRobot)).thenThrow(new ErrorConnectionException("0", "Exception"));

        assertThatThrownBy(() -> userDao.saveUser(tUser)).isInstanceOf(ErrorConnectionException.class);
    }

    /** Test saveUser operation for user. */
    @Test
    @Rollback
    public void saveUser_clientDiagramThrowsErrorConnection_throwsErrorConnection() throws Exception {
        TUser tUser = createUser("username", "password", true);
        addRoleToUser(tUser, "ROLE_USER");
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", "ssid", tUser.getUsername());
        addRobotToUser(tRobot, tUser);

        when(robotServiceMocked.register(tRobot)).thenReturn(idRobot);
        doThrow(new ErrorConnectionException("0", "Exception")).when(diagramServiceMocked).
                createRootFolder(tUser.getUsername());

        assertThatThrownBy(() -> userDao.saveUser(tUser)).isInstanceOf(ErrorConnectionException.class);
    }

    /** Test findByUsername operation for user. */
    @Test
    @Rollback
    public void findByUsername_correctInput_gotUser() throws Exception {
        TUser tUser = createUser("username", "password", true);
        addRoleToUser(tUser, "ROLE_USER");
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", "ssid", tUser.getUsername());
        addRobotToUser(tRobot, tUser);
        when(robotServiceMocked.register(tRobot)).thenReturn(idRobot);
        userDao.saveUser(tUser);

        when(robotServiceMocked.findById(idRobot)).thenReturn(tRobot);
        TUser gotUser = userDao.findByUserName("username");

        //To add id we need to recalculate hash. So we need to create whole new object
        updateUserRolesIds(tUser, gotUser);

        assertThat(gotUser).isEqualTo(tUser);
    }

    /** Test findByUsername operation for user. */
    @Test
    @Rollback
    public void findByUsername_userNotExists_throwsNotFound() {
        String usernameNotCorrect = "username";

        assertThatThrownBy(() -> userDao.findByUserName(usernameNotCorrect)).isInstanceOf(NotFoundException.class);
    }

    /** Test findByUsername operation for user. */
    @Test
    @Rollback
    public void findByUsername_clientRobotThrowsErrorConnection_throwsErrorConnection() throws Exception {
        String username = "username";
        TUser tUser = createUser(username, "password", true);
        addRoleToUser(tUser, "ROLE_USER");
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", "ssid", tUser.getUsername());
        addRobotToUser(tRobot, tUser);

        when(robotServiceMocked.register(tRobot)).thenReturn(idRobot);
        userDao.saveUser(tUser);
        when(robotServiceMocked.findById(idRobot)).thenThrow(new ErrorConnectionException("0", "Exception"));

        assertThatThrownBy(() -> userDao.findByUserName(username)).isInstanceOf(ErrorConnectionException.class);
    }


    /** Test delete operation for user. */
    @Test
    @Rollback
    public void updateUser_correctInput_updatesUserInDb() throws Exception {
        TUser tUser = createUser("username", "password", true);
        addRoleToUser(tUser, "ROLE_USER");
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", "ssid", tUser.getUsername());
        addRobotToUser(tRobot, tUser);

        when(robotServiceMocked.register(tRobot)).thenReturn(idRobot);
        userDao.saveUser(tUser);

        when(robotServiceMocked.findById(idRobot)).thenReturn(tRobot);
        TUser gotUser = userDao.findByUserName("username");
        //To add id we need to recalculate hash. So we need to create whole new object
        updateUserRolesIds(tUser, gotUser);


        //change user
        TUser tUserChanged = createUser("username", "passwordChanged", true);
        addRoleToUser(tUserChanged, "ROLE_USER");
        TRobot tRobotChanged = createRobot("robotChanged", "ssidChanhed", tUser.getUsername(), idRobot);
        addRobotToUser(tRobotChanged, tUserChanged);

        when(robotServiceMocked.findById(idRobot)).thenReturn(tRobotChanged);

        userDao.updateUser(tUserChanged);

        gotUser = userDao.findByUserName("username");
        //To add id we need to recalculate hash. So we need to create whole new object
        updateUserRolesIds(tUserChanged, gotUser);


        assertThat(gotUser).isEqualTo(tUserChanged);
    }

    /** Test delete operation for user. */
    @Test
    @Rollback
    public void updateUser_correctInput_updatesUsersRobots() throws Exception {
        TUser tUser = createUser("username", "password", true);
        addRoleToUser(tUser, "ROLE_USER");
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", "ssid", tUser.getUsername());
        addRobotToUser(tRobot, tUser);

        when(robotServiceMocked.register(tRobot)).thenReturn(idRobot);
        userDao.saveUser(tUser);

        when(robotServiceMocked.findById(idRobot)).thenReturn(tRobot);
        TUser gotUser = userDao.findByUserName("username");
        //To add id we need to recalculate hash. So we need to create whole new object
        updateUserRolesIds(tUser, gotUser);


        //change user
        TUser tUserChanged = createUser("username", "passwordChanged", true);
        addRoleToUser(tUserChanged, "ROLE_USER");
        TRobot tRobotChanged = createRobot("robotChanged", "ssidChanhed", tUser.getUsername(), idRobot);
        addRobotToUser(tRobotChanged, tUserChanged);

        when(robotServiceMocked.findById(idRobot)).thenReturn(tRobotChanged);

        userDao.updateUser(tUserChanged);

        gotUser = userDao.findByUserName("username");
        //To add id we need to recalculate hash. So we need to create whole new object
        updateUserRolesIds(tUserChanged, gotUser);


        verify(robotServiceMocked).update(tRobotChanged);
    }

    /** Test delete operation for user. */
    @Test
    @Rollback
    public void updateUser_notExistsUser_throwsAborted() {
        TUser tUser = createUser("username", "password", true);

        assertThatThrownBy(() -> userDao.updateUser(tUser)).isInstanceOf(AbortedException.class);
    }

    /** Test delete operation for user. */
    @Test
    @Rollback
    public void updateUser_clientRobotThrowsAborted_throwsAborted() throws Exception {
        TUser tUser = createUser("username", "password", true);
        addRoleToUser(tUser, "ROLE_USER");
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", "ssid", tUser.getUsername());
        addRobotToUser(tRobot, tUser);

        when(robotServiceMocked.register(tRobot)).thenReturn(idRobot);
        userDao.saveUser(tUser);

        when(robotServiceMocked.findById(idRobot)).thenReturn(tRobot);
        TUser gotUser = userDao.findByUserName("username");
        //To add id we need to recalculate hash. So we need to create whole new object
        updateUserRolesIds(tUser, gotUser);

        //change user
        TUser tUserChanged = createUser("username", "passwordChanged", true);
        addRoleToUser(tUserChanged, "ROLE_USER");
        TRobot tRobotChanged = createRobot("robotChanged", "ssidChanhed", tUser.getUsername(), idRobot);
        addRobotToUser(tRobotChanged, tUserChanged);

        doThrow(new AbortedException("0", "Exception", "Exception")).when(robotServiceMocked).
                update(tRobotChanged);
        assertThatThrownBy(() -> userDao.updateUser(tUserChanged)).isInstanceOf(AbortedException.class);
    }

    /** Test delete operation for user. */
    @Test
    @Rollback
    public void updateUser_clientRobotThrowsErrorConnection_throwsErrorConnection() throws Exception {
        TUser tUser = createUser("username", "password", true);
        addRoleToUser(tUser, "ROLE_USER");
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", "ssid", tUser.getUsername());
        addRobotToUser(tRobot, tUser);

        when(robotServiceMocked.register(tRobot)).thenReturn(idRobot);
        userDao.saveUser(tUser);

        when(robotServiceMocked.findById(idRobot)).thenReturn(tRobot);
        TUser gotUser = userDao.findByUserName("username");
        //To add id we need to recalculate hash. So we need to create whole new object
        updateUserRolesIds(tUser, gotUser);

        //change user
        TUser tUserChanged = createUser("username", "passwordChanged", true);
        addRoleToUser(tUserChanged, "ROLE_USER");
        TRobot tRobotChanged = createRobot("robotChanged", "ssidChanhed", tUser.getUsername(), idRobot);
        addRobotToUser(tRobotChanged, tUserChanged);

        doThrow(new ErrorConnectionException("0", "Exception")).when(robotServiceMocked).update(tRobotChanged);
        assertThatThrownBy(() -> userDao.updateUser(tUserChanged)).isInstanceOf(ErrorConnectionException.class);
    }

    /** Test isExistsUser operation for user. */
    @Test
    @Rollback
    public void isExistsUser_userExists_returnsTrue() throws Exception {
        String username = "username";

        TUser tUser = createUser(username, "password", true);
        addRoleToUser(tUser, "ROLE_USER");
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", "ssid", tUser.getUsername());
        addRobotToUser(tRobot, tUser);

        when(robotServiceMocked.register(tRobot)).thenReturn(idRobot);
        userDao.saveUser(tUser);

        assertThat(userDao.isExistsUser(username)).isTrue();
    }

    /** Test saveUser operation for user. */
    @Test
    @Rollback
    public void isExistsUser_userNotExists_returnsFalse() {
        String usernameNotCorrect = "username";

        assertThat(userDao.isExistsUser(usernameNotCorrect)).isFalse();
    }

    private TUser createUser(String username, String password, boolean enabled) {
        TUser tUser = new TUser();
        tUser.setUsername(username);
        tUser.setPassword(password);
        tUser.setEnabled(enabled);
        return tUser;
    }

    private void addRoleToUser(TUser tUser, String role) {
        TUserRole tUserRole = new TUserRole();
        tUserRole.setRole(role);
        if (tUser.getRoles() == null) {
            tUser.setRoles(new HashSet<>());
        }
        tUser.getRoles().add(tUserRole);
    }

    private TRobot createRobot(String name, String ssid, String username) {
        TRobot tRobot = new TRobot();

        tRobot.setName(name);
        tRobot.setSsid(ssid);
        tRobot.setUsername(username);

        return tRobot;
    }

    private TRobot createRobot(String name, String ssid, String username, Long id) {
        TRobot tRobot = createRobot(name, ssid, username);
        tRobot.setId(id);
        return tRobot;
    }

    private void addRobotToUser(TRobot tRobot, TUser tUser) {
        if (tUser.getRobots() == null) {
            tUser.setRobots(new HashSet<>());
        }
        tUser.getRobots().add(tRobot);
    }

    private void updateUserRolesIds(TUser userNotSaved, TUser userSaved) {
        Set<TUserRole> newRoles = new HashSet<>();
        for (TUserRole role : userNotSaved.getRoles()) {
            for (TUserRole roleSaved : userSaved.getRoles()) {
                if (roleSaved.getRole().equals(role.getRole())) {
                    newRoles.add(roleSaved);
                    break;
                }
            }
        }
        userNotSaved.setRoles(newRoles);
    }
}
