package com.qreal.wmp.uitesting.dia.scene.providers;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class LinkProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(LinkProvider.class);
    
    private final String selector;
    
    private final WebDriver webDriver;
    
    private Set<Link> links = new HashSet<>();
    
    private LinkProvider(String selector, WebDriver webDriver) {
        this.selector = selector;
        this.webDriver = webDriver;
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
        final SelenideElement begin = $(By.cssSelector(selector + " #" +
                source.getInnerSeleniumElement().attr("id") + " .outPorts"));
        logger.info("Begin element {}, end element {} ", begin, target);
        new Actions(webDriver)
                .dragAndDrop(source.getPort().getInnerSeleniumElement(), target.getInnerSeleniumElement())
                .build().perform();
        SelenideElement newEl = updateLinks().orElseThrow(() -> new NoSuchElementException("Link was not created"));
        logger.info("Add link {}", newEl);
        Link res = new Link(newEl.attr("id"), newEl);
        links.add(res);
        return res;
    }
    
    public void recalculateLinks() {
        links = $$(By.cssSelector(selector + " #v_7 > *")).stream()
                .filter(x -> x.attr("class").contains(Link.CLASS_NAME))
                .map(x -> new Link(x.attr("id"), x))
                .collect(Collectors.toSet());
    }
    
    @Contract("_, _ -> !null")
    public static LinkProvider getLinkProvider(String selector, WebDriver webDriver) {
        return new LinkProvider(selector, webDriver);
    }
    
    private Optional<SelenideElement> updateLinks() {
        final List<SelenideElement> allElements = $$(By.cssSelector(selector + " #v_7 > *"));
        return allElements.stream()
                .filter(htmlElement ->
                        htmlElement.attr("class").contains("link") &&
                                links.stream().noneMatch(link -> htmlElement.attr("id")
                                        .equals(link.getInnerSeleniumElement().attr("id")))
                ).findFirst();
    }
}
