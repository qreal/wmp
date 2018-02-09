package com.qreal.wmp.db.palette.config;

import com.qreal.wmp.db.palette.dao.PaletteDao;
import com.qreal.wmp.db.palette.dao.PaletteDaoImpl;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        @Bean(name = "paletteDao")
        @Autowired
        public PaletteDao createDao(SessionFactory sessionFactory) {
            return new PaletteDaoImpl(sessionFactory);
        }
    }

    @Configuration
    @Profile("testHandler")
    public static class TestHandlerConfig {
        @Bean(name = "paletteDao")
        public PaletteDao createDao() {
            return mock(PaletteDao.class);
        }
    }
}
