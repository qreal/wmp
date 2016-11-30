package com.qreal.wmp.uitesting.dia;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Describes Scene of Editor.
 * Can add rm and manipulate with objects on that area.
 */
@Service
public class Scene {

    public static final String selector = ".scene-wrapper";

    private static final Logger logger = LoggerFactory.getLogger(Pallete.class);

    private Set<SelenideElement> elements = new HashSet<>();

    private WebDriver driver;

    /** For actions such as mouse move we need driver of current page. */
    public void updateWebdriver(final WebDriver webDriver) {
        driver = webDriver;
    }

    /**
     * Drag element from scene or pallete and put it on the center of scene.
     *
     * @param element chosen web element
     * @return element from scene
     */
    public SelenideElement dragAndDrop(final SelenideElement element) {
        element.dragAndDropTo(selector);
        final SelenideElement newEl = getNewElement().get();
        elements.add(newEl);
        logger.info("Add element {} to scene", newEl);
        return newEl;
    }

    /**
     *  element from scene or pallete and put it in cell of the scene.
     *
     * @param element chosen web element
     * @param cell_x x-coordinate of cell
     * @param cell_y y-coordinate of cell
     * @return element from scene
     */
    public SelenideElement dragAndDrop(final SelenideElement element, int cell_x, int cell_y) {
        SelenideElement newEl = dragAndDrop(element);
        moveToCell(newEl, cell_x, cell_y);
        return newEl;
    }

    /** Move element to cell. */
    public void moveToCell(final SelenideElement element, final int cell_x, final int cell_y) {
        SceneWindow sceneWindow = new SceneWindow(this, driver);
        logger.info("Move element {} to cell ({}, {})", element, cell_x, cell_y);
        sceneWindow.move(element, new Dimension(cell_x * 25, cell_y * 25));
    }

    /** Focus the element. */
    public void focus(final SelenideElement element) {
        SceneWindow sceneWindow = new SceneWindow(this, driver);
        logger.info("Focus on the element {}", element);
        sceneWindow.focus(getPosition(element));
    }

    /** Return the cell where element hold. */
    public Dimension getCell(SelenideElement element) {
        Dimension result = getPosition(element);
        return new Dimension(result.getWidth() / 25, result.getHeight() / 25);
    }

    /** Check if element exist on the scene. */
    public boolean isExist(SelenideElement selenideElement) {
        return elements.stream().anyMatch(element -> element.equals(selenideElement));
    }

    /** Return the position of element in coordinates. */
    public Dimension getPosition(final SelenideElement selenideElement) {
        final String position = selenideElement.attr("transform");
        final String[] pairStr = position.substring(position.indexOf('(') + 1, position.indexOf(')')).split(",");
        return new Dimension(Integer.valueOf(pairStr[0]), Integer.valueOf(pairStr[1]));
    }

    /** Remove element from the scene. */
    public void remove(final SelenideElement selenideElement) {
        assert selenideElement != null;
        logger.info("Remove element {} form scene", selenideElement);
        elements.remove(selenideElement);
        new Actions(driver).contextClick(selenideElement).build().perform();
        $(By.id("scene-context-menu")).click();
        updateScene();
    }

    /** Remove all elements from the scene. */
    public void clean() {
        updateScene();
        if (!elements.isEmpty()) {
            List<SelenideElement> blocks = getAllBlocks();
            if (!blocks.isEmpty()) {
                remove(blocks.get(0));
            }
            clean();
        } else {
            logger.info("Clean scene");
        }
    }

    /** Add link between two elements. */
    public SelenideElement addLink(final SelenideElement source, final SelenideElement target) {
        final SelenideElement begin = $(By.cssSelector(selector + " #" + source.attr("id") + " .outPorts"));
        logger.info("Begin element {}, end element {} ", begin, target);
        new Actions(driver).dragAndDrop(begin, target).build().perform();
        SelenideElement newEl = getNewElement().get();
        logger.info("Add link {}", newEl);
        elements.add(newEl);
        return newEl;
    }

    /** Return all blocks. */
    public List<SelenideElement> getAllBlocks() {
        List<SelenideElement> result = new ArrayList<>();
        elements.stream().filter(x -> !x.attr("class").equals("link")).forEach(result::add);
        return result;
    }

    /** Return new element of the scene. */
    private Optional<SelenideElement> getNewElement() {
        final List<SelenideElement> allElements = $$(By.cssSelector(selector + " #v_7 > *"));
        return allElements.stream().filter(htmlElement -> !elements.stream().anyMatch(selenideElement ->
                htmlElement.attr("id").equals(selenideElement.attr("id")))).findFirst();

    }

    private void updateScene() {
        Set<SelenideElement> newSet = new HashSet<>();
        $$(By.cssSelector(selector + " #v_7 > *")).stream().filter(x -> x != null).forEach(newSet::add);
        elements = newSet;
    }

}
