package com.qreal.wmp.uitesting.dia;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.springframework.stereotype.Service;

import static com.codeborne.selenide.Selenide.$;

/**
 * Describes Pallete.
 * For any manipulating with it.
 */
@Service
public class Pallete {

    private final String selector = "#palette-tab-content";

    /**
     * Chose element from Pallete.
     *
     * @param elementName name of block
     * @return block
     */
    public SelenideElement getElement(String elementName) {
        return $(By.cssSelector(selector + " div[data-type=\"" + elementName + "\"]"));
    }
}
