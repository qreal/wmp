package com.qreal.wmp.uitesting.services.impl;

import com.qreal.wmp.uitesting.exceptions.WrongAuthException;
import com.qreal.wmp.uitesting.services.Auther;
import com.qreal.wmp.uitesting.services.Opener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.access.AccessDeniedException;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

/** {@inheritDoc} */
public class OpenerImpl implements Opener {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenerImpl.class);
    
    /** Uses properties from pages.properies file. */
    private final Environment env;

    private final Auther auther;
    
    public OpenerImpl(Environment env, Auther auther) {
        this.env = env;
        this.auther = auther;
    }
    
    /** {@inheritDoc} */
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
    }
    
    public void cleanOpen(final String page) {
        com.codeborne.selenide.Selenide.open(env.getProperty(page));
        logger.info("Open page {}", env.getProperty(page));
    }
}
