package com.qreal.wmp.uitesting.auth;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

/** Used for authentication in current browser session. */
@Service
public class Auther {

    /** Use properties from pages.properies file. */
    @Autowired
    private Environment env;

    private static final Logger logger = LoggerFactory.getLogger(Auther.class);

    /** Realizes authentication to the wmp.
     *
     * @param username login
     * @param password password
     * */
    public void auth(final String username, final String password) {
        open(env.getProperty("auth"));
        $(By.name("username")).setValue(username);
        $(By.name("password")).setValue(password);
        $("[type=\"submit\"]").click();
        logger.info("Authentication with login: {} and password: {}", username, password);
    }

    public void auth() {
        auth("123", "123");
    }
}
