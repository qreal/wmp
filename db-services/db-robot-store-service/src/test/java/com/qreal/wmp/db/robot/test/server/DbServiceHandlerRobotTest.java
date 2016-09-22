package com.qreal.wmp.db.robot.test.server;

import com.qreal.wmp.db.robot.config.AppInit;
import com.qreal.wmp.db.robot.dao.RobotDao;
import com.qreal.wmp.db.robot.exceptions.Aborted;
import com.qreal.wmp.db.robot.exceptions.ErrorConnection;
import com.qreal.wmp.db.robot.exceptions.NotFound;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DbServiceHandlerRobotTest {
    public RobotDbServiceHandler handler;

    @Autowired
    public ApplicationContext context;

    @Before
    public void setMocking() {
        if (handler == null) {
            handler = new RobotDbServiceHandler(context);
        }
        handler.setRobotDao(mock(RobotDao.class));
    }

    @After
    public void deleteMocking() {
        handler.rewindRobotDao();
    }

    /** Test registerRobot operation for robot. */
    @Test
    @Rollback
    public void registerRobot_correctInput_robotDaoCalled() throws TAborted, TIdAlreadyDefined, Aborted {
        TRobot tRobot = createRobot("robot");
        RobotSerial robot = new RobotSerial(tRobot);

        handler.registerRobot(tRobot);

        verify(handler.getRobotDao()).saveRobot(robot);
    }

    /** Test registerRobot operation for robot. */
    @Test
    @Rollback
    public void registerRobot_idSet_throwsTIdAlreadyDefined() throws TAborted, TIdAlreadyDefined, Aborted {
        Long idRobot = 0L;
        TRobot tRobot = createRobot("robot", idRobot);

        assertThatThrownBy(() -> handler.registerRobot(tRobot)).isInstanceOf(TIdAlreadyDefined.class);
    }

    /** Test registerRobot operation for robot. */
    @Test
    @Rollback
    public void registerRobot_daoThrowsAborted_throwsTAborted() throws TAborted, TIdAlreadyDefined, Aborted {
        TRobot tRobot = createRobot("robot");
        RobotSerial robot = new RobotSerial(tRobot);

        when(handler.getRobotDao().saveRobot(robot)).thenThrow(new Aborted("0", "Exception", "Exception"));

        assertThatThrownBy(() -> handler.registerRobot(tRobot)).isInstanceOf(TAborted.class);
    }

    /** Test findById operation for robot. */
    @Test
    @Rollback
    public void findById_robotExists_returnsRobot() throws TAborted, TIdAlreadyDefined, Aborted, NotFound, TNotFound {
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", idRobot);
        RobotSerial robot = new RobotSerial(tRobot);

        when(handler.getRobotDao().getRobot(idRobot)).thenReturn(robot);

        TRobot gotTRobot = handler.findById(idRobot);

        assertThat(tRobot).isEqualTo(gotTRobot);
    }

    /** Test findById operation for robot. */
    @Test
    @Rollback
    public void findById_robotNotExists_throwsTNotFound() throws NotFound, TNotFound {
        long idRobotNotCorrect = 0L;

        when(handler.getRobotDao().getRobot(idRobotNotCorrect)).thenThrow(new NotFound("0", "Exception"));

        assertThatThrownBy(() -> handler.findById(idRobotNotCorrect)).isInstanceOf(TNotFound.class);
    }

    /** Test deleteRobot operation for robot. */
    @Test
    @Rollback
    public void deleteRobot_correctInput_robotDaoCalled() throws TErrorConnection, TAborted, Aborted, ErrorConnection {
        long idRobot = 0L;

        handler.deleteRobot(idRobot);

        verify(handler.getRobotDao()).deleteRobot(idRobot);
    }

    /** Test deleteRobot operation for robot. */
    @Test
    @Rollback
    public void deleteRobot_daoThrowsErrorConnection_throwsTErrorConnection() throws TErrorConnection, TAborted,
            Aborted, ErrorConnection {
        long idRobotNotCorrect = 0L;

        doThrow(new ErrorConnection("Exception", "Exception")).when(handler.getRobotDao()).
                deleteRobot(idRobotNotCorrect);

        assertThatThrownBy(() -> handler.deleteRobot(idRobotNotCorrect)).isInstanceOf(TErrorConnection.class);
    }

    /** Test deleteRobot operation for robot. */
    @Test
    @Rollback
    public void deleteRobot_daoThrowsAborted_throwsTAborted() throws TErrorConnection, TAborted,
            Aborted, ErrorConnection {
        long idRobotNotCorrect = 0L;

        doThrow(new Aborted("0", "Exception", "Exception")).when(handler.getRobotDao()).
                deleteRobot(idRobotNotCorrect);

        assertThatThrownBy(() -> handler.deleteRobot(idRobotNotCorrect)).isInstanceOf(TAborted.class);
    }

    /** Test isExists operation for robot. */
    @Test
    @Rollback
    public void isRobotExists_robotExists_returnsTrue() {
        long idRobot = 0L;

        when(handler.getRobotDao().isExistsRobot(idRobot)).thenReturn(true);

        assertThat(handler.isRobotExists(idRobot)).isTrue();
    }

    /** Test updateRobot operation for robot. */
    @Test
    @Rollback
    public void updateRobot_correctInput_robotDaoCalled() throws TIdNotDefined, TAborted, Aborted {
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", idRobot);
        RobotSerial robot = new RobotSerial(tRobot);

        handler.updateRobot(tRobot);

        verify(handler.getRobotDao()).updateRobot(robot);
    }

    /** Test updateRobot operation for robot. */
    @Test
    @Rollback
    public void updateRobot_idNotSet_throwsTIdNotDefined() throws TIdNotDefined, TAborted, Aborted {
        TRobot tRobot = createRobot("robot");

        assertThatThrownBy(() -> handler.updateRobot(tRobot)).isInstanceOf(TIdNotDefined.class);
    }

    /** Test updateRobot operation for robot. */
    @Test
    @Rollback
    public void updateRobot_daoThrowsAborted_throwsTAborted() throws TIdNotDefined, TAborted, Aborted {
        long idRobot = 0L;
        TRobot tRobot = createRobot("robot", idRobot);
        RobotSerial robot = new RobotSerial(tRobot);

        doThrow(new Aborted("0", "Exception", "Exception")).when(handler.getRobotDao()).
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
