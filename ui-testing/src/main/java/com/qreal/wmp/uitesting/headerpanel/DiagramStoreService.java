package com.qreal.wmp.uitesting.headerpanel;

import com.codeborne.selenide.SelenideElement;
import com.google.common.base.Predicate;
import com.qreal.wmp.uitesting.dia.scene.SceneImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class DiagramStoreService {
    
    private final Map<String, Element> diagrams = new HashMap<>();
    
    private final By sceneSelector = By.cssSelector(SceneImpl.SELECTOR);
    
    public void saveDiagram(String key) {
        SelenideElement element = $(By.cssSelector(".saving-menu")).find(By.cssSelector(":nth-child(2)"));
        element.setValue(key);
        diagrams.put(key, prepareElement(Jsoup.parseBodyFragment($(sceneSelector).innerHtml()).body()));
        $(By.id("saving")).click();
    }
    
    public boolean equalsDrigrams(String key) {
        return diagrams.get(key).toString().equals(prepareElement(
                Jsoup.parseBodyFragment($(sceneSelector).innerHtml()).body()).toString()
        );
    }
    
    public void openDiagram(String key) {
        $(FolderAreaImpl.selector)
                .findAll(By.className("diagrams"))
                .stream().filter(elem -> elem.has(text(key)))
                .findFirst().ifPresent(SelenideElement::click);
    }
    
    public void remove(String key) {
        diagrams.remove(key);
    }
    
    public boolean isDiagramExist(String key) {
        return $(FolderAreaImpl.selector)
                .findAll(By.className("diagrams"))
                .stream().anyMatch(elem -> elem.has(text(key)));
    }
    
    private Element prepareElement(Element element) {
        removeByAttr(element, "id", el -> el.attr("id").startsWith("j_")
                || el.attr("id").startsWith("v_")
                || el.attr("id").startsWith("v-")
        );
        removeByAttr(element, "model-id");
        removeByAttr(element, "data-id");
        removeByAttr(element, "port", el -> el.attr("port").startsWith("out"));
        removeByAttr(element, "style", el -> el.attr("style").contains("pointer-events:"));
        element.getAllElements().stream()
                .filter(el -> el.className().startsWith("Ports-"))
                .forEach(el -> el.removeAttr("class"));
        
        element.getAllElements().stream()
                .filter(el -> el.className().contains("selected"))
                .forEach(el -> el.attr("class", el.className().replace(" selected", "")));
        
        Elements sorted = new Elements(
                Arrays.stream(element.toString().split("\n"))
                        .sorted()
                        .map(Element::new)
                        .collect(Collectors.toList())
        );
        return new Element(sorted.outerHtml());
    }
    
    private void removeByAttr(Element element, String attr, Predicate<Element> cond) {
        element.getElementsByAttribute(attr).stream().filter(cond::apply).forEach(el -> el.removeAttr(attr));
    }
    
    private void removeByAttr(Element element, String attr) {
        element.getElementsByAttribute(attr).forEach(el -> el.removeAttr(attr));
    }
}
