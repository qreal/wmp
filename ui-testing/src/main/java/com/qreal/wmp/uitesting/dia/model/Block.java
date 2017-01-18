package com.qreal.wmp.uitesting.dia.model;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class Block extends SceneElement {
    
    public static final String className = "element devs ImageWithPorts";
    
    private final static String portClassName = "port0";
    
    private final String name;
    
    private final SceneElement port;
    
    public Block(String name, SelenideElement innerSeleniumObject) {
        super(innerSeleniumObject);
        this.name = name;
        this.port = new SceneElement($(innerSeleniumObject.find(By.className(portClassName))));
    }
    
    public String getName() {
        return name;
    }
    
    public SceneElement getPort() {
        return port;
    }
}
