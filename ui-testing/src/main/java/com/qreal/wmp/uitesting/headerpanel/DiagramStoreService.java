package com.qreal.wmp.uitesting.headerpanel;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.scene.SceneImpl;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class DiagramStoreService {
    
    private final Map<String, String> diagrams = new HashMap<>();
    
    private final By sceneSelector = By.cssSelector(SceneImpl.SELECTOR);
    
    public void addDiagram(String key) {
        SelenideElement element = $(By.cssSelector(".saving-menu")).find(By.cssSelector(":nth-child(2)"));
        element.setValue(key);
        diagrams.put(key, $(sceneSelector).innerHtml());
        $(By.id("saving")).click();
    }
    
    public void get(String key) {
        diagrams.get(key);
    }
    
    public void remove(String key) {
        diagrams.remove(key);
    }
    
    public boolean isDiagramExist(String name) {
        return $(FolderAreaImpl.selector)
                .findAll(By.className("diagrams"))
                .stream().anyMatch(elem -> elem.has(text(name)));
    }
}
