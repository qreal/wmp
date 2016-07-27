package com.qreal.robots.common.config.development;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Properties;

@Configuration
@Profile("development")
public class DevHibernateConfig {

    @Bean(name = "hibernateProperties")
    public Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.hbm2ddl.import_files", "data.sql");
        return properties;
    }

}
