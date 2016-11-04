package com.qreal.wmp.uitesting.dia;

import com.codeborne.selenide.SelenideElement;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Describes Scene of Editor.
 * Can add rm and manipulate with objects on that area.
 */
@Service
public class Scene {

    private final String selector = ".scene-wrapper";

    private Set<SelenideElement> elements = new HashSet<>();

    private WebDriver driver;

    private static final Logger logger = LoggerFactory.getLogger(Pallete.class);

    /** For actions such as mouse move we need driver of current page. */
    public void updateWebdriver(WebDriver webDriver) {
        driver = webDriver;
    }

    /**
     * Drag element from scene or pallete and put it on the center of scene.
     *
     * @param element chosen web element
     * @return element from scene
     */
    public SelenideElement dragAndDrop(SelenideElement element) {
        List<SelenideElement> all = $$(By.cssSelector(selector + " #v_7 > *"));
        element.dragAndDropTo(selector);
        SelenideElement newEl = all.stream().filter(x ->
                !elements.stream().anyMatch(y -> x.attr("id").equals(y.attr("id")))).findFirst().orElse(element);
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
    public void moveElement(SelenideElement element, int offset_x, int offset_y) {
        assert exist(element);
        logger.info("Move element {} with offsets {} and {}", element, offset_x, offset_y);
        new Actions(driver).dragAndDropBy(element, offset_x, offset_y).build().perform();
    }

    /** Check if element exist on the scene. */
    public boolean exist(SelenideElement selenideElement) {
        return elements.stream().anyMatch(x -> x.equals(selenideElement));
    }

    public Pair getPosition(SelenideElement selenideElement) {
        String position = selenideElement.attr("transform");
        String[] pairStr = position.substring(position.indexOf('(') + 1, position.indexOf(')')).split(",");
        return new ImmutablePair(Integer.valueOf(pairStr[0]), Integer.valueOf(pairStr[1]));
    }

    /** Remove element from the scene. */
    public void remove(SelenideElement selenideElement) {
        logger.info("Remove element {} form scene", selenideElement);
        elements.remove(selenideElement);
        new Actions(driver).contextClick(selenideElement).build().perform();
        $(By.id("scene-context-menu")).click();
    }

    /** Remove all elements from the scene. */
    public void clean() {
        elements.forEach(x -> {
            new Actions(driver).contextClick(x).build().perform();
            $(By.id("scene-context-menu")).click();
        });
        elements.clear();
        logger.info("Clean scene");
    }

}
