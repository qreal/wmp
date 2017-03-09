package com.qreal.wmp.uitesting.dia.scene.elements;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class Block extends SceneElementImpl {
    
    public static final String CLASS_NAME = "element devs ImageWithPorts";
    
    private static final String PORT_CLASS_NAME = "port0";
    
    private final String name;
    
    private final SceneElement port;
    
    public Block(String name, By by) {
        super(by);
        this.name = name;
        this.port = new SceneElementImpl(By.id($(by).find(By.className(PORT_CLASS_NAME)).attr("id")));
    }
    
    public String getName() {
        return name;
    }
    
    public SceneElement getPort() {
        return port;
    }
}
