package com.qreal.wmp.uitesting.dia.scene.elements;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import com.qreal.wmp.uitesting.pages.EditorPageFacade;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/** Link describes relations between blocks. */
public class Link extends SceneElementImpl {
    
    public static final String CLASS_NAME = "link";
    
    private static final String ARROWHEAD = "marker-arrowheads";
    
    private final String name;
    
    private final SceneElement source;
    
    private final SceneElement target;
    
    private final EditorPageFacade editorPageFacade;
    
    /** Describes link between two blocks. */
    public Link(String name, By selector, EditorPageFacade editorPageFacade) {
        super(selector);
        this.name = name;
        SelenideElement source = $(selector).find(By.className(ARROWHEAD)).find(By.cssSelector(":nth-child(1)"));
        this.source = new SceneElementImpl(By.id(source.attr("id")));
        SelenideElement target = $(selector).find(By.className(ARROWHEAD)).find(By.cssSelector(":nth-child(2)"));
        this.target = new SceneElementImpl(By.id(target.attr("id")));
        this.editorPageFacade = editorPageFacade;
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
