package com.qreal.wmp.uitesting.dia.palette;

import com.codeborne.selenide.SelenideElement;


/** Describes palette's items. */
public class PaletteElement {

    private final SelenideElement innerSeleniumElement;
    
    private final String name;
    
    public PaletteElement(SelenideElement innerSeleniumElement) {
        this.innerSeleniumElement = innerSeleniumElement;
        name = innerSeleniumElement.attr("data-type");
    }
    
    public SelenideElement getInnerSeleniumElement() {
        return innerSeleniumElement;
    }
    
    public String getName() {
        return name;
    }
}
