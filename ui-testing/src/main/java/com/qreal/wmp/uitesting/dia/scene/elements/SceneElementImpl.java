package com.qreal.wmp.uitesting.dia.scene.elements;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/** All Scene elements have selector by which we can clearly define their web instances.
 * Also all scene elements have coordinates on the Scene. */
public class SceneElementImpl implements SceneElement {
    
    // Wrapper over an string selector. Used to search the element in HTML representation of current page.
    private final By selector;
    
    public SceneElementImpl(By selector) {
        this.selector = selector;
    }
    
    /** Based on the Selenium element. */
    public SelenideElement getInnerSeleniumElement() {
        return $(selector);
    }
    
    public By getBy() {
        return selector;
    }
    
    public Coordinate getCoordinateOnScene() throws ElementNotOnTheSceneException {
        return Coordinate.getCoordinateFromSeleniumObject(getInnerSeleniumElement())
                .orElseThrow(ElementNotOnTheSceneException::new);
    }
}
