package com.qreal.wmp.db.diagram.config;

import com.qreal.wmp.db.diagram.client.longpoll.LongpollService;
import com.qreal.wmp.db.diagram.dao.DiagramDao;
import com.qreal.wmp.db.diagram.dao.DiagramDaoImpl;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

/** DAO beans configuration.*/
@Configuration
public class Dao {
    @Configuration
    @Profile("default")
    public static class ProdConfig {
        @Bean(name = "diagramDao")
        @Autowired
        public DiagramDao createDao(SessionFactory sessionFactory,
                                    @Qualifier("longpollService") LongpollService longpollService) {
            return new DiagramDaoImpl(sessionFactory, longpollService);
        }
    }

    @Configuration
    @Profile("testHandler")
    public static class TestHandlerConfig {
        @Bean(name = "diagramDao")
        public DiagramDao createDao() {
            return mock(DiagramDao.class);
        }
    }
}
