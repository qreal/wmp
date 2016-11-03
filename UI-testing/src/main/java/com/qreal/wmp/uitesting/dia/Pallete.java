package com.qreal.wmp.uitesting.dia;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.springframework.stereotype.Service;

import static com.codeborne.selenide.Selenide.$;

@Service
public class Pallete {

    private final String selector = "#palette-tab-content";

    public SelenideElement getElement(String elementName) {
        return $(By.cssSelector(selector + " div[data-type=\"" + elementName + "\"]"));
    }
}
