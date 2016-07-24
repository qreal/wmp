package com.qreal.robots.common.config.core;

import com.qreal.robots.components.dashboard.controller.RobotRestServlet;
import com.qreal.robots.components.database.diagrams.service.server.DiagramDbServer;
import com.qreal.robots.components.database.robots.service.server.RobotDbServer;
import com.qreal.robots.components.database.users.service.server.UserDbServer;
import com.qreal.robots.components.editor.controller.EditorServlet;
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

        servletContext.setInitParameter("spring.profiles.default", "development");

        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();

        dispatcherContext.scan("com.qreal.robots.common");
        dispatcherContext.scan("com.qreal.robots.components.authorization");
        dispatcherContext.scan("com.qreal.robots.components.dashboard");
        dispatcherContext.scan("com.qreal.robots.components.editor");
        dispatcherContext.scan("com.qreal.robots.components.database.diagrams.DAO");
        dispatcherContext.scan("com.qreal.robots.components.database.diagrams.service");
        dispatcherContext.scan("com.qreal.robots.components.database.robots.DAO");
        dispatcherContext.scan("com.qreal.robots.components.database.robots.service");
        dispatcherContext.scan("com.qreal.robots.components.database.users.DAO");
        dispatcherContext.scan("com.qreal.robots.components.database.users.service");

        dispatcherContext.register(AppInit.class);

        servletContext.addListener(new ContextLoaderListener(dispatcherContext));

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        // Registration RestService
        ServletRegistration.Dynamic robotRestService = servletContext.addServlet("RobotRestServlet", new RobotRestServlet(dispatcherContext));
        robotRestService.setLoadOnStartup(1);
        robotRestService.addMapping("/RobotRest");

        ServletRegistration.Dynamic diagramService = servletContext.addServlet("DiagramRestServlet", new EditorServlet(dispatcherContext));
        diagramService.setLoadOnStartup(1);
        diagramService.addMapping("/editorService");

        // Registration UserDbService
        UserDbServer userDbServer = new UserDbServer(dispatcherContext);

        // Registration RobotDbService
        RobotDbServer robotDbServer = new RobotDbServer(dispatcherContext);

        // Registration DiagramDbService
        DiagramDbServer diagramDbServer = new DiagramDbServer(dispatcherContext);

        DelegatingFilterProxy filter = new DelegatingFilterProxy("springSecurityFilterChain");
        filter.setContextAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.dispatcher");

        servletContext.addFilter("springSecurityFilterChain", filter).addMappingForUrlPatterns(null, false, "/*");
    }
}