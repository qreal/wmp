package com.qreal.robots.common.config.production;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("production")
public class ProdDataSourceConfig {

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/diagram");
        dataSource.setUsername("user");
        dataSource.setPassword("user");
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnBorrow(true);

        return dataSource;
    }

}
