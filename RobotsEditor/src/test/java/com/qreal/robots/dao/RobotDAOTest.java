package com.qreal.robots.dao;

import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.dashboard.model.robot.Robot;
import com.qreal.robots.components.database.robots.dao.RobotDAO;
import com.qreal.robots.components.database.users.dao.UserDAO;
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
public class RobotDAOTest extends BaseDAOTest {

    public static final String CODE = "CODE";

    @Autowired
    private RobotDAO robotDAO;

    @Autowired
    private UserDAO userDAO;

    @Test
    public void testSaveRobot() {
        User user = getAndSaveUser(USER_NAME2, userDAO);
        Robot robot = new Robot(ROBOT_NAME2, CODE, user);
        robotDAO.save(robot);

        User savedUser = userDAO.findByUserName(USER_NAME2);

        Set<Robot> robots = savedUser.getRobots();
        assertEquals(1, robots.size());
        assertEquals(robot.getName(), robots.iterator().next().getName());
    }

    @Test
    public void testDelete() {
        User user = getAndSaveUser(USER_NAME3, userDAO);
        Robot robot = new Robot(ROBOT_NAME, CODE, user);
        robotDAO.save(robot);
        assertTrue(robotDAO.isRobotExists(ROBOT_NAME));
        robotDAO.delete(robot);
        assertFalse(robotDAO.isRobotExists(ROBOT_NAME));
    }
}
