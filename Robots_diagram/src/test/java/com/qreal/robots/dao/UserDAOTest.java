package com.qreal.robots.dao;

import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.authorization.model.auth.UserRole;
import com.qreal.robots.components.database.users.DAO.UserDAO;
import com.qreal.robots.components.database.users.DAO.UserDAOImpl;
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
public class UserDAOTest extends BaseDAOTest {


    @Autowired
    private UserDAO userDao;

    @Test
    public void testFindByName() {
        User user = getAndSaveUser(USER_NAME, userDao);

        User savedUser = userDao.findByUserName(USER_NAME);

        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.getUsername());


        Set<UserRole> roles = savedUser.getRoles();
        assertEquals(1, roles.size());
        assertEquals(UserDAOImpl.ROLE_USER, roles.iterator().next().getRole());
    }

    @Test
    public void testUserRole() {
        getAndSaveUser(USER_NAME2, userDao);
        User savedUser = userDao.findByUserName(USER_NAME2);

        Set<UserRole> roles = savedUser.getRoles();
        assertEquals(1, roles.size());
        assertEquals(UserDAOImpl.ROLE_USER, roles.iterator().next().getRole());
    }

    @Test
    public void testIsUserExist() {
        getAndSaveUser(USER_NAME3, userDao);
        assertTrue(userDao.isUserExist(USER_NAME3));
        assertFalse(userDao.isUserExist("ASDASD"));
    }


}
