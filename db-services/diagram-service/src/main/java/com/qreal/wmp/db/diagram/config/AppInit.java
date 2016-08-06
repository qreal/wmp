package com.qreal.wmp.db.diagram.config;

import com.qreal.wmp.db.diagram.server.DiagramDbServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppInit {
    /**
     * Main function creates context and starts server.
     */
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.qreal.wmp.db.diagram.config");
        context.scan("com.qreal.wmp.db.diagram.dao");
        context.register(AppInit.class);
        context.refresh();
        DiagramDbServer diagramDbServer = new DiagramDbServer(context);
    }

}