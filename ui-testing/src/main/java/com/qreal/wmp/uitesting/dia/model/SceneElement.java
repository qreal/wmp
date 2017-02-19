package com.qreal.wmp.uitesting.dia.model;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;

public class SceneElement {
    
    private final SelenideElement innerSeleniumElement;
    
    private String type;
    
    /**
     * Describes any element on the Scene.
     * */
    public SceneElement(SelenideElement innerSeleniumObject) {
        this.innerSeleniumElement = innerSeleniumObject;
        this.type = innerSeleniumObject.attr("class");
    }
    
    public SelenideElement getInnerSeleniumElement() {
        return innerSeleniumElement;
    }
    
    public Coordinate getCoordinateOnScene() throws ElementNotOnTheSceneException {
        return Coordinate.getCoordinateFromSeleniumObject(innerSeleniumElement)
                .orElseThrow(ElementNotOnTheSceneException::new);
    }
    
    public String getType() {
        return type;
    }
}
