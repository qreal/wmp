package com.qreal.robots.config;

import com.qreal.robots.server.UserDbServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppInit {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.qreal.robots.config");
        context.scan("com.qreal.robots.dao");
        context.scan("com.qreal.robots.client");

        context.register(AppInit.class);
        context.refresh();

        UserDbServer userDbServer = new UserDbServer(context);
    }

}