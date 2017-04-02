package com.qreal.wmp.uitesting.dia.scene.elements;

import com.qreal.wmp.uitesting.pages.editor.EditorPageFacade;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/** Describes item, which is placed on the scene.
 * Pallete have items. If we dragAndDrop these items to the Scene, we'll get Blocks. */
public class Block extends SceneElementImpl {
    
    public static final String CLASS_NAME = "element devs ImageWithPorts";
    
    private static final String PORT_CLASS_NAME = "port0";
    
    private final String name;
    
    private final SceneElement port;
    
    private final EditorPageFacade editorPageFacade;
    
    public Block(String name, By selector, EditorPageFacade editorPageFacade) {
        super(selector);
        this.name = name;
        this.port = new SceneElementImpl(By.id($(selector).find(By.className(PORT_CLASS_NAME)).attr("id")));
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
