package com.qreal.wmp.dashboard.common.config.core;

import com.qreal.wmp.dashboard.common.utils.PropertyLoader;
import com.qreal.wmp.dashboard.controller.RobotRestServlet;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/** Represents initialization process of web application based on Spring Framework.*/
public class AppInit implements WebApplicationInitializer {

    /**
     * Will be called before actual initialization of servlet. Here we can create hierarchy of spring contexts and link
     * it to servlet context.
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();

        dispatcherContext.scan("com.qreal.wmp.dashboard");
        dispatcherContext.register(AppInit.class);
        servletContext.addListener(new ContextLoaderListener(dispatcherContext));

        DispatcherServlet dispatcherServlet = new DispatcherServlet(dispatcherContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        //Thrift services servlet
        ServletRegistration.Dynamic robotRestService = servletContext.addServlet("RobotRestServlet",
                new RobotRestServlet(dispatcherContext));
        robotRestService.setLoadOnStartup(1);
        robotRestService.addMapping(PropertyLoader.load("service.properties", "path.dashboard.service"));

        DelegatingFilterProxy filter = new DelegatingFilterProxy("springSecurityFilterChain");
        filter.setContextAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.dispatcher");

        servletContext.addFilter("springSecurityFilterChain", filter).addMappingForUrlPatterns(null, false, "/*");
    }


}