package com.qreal.wmp.db.user.config;

import com.qreal.wmp.db.user.exceptions.AbortedException;
import com.qreal.wmp.db.user.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.user.exceptions.NotFoundException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/** Main class of program. */
@ComponentScan("com.qreal.wmp.db.user")
public class AppInit {

    /** Main function creates context and starts server.*/
    public static void main(String[] args) throws AbortedException, ErrorConnectionException, NotFoundException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.qreal.wmp.db.user.config");
        context.scan("com.qreal.wmp.db.user.dao");
        context.scan("com.qreal.wmp.db.user.client");
        context.scan("com.qreal.wmp.db.user.server");
        context.scan("com.qreal.wmp.db.user.test");


        context.register(AppInit.class);
        context.refresh();

    }

}