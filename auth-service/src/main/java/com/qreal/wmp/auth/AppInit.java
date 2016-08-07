package com.qreal.wmp.auth;

import com.qreal.wmp.auth.database.users.User;
import com.qreal.wmp.auth.database.users.UserAuthority;
import com.qreal.wmp.auth.database.users.UserDAO;
import com.qreal.wmp.auth.database.client.Client;
import com.qreal.wmp.auth.database.client.ClientDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is listener for all application events.
 * It creates admin account and password for it. Also it creates default user 123.
 */
@Component
@ComponentScan("com.qreal.wmp.auth")
public class AppInit implements ApplicationListener {

    private static final Logger logger = LoggerFactory.getLogger(AppInit.class);

    /**Listener of application events. Will react only if context was initialized.*/
    @EventListener
    public void onApplicationEvent(ApplicationEvent event) {

        if (event instanceof ContextRefreshedEvent) {
            ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();

            UserDAO userService = (UserDAO) applicationContext.getBean("userService");
            PasswordEncoder encoder = (PasswordEncoder) applicationContext.getBean("passwordEncoder");

            if (userService.loadUserByUsername("Admin") != null) {
                return;
            }

            //String password = RandomStringGenerator.generateString(12);
            String password = "Admin";
            Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
            authorities.add(new UserAuthority("ROLE_ADMIN"));
            authorities.add(new UserAuthority("ROLE_USER"));
            User admin = new User("Admin", encoder.encode(password), authorities);

            userService.add(admin);

            logger.info("CREATED USER {} WITH PASSWORD {}", "Admin", password);

            if (userService.loadUserByUsername("123") != null) {
                return;
            }

            Set<GrantedAuthority> userAuthorities = new HashSet<GrantedAuthority>();
            userAuthorities.add(new UserAuthority("ROLE_USER"));
            User user = new User("123", encoder.encode("123"), userAuthorities);

            userService.add(user);

            logger.info("CREATED USER {} WITH PASSWORD {}", "123", "123");

            ClientDAO clientService = (ClientDAO) applicationContext.getBean("clientService");

            Set<String> scopes = new HashSet<>();
            scopes.add("read");
            scopes.add("write");

            Set<String> grantTypes = new HashSet<String>();
            grantTypes.add("authorization_code");

            Client dashboardService = new Client("dashboardService", true, "secret", true, scopes, grantTypes,
                    64000, 64000, true);

            clientService.add(dashboardService);

            Client editorService = new Client("editorService", true, "secret", true, scopes, grantTypes,
                    64000, 64000, true);

            clientService.add(editorService);
            logger.info("Initalized");
        }
    }
}
