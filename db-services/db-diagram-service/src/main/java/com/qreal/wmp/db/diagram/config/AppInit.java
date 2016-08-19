package com.qreal.wmp.db.diagram.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** Main class of program. */
public class AppInit {
    /** Main function creates context and starts server.*/
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.qreal.wmp.db.diagram.config");
        context.scan("com.qreal.wmp.db.diagram.dao");
        context.scan("com.qreal.wmp.db.diagram.server");
        context.register(AppInit.class);
        context.refresh();
    }

}