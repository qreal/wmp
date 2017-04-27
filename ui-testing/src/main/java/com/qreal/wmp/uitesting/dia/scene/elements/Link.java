package com.qreal.wmp.uitesting.dia.scene.elements;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import com.qreal.wmp.uitesting.pages.editor.EditorPageFacade;
import com.qreal.wmp.uitesting.services.SelectorService;
import com.qreal.wmp.uitesting.services.SelectorService.Attribute;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/** Link describes relations between blocks. */
public class Link extends SceneElementImpl {
    
    private final String name;
    
    private final SceneElement source;
    
    private final SceneElement target;
    
    @SuppressWarnings({"all"})
    private final EditorPageFacade editorPageFacade;
    
    private final SelectorService selectorService;
    
    /** Describes link between two blocks. */
    public Link(String name, By selector, EditorPageFacade editorPageFacade, SelectorService selectorService) {
        super(selector);
        this.selectorService = selectorService;
        this.name = name;
        SelenideElement source = $(selector).find(selectorService.get("arrow.source", Attribute.SELECTOR));
        this.source = new SceneElementImpl(By.id(source.attr("id")));
        SelenideElement target = $(selector).find(selectorService.get("arrow.target", Attribute.SELECTOR));
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
