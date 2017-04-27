package com.qreal.wmp.uitesting.services.impl;

import com.qreal.wmp.uitesting.exceptions.WrongAuthException;
import com.qreal.wmp.uitesting.services.Auther;
import com.qreal.wmp.uitesting.services.SelectorService;
import com.qreal.wmp.uitesting.services.SelectorService.Attribute;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import static com.codeborne.selenide.Selectors.byText;
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
        $(By.name(selectorService.get("usernameInput", Attribute.NAME))).setValue(username);
        $(By.name(selectorService.get("passwordInput", Attribute.NAME))).setValue(password);
        $("[type=" + selectorService.get("submitButton", Attribute.TYPE) + "]").click();
        if ($(byText(selectorService.get("wrongAuthLabel", Attribute.TEXT))).exists()) {
            throw new WrongAuthException(username, password);
        }
        logger.info("Authentication with login: {} and password: {}", username, password);
    }

    public void auth() throws WrongAuthException {
        auth("Admin", "Admin");
    }
}
