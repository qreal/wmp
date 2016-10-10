package com.qreal.wmp.db.robot.config;

import com.qreal.wmp.db.robot.client.users.UserService;
import com.qreal.wmp.db.robot.dao.RobotDao;
import com.qreal.wmp.db.robot.dao.RobotDaoImpl;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@Configuration
public class Dao {

    @Configuration
    @Profile("default")
    public static class ProdConfig {
        @Bean(name = "robotDao")
        @Autowired
        RobotDao createDao(SessionFactory sessionFactory, @Qualifier("userService") UserService userService) {
            return new RobotDaoImpl(sessionFactory, userService);
        }
    }

    @Configuration
    @Profile("testDao")
    public static class TestDaoConfig {
        @Bean(name = "mockedUserService")
        UserService createUserService() {
            return mock(UserService.class);
        }


        @Bean(name = "robotDao")
        @Autowired
        RobotDao createDao(SessionFactory sessionFactory, @Qualifier("mockedUserService") UserService userService) {
            return new RobotDaoImpl(sessionFactory, userService);
        }
    }

    @Configuration
    @Profile("testHandler")
    public static class TestHandlerConfig {
        @Bean(name = "robotDao")
        RobotDao createDao() {
            return mock(RobotDao.class);
        }
    }
}
