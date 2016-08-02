package com.qreal.robots.common.config.core;

import com.qreal.robots.controller.EditorServlet;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class AppInit implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();

        dispatcherContext.scan("com.qreal.robots.common");

        dispatcherContext.scan("com.qreal.robots.controller");

        dispatcherContext.register(AppInit.class);

        servletContext.addListener(new ContextLoaderListener(dispatcherContext));

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher",
                new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        // Registration RestService
        ServletRegistration.Dynamic diagramService = servletContext.addServlet("DiagramRestServlet",
                new EditorServlet(dispatcherContext));
        diagramService.setLoadOnStartup(1);
        diagramService.addMapping("/editorService");

        DelegatingFilterProxy filter = new DelegatingFilterProxy("springSecurityFilterChain");
        filter.setContextAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.dispatcher");

        servletContext.addFilter("springSecurityFilterChain", filter).addMappingForUrlPatterns(null, false, "/*");
    }
}