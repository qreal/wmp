package com.qreal.wmp.uitesting.pages.editor;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.pallete.Pallete;
import com.qreal.wmp.uitesting.dia.property.PropertyEditor;
import com.qreal.wmp.uitesting.dia.scene.SceneProxy;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import com.qreal.wmp.uitesting.services.SelectorService;
import com.qreal.wmp.uitesting.services.SelectorService.Attribute;
import org.openqa.selenium.By;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

/** Facade for components of EditorPage. */
@SuppressWarnings("unused")
public class EditorPageFacade {
    
    private final String url;

    private SceneProxy scene;
    
    private Pallete pallete;
    
    private PropertyEditor propertyEditor;
    
    private SelectorService selectorService;
    
    public EditorPageFacade(String url,
                            SceneProxy scene,
                            Pallete pallete,
                            PropertyEditor propertyEditor,
                            SelectorService selectorService) {
        
        this.url = url;
        this.scene = scene;
        this.pallete = pallete;
        this.propertyEditor = propertyEditor;
        this.selectorService = selectorService;
    }
    
    public EditorPageFacade(String url, SelectorService selectorService) {
        this.url = url;
        this.selectorService = selectorService;
    }
    
    public void setScene(SceneProxy scene) {
        this.scene = scene;
    }
    
    public void setPallete(Pallete pallete) {
        this.pallete = pallete;
    }
    
    public void setPropertyEditor(PropertyEditor propertyEditor) {
        this.propertyEditor = propertyEditor;
    }
    
    public void setSelectorService(SelectorService selectorService) {
        this.selectorService = selectorService;
    }
    
    /** Updates all components. */
    public void update() {
        scene.getSceneWindow().update();
        scene.getBlockProvider().recalculateBlocks();
        scene.getLinkProvider().recalculateLinks();
    }
    
    /** Move block source to position (cellX, cellY).*/
    public void move(Block source, int cellX, int cellY) {
        scene.moveToCell(source, cellX, cellY);
    }
    
    /** Called when new link created by any event in order to let scene known about it. */
    public Link addDrawnLink() {
        Optional<SelenideElement> newLink = scene.getLinkProvider().updateLinks();
        if (!newLink.isPresent()) {
            throw new NoSuchElementException("Link was not created");
        }
        scene.getLinkProvider().recalculateLinks();
        return new Link(
                newLink.get().attr("id"),
                By.id(newLink.get().attr("id")),
                this,
                selectorService.create("scene.poolOfElements.link"));
    }
    
    /** Called when new block created by any event in order to let scene known about it. */
    public Block addDrawnBlock(String name) {
        if ($(By.className(selectorService.get("gestures.gesturesMenu", Attribute.CLASS))).is(Condition.visible)) {
            $(By.className(selectorService.get("gestures.gesturesMenu", Attribute.CLASS)))
                    .find(byText(name.toLowerCase()))
                    .click();
        }
        return scene.getBlockProvider().getNewBlock();
    }
    
    /** Reload Editor page. */
    public void reload() {
        Selenide.open(url);
    }
}
