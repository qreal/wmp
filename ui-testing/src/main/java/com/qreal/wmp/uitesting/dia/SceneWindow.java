package com.qreal.wmp.uitesting.dia;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;

/**
 * Describes part of the scene, which is shown on browser.
 */
public class SceneWindow {

    /** Link to full scene. */
    private final Scene scene;

    private final int stepVert;

    private final int stepHor;

    private final WebDriver driver;

    /** Web element of the Scene. */
    private final SelenideElement sceneWrapper;

    /** Constructor takes links to current scene and current driver. */
    public SceneWindow(final Scene scene, final WebDriver driver) {
        this.scene = scene;
        this.driver = driver;
        sceneWrapper =  $(By.cssSelector(".scene-wrapper"));
        stepVert = sceneWrapper.getSize().getHeight() / 12;
        stepHor = sceneWrapper.getSize().getWidth() / 12;
    }

    /**
     * Moves element to the requested position.
     *
     * @param element element to move
     * @param dist position to move
     */
    public void move(final SelenideElement element, final Dimension dist) {
        Dimension src = scene.getPosition(element);
        focus(src);

        if (src.getWidth() < dist.getWidth()) {
            callDragAndDropByX(src.getWidth(), dist.getWidth(), stepHor,
                    new Actions(driver), Keys.ARROW_RIGHT, element).perform();
        } else {
            callDragAndDropByX(src.getWidth(), dist.getWidth(), -stepHor,
                    new Actions(driver), Keys.ARROW_LEFT, element).perform();
        }

        if (src.getHeight() < dist.getHeight()) {
            callDragAndDropByY(src.getHeight(), dist.getHeight(), stepVert,
                    new Actions(driver), Keys.ARROW_DOWN, element).perform();
        } else {
            callDragAndDropByY(src.getHeight(), dist.getHeight(), -stepVert,
                    new Actions(driver), Keys.ARROW_UP, element).perform();
        }

        Dimension currentPosition = scene.getPosition(element);
        new Actions(driver).release().clickAndHold(element).moveByOffset(dist.getWidth() - currentPosition.getWidth(),
                dist.getHeight() - currentPosition.getHeight()).release().perform();
        currentPosition = scene.getPosition(element);
        if (!currentPosition.equals(dist)) {
            move(element, dist);
        }
    }

    /**
     * Move the screen to requested position.
     *
     * @param position position to move
     */
    public void focus(final Dimension position) {
        final Dimension size = sceneWrapper.getSize();
        callMovementAction(0, 2000, stepHor, new Actions(driver), Keys.ARROW_LEFT);
        callMovementAction(0, 2000, stepVert, new Actions(driver), Keys.ARROW_UP);

        callMovementAction(size.getWidth(), position.getWidth(), stepHor,
                new Actions(driver), Keys.ARROW_RIGHT).release().perform();
        callMovementAction(size.getHeight(), position.getHeight(), stepVert,
                new Actions(driver), Keys.ARROW_DOWN).release().perform();
    }

    private Actions callMovementAction(int src, int dst, int step, Actions actions, Keys key) {
        return src < dst ? callMovementAction(src + step, dst, step, actions.sendKeys(key), key) : actions;
    }

    private Actions callDragAndDropByX(int src, int dst, int step, Actions actions, Keys key, SelenideElement element) {
        final List<SelenideElement> elements = scene.getAllBlocks();
        return Math.abs(src - dst) > Math.abs(step) ? callDragAndDropByX(src + step, dst, step,
                actions.sendKeys(key).clickAndHold(element).moveByOffset(jump(elements, step, src), 0), key, element)
                : actions.sendKeys(key, key);
    }

    private Actions callDragAndDropByY(int src, int dst, int step, Actions actions, Keys key, SelenideElement element) {
        final List<SelenideElement> elements = scene.getAllBlocks();
        return Math.abs(src - dst) > Math.abs(step) ? callDragAndDropByY(src + step, dst, step,
                actions.sendKeys(key).clickAndHold(element).moveByOffset(0, jump(elements, step, src)), key, element)
                : actions.sendKeys(key, key);
    }

    private int jump(final List<SelenideElement> elements, int step, int current) {
        if (elements.stream().filter(x -> Math.abs(current - scene.getPosition(x).getWidth())
                        < Math.abs(2 * step)).findFirst().isPresent()) {
            return step + jump(elements, step, step + current);
        } else  {
            return step;
        }
    }
}
