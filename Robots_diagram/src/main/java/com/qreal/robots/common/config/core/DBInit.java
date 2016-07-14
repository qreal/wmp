package com.qreal.robots.common.config.core;

import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.database.diagrams.service.DiagramService;
import com.qreal.robots.components.database.users.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;

//User with login "123" and password "123" will be created automatically


@Component
public class DBInit implements ApplicationListener {

    @EventListener
    public void onApplicationEvent(ApplicationEvent event) {

        if (event instanceof ContextRefreshedEvent) {
            ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();

            UserService userService = (UserService) applicationContext.getBean("UserService");
            DiagramService diagramService = (DiagramService) applicationContext.getBean("DiagramService");

            PasswordEncoder encoder = (PasswordEncoder) applicationContext.getBean("PasswordEncoder");

            if (userService.isUserExist("123")) {
                return;
            }

            User user = new User("123", encoder.encode("123"), true);
            userService.save(user);

            diagramService.createRootFolder("123");

        }
    }

}