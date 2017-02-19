package com.qreal.wmp.uitesting;

import com.qreal.wmp.uitesting.dia.services.Scene;
import com.qreal.wmp.uitesting.exceptions.WrongAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.access.AccessDeniedException;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

/**
 * Used for open needed page from wmp in current browser session.
 * Allows you to access as an authorized user and not.
 */
public class Opener {

    /** Uses properties from pages.properies file. */
    private Environment env;

    private Auther auther;
    private Scene scene;
    
    private static final Logger logger = LoggerFactory.getLogger(Opener.class);
    
    public Opener(Environment env, Auther auther, Scene scene) {
        this.env = env;
        this.auther = auther;
        this.scene = scene;
    }
    
    /**
     * Opens page from wmp with authentication.
     *
     * @param page must be one of the keys from pages.property.
     */
    public void open(final String page) {
        try {
            com.codeborne.selenide.Selenide.open(env.getProperty(page));
            logger.info("Open page {}", env.getProperty(page));
            if ($(byText("Sign in to continue to Auth")).exists()) {
                logger.info("Fail with open page {}. Try to login.", env.getProperty(page));
                auther.auth();
            }
            com.codeborne.selenide.Selenide.open(env.getProperty(page));
            logger.info("Open page {}", env.getProperty(page));
        } catch (WrongAuthException e) {
            logger.error("Opener fails: " +  e.getMessage());
            throw new AccessDeniedException(e.getMessage());
        }
        logger.info("Open page {}", env.getProperty(page));
        scene.init();
    }

    /**
     * Opens page from wmp without authentication.
     *
     * @param page must be one of the keys from pages.property.
     */
    public void cleanOpen(final String page) {
        com.codeborne.selenide.Selenide.open(env.getProperty(page));
        logger.info("Open page {}", env.getProperty(page));
        scene.init();
    }
}
