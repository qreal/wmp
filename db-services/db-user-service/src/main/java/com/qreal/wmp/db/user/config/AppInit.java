package com.qreal.wmp.db.user.config;

import com.qreal.wmp.db.user.exceptions.Aborted;
import com.qreal.wmp.db.user.exceptions.ErrorConnection;
import com.qreal.wmp.db.user.exceptions.NotFound;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** Main class of program. */
public class AppInit {

    /** Main function creates context and starts server.*/
    public static void main(String[] args) throws Aborted, ErrorConnection, NotFound {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.qreal.wmp.db.user.config");
        context.scan("com.qreal.wmp.db.user.dao");
        context.scan("com.qreal.wmp.db.user.client");
        context.scan("com.qreal.wmp.db.user.server");

        context.register(AppInit.class);
        context.refresh();

    }

}