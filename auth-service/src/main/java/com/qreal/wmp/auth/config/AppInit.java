package com.qreal.wmp.auth.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/** Represents initialization process of web application based on Spring Framework.*/
@ComponentScan("com.qreal.wmp.auth")
public class AppInit implements WebApplicationInitializer {
    /**
     * Will be called before actual initialization of servlet. Here we can create hierarchy of spring contexts and link
     * it to servlet context.
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //Root context contains database objects and appinit
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.scan("com.qreal.wmp.auth");
        dispatcherContext.register(AppInit.class);
        servletContext.addListener(new ContextLoaderListener(dispatcherContext));

        DispatcherServlet dispatcherServlet = new DispatcherServlet(dispatcherContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        DelegatingFilterProxy filter = new DelegatingFilterProxy("springSecurityFilterChain");
        filter.setContextAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.dispatcher");

        servletContext.addFilter("springSecurityFilterChain", filter).addMappingForUrlPatterns(null, false, "/*");
    }
}
