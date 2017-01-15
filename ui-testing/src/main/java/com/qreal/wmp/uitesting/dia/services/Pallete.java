package com.qreal.wmp.uitesting.dia.services;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

/**
 * Describes Pallete.
 * For any manipulating with it.
 */
@Service
public class Pallete {

    private static final String selector = "#palette-tab-content";

    private static final Logger logger = LoggerFactory.getLogger(Pallete.class);

    /**
     * Chose element from Pallete.
     *
     * @param elementName name of block
     * @return block
     */
    public SelenideElement getElement(final String elementName) throws NoSuchElementException {
        final SelenideElement element =  $(By.cssSelector(selector)).find(withText(elementName));
        logger.info("Get element {} from Palette", element);
        return element;
    }

}
