package com.qreal.wmp.uitesting.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

/**
 * Used for open needed page from wmp in current browser session.
 * Allows you to access as an authorized user and not.
 */
@Service
public class Opener {

    private static final Logger logger = LoggerFactory.getLogger(Opener.class);

    /** Uses properties from pages.properies file. */
    @Autowired
    private Environment env;

    @Autowired
    private Auther auther;

    /**
     * Opens page from wmp with authentication.
     *
     * @param page must be one of the keys from pages.property.
     */
    public void open(String page) {
        com.codeborne.selenide.Selenide.open(env.getProperty(page));
        if ($(byText("Sign in to continue to Auth")).exists()) {
            logger.info("Fail with open page {}. Try to login.", env.getProperty(page));
            auther.auth();
        }
        com.codeborne.selenide.Selenide.open(env.getProperty(page));
        logger.info("Open page {}", env.getProperty(page));
    }

    /**
     * Opens page from wmp without authentication.
     *
     * @param page must be one of the keys from pages.property.
     */
    public void cleanOpen(String page) {
        com.codeborne.selenide.Selenide.open(env.getProperty(page));
        logger.info("Open page {}", env.getProperty(page));
    }
}
