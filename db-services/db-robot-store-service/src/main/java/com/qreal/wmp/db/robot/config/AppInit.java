package com.qreal.wmp.db.robot.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** Main class of program. */
public class AppInit {

    /** Main function creates context and starts server.*/
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.qreal.wmp.db.robot.config");
        context.scan("com.qreal.wmp.db.robot.dao");
        context.scan("com.qreal.wmp.db.robot.client");
        context.scan("com.qreal.wmp.db.robot.server");
        context.register(AppInit.class);
        context.refresh();
    }

}