package com.qreal.wmp.uitesting.dia.model;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.services.Scene;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
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
    public void move(final Block element, final Coordinate dist) throws ElementNotOnTheSceneException {
        Coordinate src = element.getCoordinateOnScene();
        //focus(src);

        if (src.getXAbsolute() < dist.getXAbsolute()) {
            callDragAndDropByX(src.getXAbsolute(), dist.getXAbsolute(), stepHor,
                    new Actions(driver), Keys.ARROW_RIGHT, element.getInnerSeleniumElement()).perform();
        } else {
            callDragAndDropByX(src.getXAbsolute(), dist.getXAbsolute(), -stepHor,
                    new Actions(driver), Keys.ARROW_LEFT, element.getInnerSeleniumElement()).perform();
        }

        if (src.getYAbsolute() < dist.getYAbsolute()) {
            callDragAndDropByY(src.getYAbsolute(), dist.getYAbsolute(), stepVert,
                    new Actions(driver), Keys.ARROW_DOWN, element.getInnerSeleniumElement()).perform();
        } else {
            callDragAndDropByY(src.getYAbsolute(), dist.getYAbsolute(), -stepVert,
                    new Actions(driver), Keys.ARROW_UP, element.getInnerSeleniumElement()).perform();
        }

        if (!finalJump(element, dist).equals(dist)) {
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
        final List<Block> elements = scene.getBlocks();
        return Math.abs(src - dst) > Math.abs(step) ? callDragAndDropByX(src + step, dst, step,
                actions.sendKeys(key).clickAndHold(element).moveByOffset(jump(elements, 2 * step, src), 0),
                key, element) : actions.sendKeys(key, key);
    }

    private Actions callDragAndDropByY(int src, int dst, int step, Actions actions, Keys key, SelenideElement element) {
        final List<Block> elements = scene.getBlocks();
        return Math.abs(src - dst) > Math.abs(step) ? callDragAndDropByY(src + step, dst, step,
                actions.sendKeys(key).clickAndHold(element).moveByOffset(0, jump(elements, 2 * step, src)),
                key, element) : actions.sendKeys(key, key);
    }

    private int jump(final List<Block> elements, int step, int current) {
        if (elements.stream().filter(x -> {
            try {
                return Math.abs(current - x.getCoordinateOnScene().getXAbsolute())
                        < Math.abs(2 * step);
            } catch (ElementNotOnTheSceneException e) {
                e.printStackTrace();
            }
            return false;
        }).findFirst().isPresent()) {
            return step + jump(elements, step, step + current);
        } else  {
            return step;
        }
    }
    
    private Coordinate finalJump(Block block, Coordinate dist) throws ElementNotOnTheSceneException {
        
        Coordinate currentPosition = block.getCoordinateOnScene();
    
        new Actions(driver).release().clickAndHold(block.getInnerSeleniumElement()).moveByOffset(
                dist.getXAbsolute() - currentPosition.getXAbsolute(),
                dist.getYAbsolute() - currentPosition.getYAbsolute()).release().perform();
        
        return  block.getCoordinateOnScene();
    }
}
