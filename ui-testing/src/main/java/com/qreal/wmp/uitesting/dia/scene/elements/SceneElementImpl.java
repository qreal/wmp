package com.qreal.wmp.uitesting.dia.scene.elements;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class SceneElementImpl implements SceneElement {
    
    private By by;
    
    public SceneElementImpl(By by) {
        this.by = by;
    }
    
    /** Based on the Selenium element. */
    public SelenideElement getInnerSeleniumElement() {
        return $(by);
    }
    
    public By getBy() {
        return by;
    }
    
    public Coordinate getCoordinateOnScene() throws ElementNotOnTheSceneException {
        return Coordinate.getCoordinateFromSeleniumObject(getInnerSeleniumElement())
                .orElseThrow(ElementNotOnTheSceneException::new);
    }
}
