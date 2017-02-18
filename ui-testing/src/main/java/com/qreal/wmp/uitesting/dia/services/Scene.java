package com.qreal.wmp.uitesting.dia.services;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.qreal.wmp.uitesting.dia.model.*;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Describes Scene of Editor.
 * Can add rm and manipulate with objects on that area.
 */
public class Scene {

    public static final String selector = ".scene-wrapper";

    private static final Logger logger = LoggerFactory.getLogger(Pallete.class);

    private Set<Block> blocks = new HashSet<>();
    
    private Set<Link> links = new HashSet<>();

    private WebDriver webDriver;
    
    /** For actions such as mouse move we need driver of current page. */
    public Scene(WebDriver webDriver) {
        this.webDriver = webDriver;
    }
    
    public void init() {
        if (webDriver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) webDriver).executeScript(
                    createDiv("SceneWindowLeft") + createDiv("SceneWindowTop") +
                            createDiv("SceneWindowHorSize") + createDiv("SceneWindowVerSize")
            );
        }
    }

    /**
     * Drag element from scene or pallete and put it on the center of scene.
     *
     * @param element chosen web element
     * @return element from scene
     */
    public Block dragAndDrop(final SelenideElement element) {
        element.dragAndDropTo(selector);
        final SelenideElement newEl = updateBlocks().orElseThrow(NotFoundException::new);
        Block newBlock = new Block("name", newEl);
        blocks.add(newBlock);
        logger.info("Add element {} to scene", newEl);
        return newBlock;
    }

    /**
     *  element from scene or pallete and put it in cell of the scene.
     *
     * @param element chosen web element
     * @param cell_x x-coordinate of cell
     * @param cell_y y-coordinate of cell
     * @return element from scene
     */
    public Block dragAndDrop(final SelenideElement element, int cell_x, int cell_y) {
        Block newBlock = dragAndDrop(element);
        moveToCell(newBlock, cell_x, cell_y);
        return newBlock;
    }

    /** Move element to cell. */
    public void moveToCell(final Block block, final int cell_x, final int cell_y) {
        SceneWindow sceneWindow = new SceneWindow(this, webDriver);
        logger.info("Move element {} to cell ({}, {})", block, cell_x, cell_y);
        try {
            sceneWindow.move(block, new Coordinate(cell_x * 25, cell_y * 25));
        } catch (ElementNotOnTheSceneException e) {
            e.printStackTrace();
        }
    }

    /** Focus the element. */
    public void focus(final SceneElement element) {
        SceneWindow sceneWindow = new SceneWindow(this, webDriver);
        logger.info("Focus on the element {}", element);
        try {
            sceneWindow.focus(element.getCoordinateOnScene());
        } catch (ElementNotOnTheSceneException e) {
            e.printStackTrace();
        }
    }

    /** Check if element exist on the scene. */
    public boolean exist(SceneElement element) {
        return blocks.stream().anyMatch(element::equals) || links.stream().anyMatch(element::equals);
    }
    
    /** Remove element from the scene. */
    public void remove(Block block) {
        removeSceneElement(block);
    }
    
    public void remove(Link link) {
        removeSceneElement(link.getSource());
    }

    /** Remove all elements from the scene. */
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

    /** Add link between two elements. */
    public Link addLink(final Block source, final Block target) {
        final SelenideElement begin = $(By.cssSelector(selector + " #" +
                source.getInnerSeleniumElement().attr("id") + " .outPorts"));
        logger.info("Begin element {}, end element {} ", begin, target);
        new Actions(webDriver).dragAndDrop(source.getPort().getInnerSeleniumElement(),
                target.getInnerSeleniumElement()).build().perform();
        SelenideElement newEl = updateLinks().get();
        logger.info("Add link {}", newEl);
        Link res = new Link("link", newEl);
        links.add(res);
        return res;
    }

    /** Return all blocks. */
    public List<Block> getBlocks() {
        return blocks.stream().collect(Collectors.toList());
    }
    
    /** Return new element of the scene. */
    private Optional<SelenideElement> updateBlocks() {
        final List<SelenideElement> allElements = $$(By.cssSelector(selector + " #v_7 > *"));
        return allElements.stream().filter(htmlElement ->
                htmlElement.attr("class").contains("element devs ImageWithPorts") &&
                        blocks.stream().noneMatch(block ->
                                block.getInnerSeleniumElement().attr("id").equals(htmlElement.attr("id")))).findFirst();
    }
    
    private Optional<SelenideElement> updateLinks() {
        final List<SelenideElement> allElements = $$(By.cssSelector(selector + " #v_7 > *"));
        return allElements.stream().filter(htmlElement -> htmlElement.attr("class").contains("link") &&
                links.stream().noneMatch(link ->
                    htmlElement.attr("id").equals(link.getInnerSeleniumElement().attr("id")))).findFirst();
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
            e.printStackTrace();
        }
        $(By.id("scene-context-menu")).click();
        $$(By.cssSelector(selector + " #v_7 > *")).stream().forEach(System.out::println);
        blocks = $$(By.cssSelector(selector + " #v_7 > *")).stream().filter(x -> x.attr("class").equals(Block.className)).map(x ->
                new Block("name", x)).collect(Collectors.toSet());
        links = $$(By.cssSelector(selector + " #v_7 > *")).stream().filter(x -> x.attr("class").equals(Link.className)).map(x ->
                new Link("name", x)).collect(Collectors.toSet());
    }
}
