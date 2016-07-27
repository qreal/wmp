package com.qreal.robots.dao;

import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.dashboard.model.robot.Robot;
import com.qreal.robots.components.database.robots.dao.RobotDao;
import com.qreal.robots.components.database.users.dao.UserDao;
import com.qreal.robots.dao.config.HibernateTestConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
public class RobotDaoTest extends BaseDaoTest {

    public static final String CODE = "CODE";

    @Autowired
    private RobotDao robotDao;

    @Autowired
    private UserDao userDao;

    @Test
    public void testSaveRobot() {
        User user = getAndSaveUser(USER_NAME2, userDao);
        Robot robot = new Robot(ROBOT_NAME2, CODE, user);
        robotDao.save(robot);

        User savedUser = userDao.findByUserName(USER_NAME2);

        Set<Robot> robots = savedUser.getRobots();
        assertEquals(1, robots.size());
        assertEquals(robot.getName(), robots.iterator().next().getName());
    }

    @Test
    public void testDelete() {
        User user = getAndSaveUser(USER_NAME3, userDao);
        Robot robot = new Robot(ROBOT_NAME, CODE, user);
        robotDao.save(robot);
        assertTrue(robotDao.isRobotExists(ROBOT_NAME));
        robotDao.delete(robot);
        assertFalse(robotDao.isRobotExists(ROBOT_NAME));
    }
}
