package com.qreal.wmp.uitesting.dia.scene.elements;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.utils.Coordinate;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class Link extends SceneElementImpl {
    
    public static final String CLASS_NAME = "link";
    
    private static final String SOURCE_POINT_CLASSNAME = "marker-source";
    
    private static final String TARGET_POINT_CLASSNAME = "marker-target";
    
    private final String name;
    
    private final SceneElement source;
    
    private final SceneElement target;
    
    public Link(String name, SelenideElement innerSeleniumObject) {
        super(innerSeleniumObject);
        this.name = name;
        this.source = new SceneElementImpl($(innerSeleniumObject.find(By.className(SOURCE_POINT_CLASSNAME))));
        this.target = new SceneElementImpl($(innerSeleniumObject.find(By.className(TARGET_POINT_CLASSNAME))));
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
