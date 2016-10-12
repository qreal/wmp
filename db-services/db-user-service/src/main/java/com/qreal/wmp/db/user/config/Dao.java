package com.qreal.wmp.db.user.config;

import com.qreal.wmp.db.user.client.diagrams.DiagramService;
import com.qreal.wmp.db.user.client.robots.RobotService;
import com.qreal.wmp.db.user.dao.UserDao;
import com.qreal.wmp.db.user.dao.UserDaoImpl;
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
        @Bean(name = "userDao")
        @Autowired
        UserDao createDao(SessionFactory sessionFactory, @Qualifier("robotService") RobotService robotService,
                          @Qualifier("diagramService") DiagramService diagramService) {
            return new UserDaoImpl(sessionFactory, robotService, diagramService);
        }
    }

    @Configuration
    @Profile("testDao")
    public static class TestDaoConfig {
        @Bean(name = "mockedRobotService")
        RobotService createRobotService() {
            return mock(RobotService.class);
        }

        @Bean(name = "mockedDiagramService")
        DiagramService createDiagramService() {
            return mock(DiagramService.class);
        }

        @Bean(name = "userDao")
        @Autowired
        UserDao createDao(SessionFactory sessionFactory, @Qualifier("mockedRobotService") RobotService robotService,
                          @Qualifier("mockedDiagramService") DiagramService diagramService) {
            return new UserDaoImpl(sessionFactory, robotService, diagramService);
        }
    }

    @Configuration
    @Profile("testHandler")
    public static class TestHandlerConfig {
        @Bean(name = "userDao")
        UserDao createDao() {
            return mock(UserDao.class);
        }
    }
}
