package com.qreal.wmp.uitesting.dia.pallete;

import com.codeborne.selenide.SelenideElement;

public class PalleteElement {

    private final SelenideElement inner;
    
    private final String name;
    
    public PalleteElement(SelenideElement inner) {
        this.inner = inner;
        name = inner.attr("data-type");
    }
    
    public SelenideElement getInner() {
        return inner;
    }
    
    public String getName() {
        return name;
    }
}
