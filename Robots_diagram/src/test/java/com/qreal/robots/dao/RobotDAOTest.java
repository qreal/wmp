/*
 * Copyright Denis Ageev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qreal.robots.dao;

import com.qreal.robots.dao.config.HibernateTestConfig;
import com.qreal.robots.model.auth.User;
import com.qreal.robots.model.robot.Robot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by dageev on 14.03.15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
public class RobotDAOTest extends BaseDAOTest {


    public static final String CODE = "CODE";
    @Autowired
    private RobotDAO robotDAO;

    @Autowired
    private UserDAO userDAO;


    @Test
    public void testFindByNameRobot() {
        User user = getAndSaveUser(USER_NAME, userDAO);
        Robot robot = new Robot(ROBOT_NAME, CODE, user);
        robotDAO.save(robot);

        Robot savedRobot = robotDAO.findByName(ROBOT_NAME);

        assertNotNull(savedRobot);
        assertEquals(robot.getName(), savedRobot.getName());
    }

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
