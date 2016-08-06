package com.qreal.wmp.db.user.config;

import com.qreal.wmp.db.user.server.UserDbServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppInit {

    /**
     * Main function creates context and starts server.
     */
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.qreal.wmp.db.user.config");
        context.scan("com.qreal.wmp.db.user.dao");
        context.scan("com.qreal.wmp.db.user.client");

        context.register(AppInit.class);
        context.refresh();

        UserDbServer userDbServer = new UserDbServer(context);
    }

}