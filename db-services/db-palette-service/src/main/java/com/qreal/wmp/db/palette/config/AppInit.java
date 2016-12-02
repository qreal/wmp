package com.qreal.wmp.db.palette.config;

import com.qreal.wmp.db.palette.exceptions.AbortedException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/** Main class of program. */
@ComponentScan("com.qreal.wmp.db.palette")
public class AppInit {

    /** Main function creates context and starts server.*/
    public static void main(String[] args) throws AbortedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.qreal.wmp.db.palette");
        context.register(AppInit.class);
        context.refresh();
    }
}