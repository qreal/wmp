package com.qreal.wmp.uitesting.dia.property;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.NoSuchElementException;

/** Describe Property Editor. */
public interface PropertyEditor {
    /** Set property of element which on the focus. */
    void setProperty(SelenideElement element, String propertyName, String propertyValue) throws NoSuchElementException;
    
    /** Return the value of property by name. */
    String getProperty(final SelenideElement element, final String propertyName) throws NoSuchElementException;
}
