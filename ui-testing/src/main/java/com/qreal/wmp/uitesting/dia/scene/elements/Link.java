package com.qreal.wmp.uitesting.dia.scene.elements;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class Link extends SceneElementImpl {
    
    public static final String CLASS_NAME = "link";
    
    private static final String ARROWHEAD = "marker-arrowheads";
    
    private final String name;
    
    private final SceneElement source;
    
    private final SceneElement target;
    
    /** Describes link between two blocks. */
    public Link(String name, SelenideElement innerSeleniumObject) {
        super(innerSeleniumObject);
        this.name = name;
        this.source = new SceneElementImpl($(innerSeleniumObject
                .find(By.className(ARROWHEAD))).find(By.cssSelector(":nth-child(1)"))
        );
        this.target = new SceneElementImpl($(innerSeleniumObject
                .find(By.className(ARROWHEAD))).find(By.cssSelector(":nth-child(2)"))
        );
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
