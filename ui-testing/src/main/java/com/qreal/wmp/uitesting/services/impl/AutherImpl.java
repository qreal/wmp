package com.qreal.wmp.uitesting.services.impl;

import com.qreal.wmp.uitesting.exceptions.WrongAuthException;
import com.qreal.wmp.uitesting.services.Auther;
import com.qreal.wmp.uitesting.services.SelectorService;
import com.qreal.wmp.uitesting.services.SelectorService.Attribute;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

/** {@inheritDoc} */
public class AutherImpl implements Auther {
    
    private static final Logger logger = LoggerFactory.getLogger(AutherImpl.class);
    
    /** Use properties from pages.properies file. */
    private final Environment env;
    
    private final SelectorService selectorService;
    
    public AutherImpl(Environment env, SelectorService selectorService) {
        this.env = env;
        this.selectorService = selectorService;
    }
    
    /** {@inheritDoc} */
    public void auth(final String username, final String password) throws WrongAuthException {
        open(env.getProperty("auth"));
        $(By.id(selectorService.get("usernameInput", Attribute.ID))).setValue(username);
        $(By.id(selectorService.get("passwordInput", Attribute.ID))).setValue(password);
        $(By.id(selectorService.get("submitButton", Attribute.ID))).click();
        if ($(By.id(selectorService.get("wrongAuthLabel", Attribute.ID))).isDisplayed()) {
            throw new WrongAuthException(username, password);
        }
        logger.info("Authentication with login: {} and password: {}", username, password);
    }

    public void auth() throws WrongAuthException {
        auth("Admin", "Admin");
    }
}
