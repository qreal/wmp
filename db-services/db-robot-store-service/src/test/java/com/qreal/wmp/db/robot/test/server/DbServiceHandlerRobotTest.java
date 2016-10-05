package com.qreal.wmp.db.robot.test.server;

import com.qreal.wmp.db.robot.config.AppInit;
import com.qreal.wmp.db.robot.dao.RobotDao;
import com.qreal.wmp.db.robot.exceptions.AbortedException;
import com.qreal.wmp.db.robot.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.robot.exceptions.NotFoundException;
import com.qreal.wmp.db.robot.model.robot.RobotSerial;
import com.qreal.wmp.db.robot.server.RobotDbServiceHandler;
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

@ActiveProfiles("testHandler")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DbServiceHandlerRobotTest {
    
    @Autowired
    private RobotDao robotDaoMocked;
    
    private RobotDbServiceHandler handler;

    @Autowired
    private ApplicationContext context;

    @Before
    public void setMocking() {
        if (handler == null) {
            handler = new RobotDbServiceHandler(context);
        }
    }

    @After
    public void deleteMocking() {
        reset(robotDaoMocked);
    }

    /** Test registerRobot operation for robot. */
    @Test
    @Rollback
    public void registerRobot_correctInput_robotDaoCalled() throws Exception {
        TRobot tRobot = createRobot("robot");
        RobotSerial robot = new RobotSerial(tRobot);

        handler.registerRobot(tRobot);

        verify(robotDaoMocked).saveRobot(robot);
    }

    /** Test registerRobot operation for robot. */
    @Test
    @Rollback
    public void registerRobot_idSet_throwsTIdAlreadyDefined() throws Exception {
        Long idRobot = 0L;
        TRobot tRobot = createRobot("robot", idRobot);

        assertThatThrownBy(() -> handler.registerRobot(tRobot)).isInstanceOf(TIdAlreadyDefined.class);
    }

    /** Test registerRobot operation for robot. */
    @Test
    @Rollback
    public void registerRobot_daoThrowsAborted_throwsTAborted() throws Exception {
        TRobot tRobot = createRobot("robot");
        RobotSerial robot = new RobotSerial(tRobot);

        when(robotDaoMocked.saveRobot(robot)).thenThrow(new AbortedException("0", "Exception", "Exception"));

        assertThatThrownBy(() -> handler.registerRobot(tRobot)).isInstanceOf(TAborted.class);
    }

    /** Test findById operation for robot. */
    @Test
    @Rollback
    public void findById_robotExists_returnsRobot() throws Exception {
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", idRobot);
        RobotSerial robot = new RobotSerial(tRobot);

        when(robotDaoMocked.getRobot(idRobot)).thenReturn(robot);

        TRobot gotTRobot = handler.findById(idRobot);

        assertThat(tRobot).isEqualTo(gotTRobot);
    }

    /** Test findById operation for robot. */
    @Test
    @Rollback
    public void findById_robotNotExists_throwsTNotFound() throws Exception {
        long idRobotNotCorrect = 0L;

        when(robotDaoMocked.getRobot(idRobotNotCorrect)).thenThrow(new NotFoundException("0", "Exception"));

        assertThatThrownBy(() -> handler.findById(idRobotNotCorrect)).isInstanceOf(TNotFound.class);
    }

    /** Test deleteRobot operation for robot. */
    @Test
    @Rollback
    public void deleteRobot_correctInput_robotDaoCalled() throws Exception {
        long idRobot = 0L;

        handler.deleteRobot(idRobot);

        verify(robotDaoMocked).deleteRobot(idRobot);
    }

    /** Test deleteRobot operation for robot. */
    @Test
    @Rollback
    public void deleteRobot_daoThrowsErrorConnection_throwsTErrorConnection() throws Exception {
        long idRobotNotCorrect = 0L;

        doThrow(new ErrorConnectionException("Exception", "Exception")).when(robotDaoMocked).
                deleteRobot(idRobotNotCorrect);

        assertThatThrownBy(() -> handler.deleteRobot(idRobotNotCorrect)).isInstanceOf(TErrorConnection.class);
    }

    /** Test deleteRobot operation for robot. */
    @Test
    @Rollback
    public void deleteRobot_daoThrowsAborted_throwsTAborted() throws Exception {
        long idRobotNotCorrect = 0L;

        doThrow(new AbortedException("0", "Exception", "Exception")).when(robotDaoMocked).
                deleteRobot(idRobotNotCorrect);

        assertThatThrownBy(() -> handler.deleteRobot(idRobotNotCorrect)).isInstanceOf(TAborted.class);
    }

    /** Test isExists operation for robot. */
    @Test
    @Rollback
    public void isRobotExists_robotExists_returnsTrue() {
        long idRobot = 0L;

        when(robotDaoMocked.isExistsRobot(idRobot)).thenReturn(true);

        assertThat(handler.isRobotExists(idRobot)).isTrue();
    }

    /** Test updateRobot operation for robot. */
    @Test
    @Rollback
    public void updateRobot_correctInput_robotDaoCalled() throws Exception {
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", idRobot);
        RobotSerial robot = new RobotSerial(tRobot);

        handler.updateRobot(tRobot);

        verify(robotDaoMocked).updateRobot(robot);
    }

    /** Test updateRobot operation for robot. */
    @Test
    @Rollback
    public void updateRobot_idNotSet_throwsTIdNotDefined() throws Exception {
        TRobot tRobot = createRobot("robot");

        assertThatThrownBy(() -> handler.updateRobot(tRobot)).isInstanceOf(TIdNotDefined.class);
    }

    /** Test updateRobot operation for robot. */
    @Test
    @Rollback
    public void updateRobot_daoThrowsAborted_throwsTAborted() throws Exception {
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", idRobot);
        RobotSerial robot = new RobotSerial(tRobot);

        doThrow(new AbortedException("0", "Exception", "Exception")).when(robotDaoMocked).
                updateRobot(robot);

        assertThatThrownBy(() -> handler.updateRobot(tRobot)).isInstanceOf(TAborted.class);
    }

    private TRobot createRobot(String name) {
        TRobot tRobot = new TRobot();
        tRobot.setName(name);
        return tRobot;
    }

    private TRobot createRobot(String name, Long id) {
        TRobot tRobot = new TRobot();
        tRobot.setName(name);
        tRobot.setId(id);
        return tRobot;
    }
}
