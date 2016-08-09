package com.qreal.wmp.editor.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/** Represents files resolve policy of Spring Framework.*/
@Configuration
@EnableWebMvc
@Import({SecurityConfig.class})
@ComponentScan("com.qreal.wmp.editor")
public class WebConfig extends WebMvcConfigurerAdapter {

    /** Time, in seconds, to have the browser cache static resources (one week).*/
    private static final int BROWSER_CACHE_CONTROL = 604800;

    /** Resolves view name in jsp file path.*/
    @Bean
    public InternalResourceViewResolver setupViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        return resolver;
    }

    /** Creates resources handlers for static resources.*/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/WEB-INF/pages/**").addResourceLocations("/pages/");
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/app/**").addResourceLocations("/app/");
        registry.addResourceHandler("/configs/**").addResourceLocations("/configs/");
        registry.addResourceHandler("/images/**").addResourceLocations("/images/").
                setCachePeriod(BROWSER_CACHE_CONTROL);

    }
}