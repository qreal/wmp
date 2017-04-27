package com.qreal.wmp.uitesting.dia.scene.elements;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Describes item, which is placed on the scene.
 * Palette have items. If we dragAndDrop these items to the Scene, we'll get Blocks.
 */
public class Block extends SceneElementImpl {
    
    public static final String CLASS_NAME = "element devs ImageWithPorts";
    
    private static final String PORT_CLASS_NAME = "port0";
    
    private final String name;
    
    private final SceneElement port;
    
    private final Scene scene;
    
    public Block(String name, By selector, Scene scene) {
        super(selector);
        this.name = name;
        this.port = new SceneElementImpl(By.id($(selector).find(By.className(PORT_CLASS_NAME)).attr("id")));
        this.scene = scene;
    }
    
    public String getName() {
        return name;
    }
    
    public SceneElement getPort() {
        return port;
    }
    
    public void moveToCell(int cellX, int cellY) {
        scene.moveToCell(this, cellX, cellY);
    }
}
