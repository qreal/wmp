package com.qreal.wmp.uitesting.dia.scene.elements;

import com.qreal.wmp.uitesting.pages.editor.EditorPageFacade;
import com.qreal.wmp.uitesting.services.SelectorService;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Describes item, which is placed on the scene.
 * Palette have items. If we dragAndDrop these items to the Scene, we'll get Blocks.
 */
public class Block extends SceneElementImpl {
    
    private final String name;
    
    private final SceneElement port;
    
    private final EditorPageFacade editorPageFacade;
    
    /** Part of diagram. Appears when element from palette moves to scene. */
    public Block(String name, By selector, EditorPageFacade editorPageFacade, SelectorService selectorService) {
        super(selector);
        this.name = name;
        this.port = new SceneElementImpl(By.id($(selector)
                .find(By.cssSelector(selectorService.getSelector("port"))).attr("id")));
        this.editorPageFacade = editorPageFacade;
    }
    
    public String getName() {
        return name;
    }
    
    public SceneElement getPort() {
        return port;
    }
    
    public void moveToCell(int cellX, int cellY) {
        editorPageFacade.move(this, cellX, cellY);
    }
}
