package com.qreal.wmp.db.robot.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/** Main class of program. */
@ComponentScan("com.qreal.wmp.db.robot")
public class AppInit {

    /** Main function creates context and starts server.*/
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.qreal.wmp.db.robot.config");
        context.scan("com.qreal.wmp.db.robot.dao");
        context.scan("com.qreal.wmp.db.robot.client");
        context.scan("com.qreal.wmp.db.robot.server");
        context.scan("com.qreal.wmp.db.robot.test");
        context.register(AppInit.class);
        context.refresh();
    }

}