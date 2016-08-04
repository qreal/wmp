package com.resources.auth.config.database;

import java.util.Properties;

import javax.sql.DataSource;

import com.resources.auth.database.client.Client;
import com.resources.auth.database.client.ClientDAO;
import com.resources.auth.database.client.ClientDAOSec;
import com.resources.auth.database.users.*;
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

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class HibernateConfig {

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUsername("sa");
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS diagram");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean(name = "hibernateProperties")
    public Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.hbm2ddl.import_files", "data.sql");
        return properties;
    }

    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) {
        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
        sessionBuilder.addProperties(getHibernateProperties());
        sessionBuilder.addAnnotatedClasses(User.class, UserAuthority.class);
        sessionBuilder.addAnnotatedClasses(Client.class);
        sessionBuilder.scanPackages("com.resources.auth.Database");
        return sessionBuilder.buildSessionFactory();
    }

    @Autowired
    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

    @Autowired
    @DependsOn("transactionManager")
    @Bean(name = "userService")
    public UserDAO getUserDao(SessionFactory sessionFactory) {
        UserDAO userService = new UserDAO();
        userService.setSessionFactory(sessionFactory);
        return userService;
    }

    @Autowired
    @Bean(name = "userServiceSec")
    public UserDAOSec getUserDaoSec(UserDAO userService) {
        UserDAOSec userServiceSec = new UserDAOSec();
        userServiceSec.setUserService(userService);
        return userServiceSec;
    }

    @Autowired
    @DependsOn("transactionManager")
    @Bean(name = "clientService")
    public ClientDAO getClientDao(SessionFactory sessionFactory) {
        ClientDAO clientService = new ClientDAO();
        clientService.setSessionFactory(sessionFactory);
        return clientService;
    }

    @Autowired
    @Bean(name = "clientServiceSec")
    public ClientDAOSec getClientDaoSec(ClientDAO clientService) {
        ClientDAOSec clientServiceSec = new ClientDAOSec();
        clientServiceSec.setClientService(clientService);
        return clientServiceSec;
    }

    @Bean(name = "passwordEncoder")
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}