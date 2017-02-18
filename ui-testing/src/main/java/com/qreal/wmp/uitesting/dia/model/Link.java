package com.qreal.wmp.uitesting.dia.model;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class Link extends SceneElement {
    
    public static final String className = "link";
    
    private static final String sourcePointClassName = "marker-source";
    
    private static final String targetPointClassName = "marker-target";
    
    private final String name;
    
    private final SceneElement source;
    
    private final SceneElement target;
    
    public Link(String name, SelenideElement innerSeleniumObject) {
        super(innerSeleniumObject);
        this.name = name;
        this.source = new SceneElement($(innerSeleniumObject.find(By.className(sourcePointClassName))));
        this.target = new SceneElement($(innerSeleniumObject.find(By.className(targetPointClassName))));
    }
    
    public String getName() {
        return name;
    }
    
    public SceneElement getSource() {
        return source;
    }
    
    public SceneElement getTarget() {
        return target;
    }
    
    @Override
    public Coordinate getCoordinateOnScene() throws ElementNotOnTheSceneException {
        return new Coordinate(
                (source.getCoordinateOnScene().getXAbsolute() + target.getCoordinateOnScene().getXAbsolute()) / 2,
                (source.getCoordinateOnScene().getYAbsolute() + target.getCoordinateOnScene().getYAbsolute()) / 2
        );
    }
}
