package com.qreal.wmp.db.user.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/** Declares a transaction manager for Hibernate and its session factory bean. */
@Configuration
@EnableTransactionManagement
public class Hibernate {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private Properties hibernateProperties;

    /** Factory of DB sessions. Sessions are created using dataSource bean. */
    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
        sessionBuilder.scanPackages("com.qreal.wmp.db.user.model");
        sessionBuilder.addProperties(hibernateProperties);
        return sessionBuilder.buildSessionFactory();
    }

    /** Transaction manager for a session factory. No support for distributed transactions so far. */
    @Bean
    public HibernateTransactionManager txManager() {
        return new HibernateTransactionManager(sessionFactory());
    }
}
