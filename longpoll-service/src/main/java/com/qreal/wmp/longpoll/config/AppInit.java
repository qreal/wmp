package com.qreal.wmp.longpoll.config;

import com.qreal.wmp.longpoll.server.thrift.LongpollServlet;
import com.qreal.wmp.longpoll.utils.PropertyLoader;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/** Main class of program. */
public class AppInit implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();

        dispatcherContext.scan("com.qreal.wmp.longpoll");
        dispatcherContext.register(AppInit.class);
        servletContext.addListener(new ContextLoaderListener(dispatcherContext));

        DispatcherServlet dispatcherServlet = new DispatcherServlet(dispatcherContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        // Registration RestService
        ServletRegistration.Dynamic longpollService = servletContext.addServlet("LongpollRestServlet",
                new LongpollServlet(dispatcherContext));
        longpollService.setLoadOnStartup(1);
        longpollService.addMapping(PropertyLoader.load("service.properties", "path.longpoll.thrift"));

    }
}