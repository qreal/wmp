package com.qreal.wmp.auth.config.database;

import java.util.Properties;

import javax.sql.DataSource;

import com.qreal.wmp.auth.database.client.ClientDAOSec;
import com.qreal.wmp.auth.database.client.Client;
import com.qreal.wmp.auth.database.client.ClientDAO;
import com.qreal.wmp.auth.database.users.User;
import com.qreal.wmp.auth.database.users.UserAuthority;
import com.qreal.wmp.auth.database.users.UserDAO;
import com.qreal.wmp.auth.database.users.UserDAOSec;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Development configuration of Hibernate ORM.
 * In development used H2 in-memory database and create-drop strategy of start.
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class HibernateConfig {
    /** Provides access to DB.*/
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUsername("sa");
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS diagram");
        dataSource.setPassword("");
        return dataSource;
    }

    /** Configuration properties of DB.*/
    @Bean(name = "hibernateProperties")
    public Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.hbm2ddl.import_files", "data.sql");
        return properties;
    }

    /** Factory of DB sessions. Sessions created using dataSource bean.*/
    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) {
        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
        sessionBuilder.addProperties(getHibernateProperties());
        sessionBuilder.addAnnotatedClasses(User.class, UserAuthority.class);
        sessionBuilder.addAnnotatedClasses(Client.class);
        sessionBuilder.scanPackages("com.qreal.wmp.auth.database");
        return sessionBuilder.buildSessionFactory();
    }

    /** Transaction manager for session factory. No support for distributed transactions.*/
    @Autowired
    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

    /** DAO for user entity in local DB.*/
    @Autowired
    @DependsOn("transactionManager")
    @Bean(name = "userService")
    public UserDAO getUserDao(SessionFactory sessionFactory) {
        UserDAO userService = new UserDAO();
        userService.setSessionFactory(sessionFactory);
        return userService;
    }

    /** Adapter from userService to Spring Framework Security class UserDetailsService. */
    @Autowired
    @Bean(name = "userServiceSec")
    public UserDAOSec getUserDaoSec(UserDAO userService) {
        UserDAOSec userServiceSec = new UserDAOSec();
        userServiceSec.setUserService(userService);
        return userServiceSec;
    }

    /** DAO for client entity in local DB.*/
    @Autowired
    @DependsOn("transactionManager")
    @Bean(name = "clientService")
    public ClientDAO getClientDao(SessionFactory sessionFactory) {
        ClientDAO clientService = new ClientDAO();
        clientService.setSessionFactory(sessionFactory);
        return clientService;
    }

    /** Adapter from userService to Spring Framework Security OAuth2 class ClientDetailsService. */
    @Autowired
    @Bean(name = "clientServiceSec")
    public ClientDAOSec getClientDaoSec(ClientDAO clientService) {
        ClientDAOSec clientServiceSec = new ClientDAOSec();
        clientServiceSec.setClientService(clientService);
        return clientServiceSec;
    }

    /** Encoder hashes password of users.*/
    @Bean(name = "passwordEncoder")
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}