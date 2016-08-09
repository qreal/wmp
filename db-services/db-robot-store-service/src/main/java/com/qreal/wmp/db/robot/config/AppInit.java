package com.qreal.wmp.db.robot.config;

import com.qreal.wmp.db.robot.server.RobotDbServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** Main class of program. */
public class AppInit {

    /**
     * Main function creates context and starts server.
     */
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.qreal.wmp.db.robot.config");
        context.scan("com.qreal.wmp.db.robot.dao");
        context.scan("com.qreal.wmp.db.robot.client");
        context.register(AppInit.class);
        context.refresh();

        RobotDbServer robotDbServer = new RobotDbServer(context);
    }

}