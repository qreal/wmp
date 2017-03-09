package com.qreal.wmp.db.robot.dao;

import com.qreal.wmp.db.robot.client.users.UserService;
import com.qreal.wmp.db.robot.config.AppInit;
import com.qreal.wmp.db.robot.exceptions.AbortedException;
import com.qreal.wmp.db.robot.exceptions.NotFoundException;
import com.qreal.wmp.db.robot.model.robot.RobotSerial;
import com.qreal.wmp.thrift.gen.TRobot;
import com.qreal.wmp.thrift.gen.TUser;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ActiveProfiles("testDao")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DaoRobotTest {
    @Autowired
    private RobotDao robotDao;

    @Resource(name = "mockedUserService")
    private UserService userServiceMocked;

    @After
    public void deleteMocking() {
        reset(userServiceMocked);
    }

    /** Test saveRobot operation for robot. */
    @Test
    @Rollback
    public void saveRobot_correctInput_savesRobotInDb() throws Exception {
        RobotSerial robot = createRobot("robot", "ssid", "owner");
        long idRobot = robotDao.saveRobot(robot);
        robot.setId(idRobot);

        RobotSerial gotRobot = robotDao.getRobot(idRobot);

        assertThat(gotRobot).isEqualTo(robot);
    }

    /** Test saveRobot operation for robot. */
    @Test
    @Rollback
    public void getRobot_robotExists_gotRobot() throws Exception {
        RobotSerial robot = createAndSaveRobot("robot", "ssid", "owner");

        RobotSerial gotRobot = robotDao.getRobot(robot.getId());

        assertThat(gotRobot).isEqualTo(robot);
    }

    /** Test saveRobot operation for robot. */
    @Test
    @Rollback
    public void getRobot_robotNotExists_throwsNotFound() throws Exception {
        long idRobotNotCorrect = 0L;

        assertThatThrownBy(() -> robotDao.getRobot(idRobotNotCorrect)).isInstanceOf(NotFoundException.class);
    }

    /** Test deleteRobot operation for robot. */
    @Test
    @Rollback
    public void deleteRobot_correctInput_deletesRobotFromDb() throws Exception {
        RobotSerial robot = createAndSaveRobot("robot", "ssid", "owner");
        TRobot tRobot = robot.toTRobot();
        TUser owner = createUser("owner");
        addRobotToUser(owner, tRobot);

        robotDao.deleteRobot(tRobot.getId());

        assertThatThrownBy(() -> robotDao.getRobot(tRobot.getId())).isInstanceOf(NotFoundException.class);
    }

    /** Test deleteRobot operation for robot. */
    @Test
    @Rollback
    public void deleteRobot_correctInput_callFindInUserService() throws Exception {
        RobotSerial robot = createAndSaveRobot("robot", "ssid", "owner");
        TRobot tRobot = robot.toTRobot();
        TUser owner = createUser("owner");
        addRobotToUser(owner, tRobot);

        robotDao.deleteRobot(tRobot.getId());

        verify(userServiceMocked).getUser("owner");
    }

    /** Test deleteRobot operation for robot. */
    @Test
    @Rollback
    public void deleteRobot_correctInput_deleteRobotFromOwnerList() throws Exception {
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
    public void deleteRobot_correctInput_callUpdateInUserService() throws Exception {
        RobotSerial robot = createAndSaveRobot("robot", "ssid", "owner");
        TRobot tRobot = robot.toTRobot();
        TUser owner = createUser("owner");
        addRobotToUser(owner, tRobot);

        robotDao.deleteRobot(tRobot.getId());

        verify(userServiceMocked).updateUser(owner);
    }

    /** Test deleteRobot operation for robot. */
    @Test
    @Rollback
    public void deleteRobot_robotNotExists_throwsAborted() throws Exception {
        long idRobotNotCorrect = 0L;

        assertThatThrownBy(() -> robotDao.deleteRobot(idRobotNotCorrect)).isInstanceOf(AbortedException.class);
    }

    /** Test deleteRobot operation for robot. */
    @Test
    @Rollback
    public void deleteRobot_userNotExists_throwsAborted() throws Exception {
        String owner = "owner";
        RobotSerial testRobot = createAndSaveRobot("robot", "ssid", owner);

        when(userServiceMocked.getUser(owner)).thenThrow(new NotFoundException(owner, "Exception"));

        assertThatThrownBy(() -> robotDao.deleteRobot(testRobot.getId())).isInstanceOf(AbortedException.class);
    }

    /** Test exists operation for robot. */
    @Test
    @Rollback
    public void isExistsRobot_robotExists_returnsTrue() throws Exception {
        RobotSerial testRobot = createAndSaveRobot("robot", "ssid", "owner");

        assertThat(robotDao.isExistsRobot(testRobot.getId())).isTrue();
    }

    /** Test exists operation for robot. */
    @Test
    @Rollback
    public void isExistsRobot_robotNotExists_returnsFalse() throws Exception {
        long idRobotNotCorrect = 0L;

        assertThat(robotDao.isExistsRobot(idRobotNotCorrect)).isFalse();
    }

    /** Test updateUser operation for robot. */
    @Test
    @Rollback
    public void updateRobot_robotExists_updatesRobot() throws Exception {
        RobotSerial testRobot = createAndSaveRobot("robot", "ssid", "owner");

        RobotSerial changedRobot = createRobot("robotChanged", "ssidChanged", "ownerChanged");
        changedRobot.setId(testRobot.getId());

        robotDao.updateRobot(changedRobot);

        assertThat(changedRobot).isEqualTo(testRobot);
    }

    /** Test updateUser operation for robot. */
    @Test
    @Rollback
    public void updateRobot_robotNotExists_throwsAborted() throws Exception {
        long idRobotNotCorrect = 0L;
        RobotSerial changedRobot = createRobot("robotChanged", "ssidChanged", "ownerChanged");
        changedRobot.setId(idRobotNotCorrect);

        assertThatThrownBy(() -> robotDao.updateRobot(changedRobot)).isInstanceOf(AbortedException.class);
    }

    private RobotSerial createRobot(String name, String ssid, String owner) {
        RobotSerial robotSerial = new RobotSerial();
        robotSerial.setName(name);
        robotSerial.setSsid(ssid);
        robotSerial.setOwner(owner);
        return robotSerial;
    }

    private RobotSerial createAndSaveRobot(String name, String ssid, String owner) throws Exception {
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

    private void addRobotToUser(TUser tUser, TRobot tRobot) throws Exception {
        if (tUser.getRobots() == null) {
            tUser.setRobots(new HashSet<>());
        }
        tUser.getRobots().add(tRobot);
        when(userServiceMocked.getUser(tUser.getUsername())).thenReturn(tUser);
    }

}
