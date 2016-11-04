package com.qreal.wmp.uitesting.dia;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.codeborne.selenide.Selenide.$;

/**
 * Describes Pallete.
 * For any manipulating with it.
 */
@Service
public class Pallete {

    private final String selector = "#palette-tab-content";

    private static final Logger logger = LoggerFactory.getLogger(Pallete.class);

    /**
     * Chose element from Pallete.
     *
     * @param elementName name of block
     * @return block
     */
    public SelenideElement getElement(String elementName) {
        SelenideElement element = $(By.cssSelector(selector + " div[data-type=\"" + elementName + "\"]"));
        logger.info("Get element {} from Palette", element);
        return element;
    }
}
