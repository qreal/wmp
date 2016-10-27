package com.qreal.wmp.editor.common.config.core;

import com.qreal.wmp.editor.common.utils.PropertyLoader;
import com.qreal.wmp.editor.controller.EditorServlet;
import com.qreal.wmp.editor.database.diagrams.model.Folder;
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

        dispatcherContext.scan("com.qreal.wmp.editor");
        dispatcherContext.register(AppInit.class);
        servletContext.addListener(new ContextLoaderListener(dispatcherContext));

        DispatcherServlet dispatcherServlet = new DispatcherServlet(dispatcherContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        // Registration RestService
        ServletRegistration.Dynamic diagramService = servletContext.addServlet("DiagramRestServlet",
                new EditorServlet(dispatcherContext));
        diagramService.setLoadOnStartup(1);
        diagramService.addMapping(PropertyLoader.load("service.properties", "path.editor.service"));

        //Folder
        Folder.setContext(dispatcherContext);

        DelegatingFilterProxy filter = new DelegatingFilterProxy("springSecurityFilterChain");
        filter.setContextAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.dispatcher");

        servletContext.addFilter("springSecurityFilterChain", filter).addMappingForUrlPatterns(null, false, "/*");
    }
}
