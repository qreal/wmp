package com.qreal.wmp.uitesting.dia.scene.elements;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;

public class SceneElementImpl implements SceneElement {
    
    private final SelenideElement innerSeleniumElement;
    
    private String type;
    
    /** Based on the Selenium element. */
    public SceneElementImpl(SelenideElement innerSeleniumObject) {
        this.innerSeleniumElement = innerSeleniumObject;
        this.type = innerSeleniumObject.attr("class");
        if (type.contains("selected")) {
            type = type.substring(0, type.indexOf(" selected"));
        }
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
