package com.qreal.wmp.db.robot.test.dao;

import com.qreal.wmp.db.robot.client.users.UserService;
import com.qreal.wmp.db.robot.config.AppInit;
import com.qreal.wmp.db.robot.dao.RobotDao;
import com.qreal.wmp.db.robot.exceptions.Aborted;
import com.qreal.wmp.db.robot.exceptions.ErrorConnection;
import com.qreal.wmp.db.robot.exceptions.NotFound;
import com.qreal.wmp.db.robot.model.robot.RobotSerial;
import com.qreal.wmp.thrift.gen.TRobot;
import com.qreal.wmp.thrift.gen.TUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DaoRobotTest {
    @Autowired
    public RobotDao robotDao;

    @Before
    public void setMocking() {
        UserService mockedUserService = mock(UserService.class);
        robotDao.setUserService(mockedUserService);
    }

    @After
    public void deleteMocking() {
        robotDao.rewindUserService();
    }

    /** Test saveRobot operation for robot. */
    @Test
    @Rollback
    public void saveRobot_correctInput_savesRobotInDb() throws Aborted, NotFound {
        RobotSerial robot = createRobot("robot", "ssid", "owner");
        long idRobot = robotDao.saveRobot(robot);
        robot.setId(idRobot);

        RobotSerial gotRobot = robotDao.getRobot(idRobot);

        assertThat(gotRobot).isEqualTo(robot);
    }

    /** Test saveRobot operation for robot. */
    @Test
    @Rollback
    public void getRobot_robotExists_gotRobot() throws Aborted, NotFound {
        RobotSerial robot = createAndSaveRobot("robot", "ssid", "owner");

        RobotSerial gotRobot = robotDao.getRobot(robot.getId());

        assertThat(gotRobot).isEqualTo(robot);
    }

    /** Test saveRobot operation for robot. */
    @Test
    @Rollback
    public void getRobot_robotNotExists_throwsNotFound() throws Aborted, NotFound {
        long idRobotNotCorrect = 0L;

        assertThatThrownBy(() -> robotDao.getRobot(idRobotNotCorrect)).isInstanceOf(NotFound.class);
    }

    /** Test deleteRobot operation for robot. */
    @Test
    @Rollback
    public void deleteRobot_correctInput_deletesRobotFromDb() throws Aborted, ErrorConnection, NotFound {
        RobotSerial robot = createAndSaveRobot("robot", "ssid", "owner");
        TRobot tRobot = robot.toTRobot();
        TUser owner = createUser("owner");
        addRobotToUser(owner, tRobot);

        robotDao.deleteRobot(tRobot.getId());

        assertThatThrownBy(() -> robotDao.getRobot(tRobot.getId())).isInstanceOf(NotFound.class);
    }

    /** Test deleteRobot operation for robot. */
    @Test
    @Rollback
    public void deleteRobot_correctInput_callFindInUserService() throws Aborted, ErrorConnection, NotFound {
        RobotSerial robot = createAndSaveRobot("robot", "ssid", "owner");
        TRobot tRobot = robot.toTRobot();
        TUser owner = createUser("owner");
        addRobotToUser(owner, tRobot);

        robotDao.deleteRobot(tRobot.getId());

        verify(robotDao.getUserService()).findByUserName("owner");
    }

    /** Test deleteRobot operation for robot. */
    @Test
    @Rollback
    public void deleteRobot_correctInput_deleteRobotFromOwnerList() throws Aborted, ErrorConnection, NotFound {
        RobotSerial robot = createAndSaveRobot("robot", "ssid", "owner");
        TRobot tRobot = robot.toTRobot();
        TUser owner = createUser("owner");
        addRobotToUser(owner, tRobot);

        robotDao.deleteRobot(tRobot.getId());

        assertThat(owner.getRobots().isEmpty()).isTrue();
    }

    /** Test deleteRobot operation for robot. */
    @Test
    @Rollback
    public void deleteRobot_correctInput_callUpdateInUserService() throws Aborted, ErrorConnection, NotFound {
        RobotSerial robot = createAndSaveRobot("robot", "ssid", "owner");
        TRobot tRobot = robot.toTRobot();
        TUser owner = createUser("owner");
        addRobotToUser(owner, tRobot);

        robotDao.deleteRobot(tRobot.getId());

        verify(robotDao.getUserService()).update(owner);
    }

    /** Test deleteRobot operation for robot. */
    @Test
    @Rollback
    public void deleteRobot_robotNotExists_throwsAborted() throws Aborted, ErrorConnection, NotFound {
        long idRobotNotCorrect = 0L;

        assertThatThrownBy(() -> robotDao.deleteRobot(idRobotNotCorrect)).isInstanceOf(Aborted.class);
    }

    /** Test deleteRobot operation for robot. */
    @Test
    @Rollback
    public void deleteRobot_userNotExists_throwsAborted() throws Aborted, ErrorConnection, NotFound {
        String owner = "owner";
        RobotSerial testRobot = createAndSaveRobot("robot", "ssid", owner);

        when(robotDao.getUserService().findByUserName(owner)).thenThrow(new NotFound(owner, "Exception"));

        assertThatThrownBy(() -> robotDao.deleteRobot(testRobot.getId())).isInstanceOf(Aborted.class);
    }

    /** Test exists operation for robot. */
    @Test
    @Rollback
    public void isExistsRobot_robotExists_returnsTrue() throws Aborted, NotFound, ErrorConnection {
        RobotSerial testRobot = createAndSaveRobot("robot", "ssid", "owner");

        assertThat(robotDao.isExistsRobot(testRobot.getId())).isTrue();
    }

    /** Test exists operation for robot. */
    @Test
    @Rollback
    public void isExistsRobot_robotNotExists_returnsFalse() throws Aborted, NotFound, ErrorConnection {
        long idRobotNotCorrect = 0L;

        assertThat(robotDao.isExistsRobot(idRobotNotCorrect)).isFalse();
    }

    /** Test update operation for robot. */
    @Test
    @Rollback
    public void updateRobot_robotExists_updatesRobot() throws Aborted, NotFound, ErrorConnection {
        RobotSerial testRobot = createAndSaveRobot("robot", "ssid", "owner");

        RobotSerial changedRobot = createRobot("robotChanged", "ssidChanged", "ownerChanged");
        changedRobot.setId(testRobot.getId());

        robotDao.updateRobot(changedRobot);

        assertThat(changedRobot).isEqualTo(testRobot);
    }

    /** Test update operation for robot. */
    @Test
    @Rollback
    public void updateRobot_robotNotExists_throwsAborted() throws Aborted, NotFound, ErrorConnection {
        long idRobotNotCorrect = 0L;
        RobotSerial changedRobot = createRobot("robotChanged", "ssidChanged", "ownerChanged");
        changedRobot.setId(idRobotNotCorrect);

        assertThatThrownBy(() -> robotDao.updateRobot(changedRobot)).isInstanceOf(Aborted.class);
    }

    private RobotSerial createRobot(String name, String ssid, String owner) {
        RobotSerial robotSerial = new RobotSerial();
        robotSerial.setName(name);
        robotSerial.setSsid(ssid);
        robotSerial.setOwner(owner);
        return robotSerial;
    }

    private RobotSerial createAndSaveRobot(String name, String ssid, String owner) throws Aborted {
        RobotSerial robotSerial = createRobot(name, ssid, owner);
        long idRobot = robotDao.saveRobot(robotSerial);
        robotSerial.setId(idRobot);
        return robotSerial;
    }

    private TUser createUser(String username) {
        TUser tUser = new TUser();
        tUser.setUsername(username);
        return tUser;
    }

    private void addRobotToUser(TUser tUser, TRobot tRobot) throws NotFound, ErrorConnection {
        if (tUser.getRobots() == null) {
            tUser.setRobots(new HashSet<>());
        }
        tUser.getRobots().add(tRobot);
        when(robotDao.getUserService().findByUserName(tUser.getUsername())).thenReturn(tUser);
    }

}
