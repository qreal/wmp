package com.qreal.wmp.db.diagram.config;

import com.qreal.wmp.db.diagram.exceptions.AbortedException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/** Main class of program. */
@ComponentScan("com.qreal.wmp.db.diagram")
public class AppInit {
    /** Main function creates context and starts server.*/
    public static void main(String[] args) throws AbortedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.qreal.wmp.db.diagram");
        context.register(AppInit.class);
        context.refresh();
    }
}