package com.qreal.wmp.auth.config;

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
public class DbInit implements ApplicationListener {

    private static final Logger logger = LoggerFactory.getLogger(DbInit.class);

    private PasswordEncoder passwordEncoder;

    /**Listener of application events. Will react only if context was initialized.*/
    @EventListener
    public void onApplicationEvent(ApplicationEvent event) {

        if (event instanceof ContextRefreshedEvent) {
            ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();

            UserDAO userService = (UserDAO) applicationContext.getBean("userService");

            passwordEncoder = (PasswordEncoder) applicationContext.getBean("passwordEncoder");

            ClientDAO clientService = (ClientDAO) applicationContext.getBean("clientService");

            userService.add(createAdmin());

            userService.add(createTestUser("123", "123"));

            userService.add(createTestUser("1234", "1234"));

            clientService.add(createClient("dashboardService", "secret"));

            clientService.add(createClient("editorService", "secret"));

            logger.info("DB initialized");
        }
    }

    private User createAdmin() {
        logger.info("Creating admin {} with password {}", "Admin", "Admin");
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new UserAuthority("ROLE_ADMIN"));
        authorities.add(new UserAuthority("ROLE_USER"));
        User user = new User("Admin", passwordEncoder.encode("Admin"), authorities);
        logger.info("Created admin {} with password {}", "Admin", "Admin");
        return user;
    }

    private User createTestUser(String name, String password) {
        logger.info("Creating user {} with password {}", name, password);
        Set<GrantedAuthority> userAuthorities = new HashSet<>();
        userAuthorities.add(new UserAuthority("ROLE_USER"));
        User user = new User(name, passwordEncoder.encode(password), userAuthorities);
        logger.info("Created user {} with password {}", name, password);
        return user;
    }

    private Client createClient(String clientId, String secret) {
        logger.info("Creating client {} with secret {}", clientId, secret);
        Set<String> scopes = new HashSet<>();
        scopes.add("read");
        scopes.add("write");
        Set<String> grantTypes = new HashSet<>();
        grantTypes.add("authorization_code");
        Client client = new Client(clientId, true, secret, true, scopes, grantTypes, 64000, 64000, true);
        logger.info("Created client {} with secret {}", clientId, secret);
        return client;
    }
}
