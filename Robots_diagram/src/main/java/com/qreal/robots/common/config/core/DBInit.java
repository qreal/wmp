package com.qreal.robots.common.config.core;

import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.database.diagrams.service.client.DiagramService;
import com.qreal.robots.components.database.users.service.client.UserService;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//User with login "123" and password "123" will be created automatically


@Component
public class DBInit implements ApplicationListener {

    private static final Logger logger = LoggerFactory.getLogger(DBInit.class);


    @EventListener
    public void onApplicationEvent(ApplicationEvent event) {

        if (event instanceof ContextRefreshedEvent) {
            ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();

            DiagramService diagramService = (DiagramService) applicationContext.getBean("DiagramService");
            PasswordEncoder encoder = (PasswordEncoder) applicationContext.getBean("passwordEncoder");
            UserService userService = (UserService) applicationContext.getBean("UserService");

            if (userService.isUserExist("123")) {
                return;
            }

            logger.info("Creating 123 test user");

            diagramService.createRootFolder("123");
            userService.save(new User("123", encoder.encode("123"), true));

            logger.info("123 test user created");

        }
    }

}