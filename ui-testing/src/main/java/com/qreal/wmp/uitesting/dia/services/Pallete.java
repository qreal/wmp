package com.qreal.wmp.uitesting.dia.services;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.model.PalleteElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

/**
 * Describes Pallete.
 * For any manipulating with it.
 */
public class Pallete {

    private static final String SELECTOR = "#palette-tab-content";

    private static final Logger logger = LoggerFactory.getLogger(Pallete.class);

    /**
     * Chose element from Pallete.
     *
     * @param elementName name of block
     * @return block
     */
    public PalleteElement getElement(final String elementName) throws NoSuchElementException {
        final SelenideElement element =  $(By.cssSelector(SELECTOR)).find(withText(elementName));
        logger.info("Get element {} from Palette", element);
        return new PalleteElement(element);
    }

}
