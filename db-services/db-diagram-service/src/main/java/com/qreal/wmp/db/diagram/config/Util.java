package com.qreal.wmp.db.diagram.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/** Creates beans for Spring needs. **/
@Configuration
public class Util {
    /** Processor for @Value annotations linked to property files.*/
    @Bean(name = "propertyPlaceholder")
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
