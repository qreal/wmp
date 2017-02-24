package com.qreal.wmp.uitesting.dia.scene;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.pallete.PalleteElement;
import com.qreal.wmp.uitesting.dia.pallete.PalleteImpl;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import com.qreal.wmp.uitesting.dia.scene.elements.SceneElement;
import com.qreal.wmp.uitesting.dia.scene.window.SceneWindow;
import com.qreal.wmp.uitesting.dia.scene.window.SceneWindowImpl;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Describes Scene of Editor.
 * Can add rm and manipulate with objects on that area.
 */
public class SceneImpl implements Scene {

    private static final String SELECTOR = ".scene-wrapper";

    private static final Logger logger = LoggerFactory.getLogger(PalleteImpl.class);
    
    private final WebDriver webDriver;
    
    private final SceneWindow sceneWindow;
    
    private Set<Block> blocks = new HashSet<>();
    
    private Set<Link> links = new HashSet<>();
    
    /** For actions such as mouse move we need driver of current page. */
    private SceneImpl(WebDriver webDriver) {
        this.webDriver = webDriver;
        /** For actions such as mouse move we need driver of current page. */
        if (webDriver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) webDriver).executeScript(
                    createDiv("SceneWindowLeft") + createDiv("SceneWindowTop") +
                            createDiv("SceneWindowHorSize") + createDiv("SceneWindowVerSize")
            );
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        sceneWindow = SceneWindowImpl.getSceneWindow(this, webDriver);
    }
    
    @Override
    public String getSelector() {
        return SELECTOR;
    }
    
    @Override
    public Block dragAndDrop(final PalleteElement element) {
        element.getInner().dragAndDropTo(SELECTOR);
        final SelenideElement newEl = updateBlocks().orElseThrow(NotFoundException::new);
        Block newBlock = new Block("name", newEl);
        blocks.add(newBlock);
        logger.info("Add element {} to scene", newEl);
        return newBlock;
    }
    
    @Override
    public Block dragAndDrop(final PalleteElement element, int cell_x, int cell_y) {
        Block newBlock = dragAndDrop(element);
        moveToCell(newBlock, cell_x, cell_y);
        return newBlock;
    }
    
    @Override
    public void moveToCell(final Block block, final int cell_x, final int cell_y) {
        logger.info("Move element {} to cell ({}, {})", block, cell_x, cell_y);
        try {
            sceneWindow.move(block, new Coordinate(cell_x * 25, cell_y * 25));
        } catch (ElementNotOnTheSceneException e) {
            logger.error(e.getMessage());
        }
    }
    
    @Override
    public void focus(final SceneElement element) {
        logger.info("Focus on the element {}", element);
        try {
            sceneWindow.focus(element.getCoordinateOnScene());
        } catch (ElementNotOnTheSceneException e) {
            logger.error(e.getMessage());
        }
    }
    
    @Override
    public boolean exist(SceneElement element) {
        return blocks.stream().anyMatch(element::equals) || links.stream().anyMatch(element::equals);
    }
    
    @Override
    public void remove(SceneElement element) {
        if (element instanceof Link) {
            removeSceneElement(((Link) element).getSource());
        } else {
            removeSceneElement(element);
        }
    }
    
    @Override
    public void clean() {
        if (!links.isEmpty()) {
            remove(links.stream().collect(Collectors.toList()).get(0));
            clean();
        } else {
            if (!blocks.isEmpty()) {
                remove(blocks.stream().collect(Collectors.toList()).get(0));
                clean();
            } else {
                logger.info("Clean scene");
            }
        }
    }
    
    @Override
    public Link addLink(final Block source, final Block target) {
        final SelenideElement begin = $(By.cssSelector(SELECTOR + " #" +
                source.getInnerSeleniumElement().attr("id") + " .outPorts"));
        logger.info("Begin element {}, end element {} ", begin, target);
        new Actions(webDriver)
                .dragAndDrop(source.getPort().getInnerSeleniumElement(), target.getInnerSeleniumElement())
                .build().perform();
        SelenideElement newEl = updateLinks().orElseThrow(() -> new NoSuchElementException("Link was not created"));
        logger.info("Add link {}", newEl);
        Link res = new Link("link", newEl);
        links.add(res);
        return res;
    }
    
    @Override
    public List<Block> getBlocks() {
        return blocks.stream().collect(Collectors.toList());
    }
    
    public static Scene getScene(WebDriver webDriver) {
        return new SceneImpl(webDriver);
    }
    
    /** Return new element of the scene. */
    private Optional<SelenideElement> updateBlocks() {
        final List<SelenideElement> allElements = $$(By.cssSelector(SELECTOR + " #v_7 > *"));
        return allElements.stream()
                .filter(htmlElement ->
                        htmlElement.attr("class").contains("element devs ImageWithPorts") &&
                                blocks.stream().noneMatch(block -> block.getInnerSeleniumElement()
                                        .attr("id").equals(htmlElement.attr("id")))
                ).findFirst();
    }
    
    private Optional<SelenideElement> updateLinks() {
        final List<SelenideElement> allElements = $$(By.cssSelector(SELECTOR + " #v_7 > *"));
        return allElements.stream()
                .filter(htmlElement ->
                        htmlElement.attr("class").contains("link") &&
                                links.stream().noneMatch(link -> htmlElement.attr("id")
                                        .equals(link.getInnerSeleniumElement().attr("id")))
                ).findFirst();
    }
    
    private static String createDiv(String divName) {
        return "$('body').append('<div id=\"" + divName + "\" style=\"position:absolute;visibility:hidden;\"></div>');";
    }
    
    private void removeSceneElement(SceneElement sceneElement) {
        focus(sceneElement);
        assert sceneElement != null;
        logger.info("Remove element {} form scene", sceneElement.getInnerSeleniumElement().toString());
        new Actions(webDriver).contextClick(sceneElement.getInnerSeleniumElement()).build().perform();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        $(By.id("scene-context-menu")).click();
        blocks = $$(By.cssSelector(SELECTOR + " #v_7 > *")).stream()
                .filter(x -> x.attr("class").contains(Block.CLASS_NAME))
                .map(x -> new Block("name", x))
                .collect(Collectors.toSet());
        
        links = $$(By.cssSelector(SELECTOR + " #v_7 > *")).stream()
                .filter(x -> x.attr("class").contains(Link.CLASS_NAME))
                .map(x -> new Link("name", x))
                .collect(Collectors.toSet());
    }
}
