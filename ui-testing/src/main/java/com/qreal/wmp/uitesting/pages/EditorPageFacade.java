package com.qreal.wmp.uitesting.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.pallete.Pallete;
import com.qreal.wmp.uitesting.dia.property.PropertyEditor;
import com.qreal.wmp.uitesting.dia.scene.SceneProxy;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import org.openqa.selenium.By;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class EditorPageFacade {

    private SceneProxy scene;
    
    private Pallete pallete;
    
    private PropertyEditor propertyEditor;
    
    public EditorPageFacade(SceneProxy scene, Pallete pallete, PropertyEditor propertyEditor) {
        this.scene = scene;
        this.pallete = pallete;
        this.propertyEditor = propertyEditor;
    }
    
    public EditorPageFacade() {}
    
    public void setScene(SceneProxy scene) {
        this.scene = scene;
    }
    
    public void setPallete(Pallete pallete) {
        this.pallete = pallete;
    }
    
    public void setPropertyEditor(PropertyEditor propertyEditor) {
        this.propertyEditor = propertyEditor;
    }
    
    public void update() {
        scene.getBlockProvider().recalculateBlocks();
        scene.getLinkProvider().recalculateLinks();
    }
    
    public void move(Block source, int cellX, int cellY) {
        scene.moveToCell(source, cellX, cellY);
    }
    
    public Link addDrawnLink() {
        Optional<SelenideElement> newLink = scene.getLinkProvider().updateLinks();
        if (!newLink.isPresent()) {
            throw new NoSuchElementException("Link was not created");
        }
        scene.getLinkProvider().recalculateLinks();
        return new Link(newLink.get().attr("id"), By.id(newLink.get().attr("id")), this);
    }
    
    public Block addDrawnBlock(String name) {
        if ($(By.className("gestures-menu")).is(Condition.visible)) {
            $(By.className("gestures-menu")).find(byText(name.toLowerCase())).click();
        }
        return scene.getBlockProvider().getNewBlock();
    }
}
