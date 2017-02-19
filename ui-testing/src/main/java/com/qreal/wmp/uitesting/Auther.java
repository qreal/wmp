package com.qreal.wmp.uitesting;

import com.qreal.wmp.uitesting.exceptions.WrongAuthException;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

/** Used for authentication in current browser session. */
public class Auther {

    /** Use properties from pages.properies file. */
    private Environment env;

    private static final Logger logger = LoggerFactory.getLogger(Auther.class);
    
    public Auther(Environment env) {
        this.env = env;
    }
    /** Realizes authentication to the wmp.
     *
     * @param username login
     * @param password password
     * */
    public void auth(final String username, final String password) throws WrongAuthException {
        open(env.getProperty("auth"));
        $(By.name("username")).setValue(username);
        $(By.name("password")).setValue(password);
        $("[type=\"submit\"]").click();
        if ($(byText("Password or login wrong")).exists()) {
            throw new WrongAuthException(username, password);
        }
        logger.info("Authentication with login: {} and password: {}", username, password);
    }

    public void auth() throws WrongAuthException {
        auth("123", "123");
    }
}
