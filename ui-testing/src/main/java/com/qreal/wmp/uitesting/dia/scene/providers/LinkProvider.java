package com.qreal.wmp.uitesting.dia.scene.providers;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import com.qreal.wmp.uitesting.pages.editor.EditorPageFacade;
import com.qreal.wmp.uitesting.services.SelectorService;
import com.qreal.wmp.uitesting.services.SelectorService.Attribute;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$$;

/** Encapsulates links operations. */
public class LinkProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(LinkProvider.class);
    
    private final SelectorService selectorService;
    
    private final WebDriver webDriver;
    
    private final EditorPageFacade editorPageFacade;
    
    private Set<Link> links = new HashSet<>();
    
    private LinkProvider(WebDriver webDriver, EditorPageFacade editorPageFacade, SelectorService selectorService) {
        this.selectorService = selectorService;
        this.webDriver = webDriver;
        this.editorPageFacade = editorPageFacade;
    }
    
    public SelectorService getSelectorService() {
        return selectorService;
    }
    
    public List<Link> getLinks() {
        return Collections.unmodifiableList(links.stream().collect(Collectors.toList()));
    }
    
    public boolean isEmpty() {
        return links.isEmpty();
    }
    
    public boolean exist(Link link) {
        return links.stream().anyMatch(anyLink -> anyLink.getName().equals(link.getName()));
    }
    
    /** Add link between two blocks. */
    public Link addLink(final Block source, final Block target) {
        logger.info("Begin element {}, end element {} ", source, target);
        new Actions(webDriver)
                .release()
                .dragAndDrop(source.getPort().getInnerSeleniumElement(), target.getInnerSeleniumElement())
                .build().perform();
        SelenideElement newEl = updateLinks().orElseThrow(() -> new NoSuchElementException("Link was not created"));
        logger.info("Add link {}", newEl);
        Link res = new Link(
                newEl.attr("id"),
                By.id(newEl.attr("id")),
                editorPageFacade,
                selectorService.create("link"));
        
        links.add(res);
        return res;
    }
    
    /** Scans scene and updates set of links. */
    public void recalculateLinks() {
        links = $$(By.cssSelector(selectorService.get("link", Attribute.SELECTOR))).stream()
              //  .filter(x -> x.attr("class").contains(selectorService.get("link", Attribute.CLASS)))
                .map(x -> new Link(
                        x.attr("id"),
                        By.id(x.attr("id")),
                        editorPageFacade,
                        selectorService.create("link"))
                )
                .collect(Collectors.toSet());
    }
    
    @Contract("_, _, _ -> !null")
    public static LinkProvider getLinkProvider(
            WebDriver webDriver,
            EditorPageFacade facade,
            SelectorService selectorService) {
        
        return new LinkProvider(webDriver, facade, selectorService);
    }
    
    /** Returns new link if it was created. */
    public Optional<SelenideElement> updateLinks() {
        final Map<String, Link> linkMap = links.stream()
                .collect(Collectors.toMap(Link::getName, link -> link));
        return $$(By.cssSelector(selectorService.get("link", Attribute.SELECTOR))).stream()
                .filter(el -> !linkMap.containsKey(el.attr("id"))).findFirst();
    }
}
