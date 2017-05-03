package com.qreal.wmp.uitesting.headerpanel;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.headerpanel.folderwindow.FolderAreaImpl;
import com.qreal.wmp.uitesting.pages.editor.EditorPageFacade;
import com.qreal.wmp.uitesting.services.SelectorService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

/**
 * Keeps custom html representations of saved diagrams.
 * Also makes final steps of save/open action in folder menu.
 */
public class DiagramStoreService {
    
    private final Map<String, Element> diagrams = new HashMap<>();
    
    private final By sceneSelector;
    
    private final SelectorService selectorService;
    
    private String lastKnownKey;
    
    public DiagramStoreService(EditorPageFacade facade, SelectorService selectorService) {
        this.selectorService = selectorService;
        sceneSelector = By.className(facade.getEditorPageSelectors().get("scene", SelectorService.Attribute.CLASS));
    }
    
    
    /** Saves diagram. */
    public void saveDiagram(String key) {
        SelenideElement element =
                $(By.id(selectorService.get("savingMenu.savingInput", SelectorService.Attribute.ID)));
        element.setValue(getFilename(key));
        diagrams.put(key, prepareElement(Jsoup.parseBodyFragment($(sceneSelector).innerHtml()).body()));
        $(By.id(selectorService.get("savingMenu.savingItem", SelectorService.Attribute.ID))).click();
        $(By.id(selectorService.get("savingMenu.savingItem", SelectorService.Attribute.ID)))
                .shouldBe(Condition.disappear);
        lastKnownKey = key;
    }
    
    /**
     * Key is the last knownKey.
     * Call when user click save button (not SaveAs).
     */
    public void saveDiagram() {
        diagrams.put(lastKnownKey, prepareElement(Jsoup.parseBodyFragment($(sceneSelector).innerHtml()).body()));
    }
    
    /** Check if current diagram(now in the scene) equals diagram which is kept in store. */
    public boolean equalsDrigrams(String key) {
        return diagrams.get(key).toString().equals(prepareElement(
                Jsoup.parseBodyFragment($(sceneSelector).innerHtml()).body()).toString()
        );
    }
    
    /** Opens diagram. */
    public void openDiagram(String key) {
        String filename = getFilename(key);
        $(FolderAreaImpl.selector)
                .findAll(By.className("diagrams"))
                .stream().filter(elem -> elem.has(text(filename)))
                .findFirst().ifPresent(SelenideElement::click);
    }
    
    public void remove(String key) {
        diagrams.remove(key);
    }
    
    public boolean isDiagramExist(String key) {
        String filename = getFilename(key);
        return $(FolderAreaImpl.selector)
                .findAll(By.className("diagrams"))
                .stream().anyMatch(elem -> elem.has(text(filename)));
    }
    
    /**
     * Diagrams can be the same but have different generated ids.
     * It depends on how we restore them by opening.
     * In this case, we believe that they are the same.
     * So we get rid of bad ids.
     */
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
        element.getElementsByAttribute(attr).stream().filter(cond).forEach(el -> el.removeAttr(attr));
    }
    
    private void removeByAttr(Element element, String attr) {
        element.getElementsByAttribute(attr).forEach(el -> el.removeAttr(attr));
    }
    
    private String getFilename(String path) {
        String steps[] = path.split("/");
        return steps[steps.length - 1];
    }
}
