package com.qreal.wmp.uitesting.dia.palette;

import com.codeborne.selenide.SelenideElement;


/** Describes palette's items. */
public class PaletteElement {

    private final SelenideElement innerSeleniumELement;
    
    private final String name;
    
    public PaletteElement(SelenideElement innerSeleniumELement) {
        this.innerSeleniumELement = innerSeleniumELement;
        name = innerSeleniumELement.attr("data-type");
    }
    
    public SelenideElement getInnerSeleniumELement() {
        return innerSeleniumELement;
    }
    
    public String getName() {
        return name;
    }
}
