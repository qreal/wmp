package com.qreal.wmp.uitesting.dia;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Describes Scene of Editor.
 * Can add rm and manipulate with objects on that area.
 */
@Service
public class Scene {

    private static final String selector = ".scene-wrapper";

    private static final Logger logger = LoggerFactory.getLogger(Pallete.class);

    private final Set<SelenideElement> elements = new HashSet<>();

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
        final SelenideElement newEl = updateScene().get();
        elements.add(newEl);
        logger.info("Add element {} to scene", newEl);
        return newEl;
    }

    /**
     * Move element on the Scene by offsets.
     *
     * @param element chosen web element
     * @param offset_x offset of x coordinate
     * @param offset_y offset of y coordinate
     */
    public void moveElement(final SelenideElement element, final int offset_x, final int offset_y) {
        assert exist(element);
        logger.info("Move element {} with offsets {} and {}", element, offset_x, offset_y);
        new Actions(driver).dragAndDropBy(element, offset_x, offset_y).build().perform();
    }

    /** Check if element exist on the scene. */
    public boolean exist(SelenideElement selenideElement) {
        return elements.stream().anyMatch(element -> element.equals(selenideElement));
    }

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
    }

    /** Remove all elements from the scene. */
    public void clean() {
        $$(By.cssSelector(selector + " #v_7 > *")).forEach(element -> remove(element));
        elements.clear();
        logger.info("Clean scene");
    }

    /** Add link between two elements. */
    public SelenideElement addLink(final SelenideElement source, final SelenideElement target) {
        final SelenideElement begin = $(By.cssSelector(selector + " #" + source.attr("id") + " .outPorts"));
        logger.info("Begin element {}", begin);
        new Actions(driver).dragAndDrop(begin, target).build().perform();
        SelenideElement newEl = updateScene().get();
        logger.info("Add link {}", newEl);
        elements.add(newEl);
        return newEl;
    }

    /** Return new element of the scene. */
    private Optional<SelenideElement> updateScene() {
        final List<SelenideElement> allElements = $$(By.cssSelector(selector + " #v_7 > *"));
        return allElements.stream().filter(htmlElement -> !elements.stream().anyMatch(selenideElement ->
                htmlElement.attr("id").equals(selenideElement.attr("id")))).findFirst();

    }

}
