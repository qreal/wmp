package com.qreal.wmp.db.user.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/** Main class of program. */
@ComponentScan("com.qreal.wmp.db.user")
public class AppInit {
    /** Main function creates context and starts server.*/
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.qreal.wmp.db.user");
        context.register(AppInit.class);
        context.refresh();
    }
}