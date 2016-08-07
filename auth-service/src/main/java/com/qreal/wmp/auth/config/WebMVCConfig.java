package com.qreal.wmp.auth.config;

import com.qreal.wmp.auth.config.oauth2.OAuth2ServerConfig;
import com.qreal.wmp.auth.config.security.MethodSecurityConfig;
import com.qreal.wmp.auth.config.security.SecurityConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/** Represents files resolve policy of Spring Framework.*/
@Configuration
@EnableWebMvc
@Import({SecurityConfiguration.class, OAuth2ServerConfig.class, MethodSecurityConfig.class})
public class WebMVCConfig extends WebMvcConfigurerAdapter {
    /** Resolves view name in jsp file path.*/
    @Bean(name = "viewResolver")
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    /** Creates resources handlers for static resources.*/
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    /**
     * Configure static resources and it seems that it will route to default tomcat servlet in case dispatcher
     * couldn't handle request.
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}