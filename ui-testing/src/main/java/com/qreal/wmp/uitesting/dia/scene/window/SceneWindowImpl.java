package com.qreal.wmp.uitesting.dia.scene.window;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;

/**
 * Describes part of the scene, which is shown on browser.
 */
public class SceneWindowImpl implements SceneWindow {

    /** Link to full scene. */
    private final Scene scene;

    private int stepVert;

    private int stepHor;

    private final WebDriver driver;
    
    private static final Logger logger = LoggerFactory.getLogger(SceneWindowImpl.class);
    
    /** Constructor takes links to current scene and current driver. */
    public SceneWindowImpl(final Scene scene, final WebDriver driver) {
        this.scene = scene;
        this.driver = driver;
        updateSteps();
    }
    
    /**
     * Moves element to the requested position.
     *
     * @param element element to move
     * @param dist position to move
     */
    @Override
    public void move(final Block element, final Coordinate dist) throws ElementNotOnTheSceneException {
        Coordinate src = element.getCoordinateOnScene();
        focus(src);

        if (src.getXAbsolute() < dist.getXAbsolute()) {
            callDragAndDropByX(
                    src.getXAbsolute(), dist.getXAbsolute(), stepHor,
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

    @Override
    public void focus(final Coordinate coordinate) {
        logger.debug("Focus to " + coordinate.getXAbsolute() + " " + coordinate.getYAbsolute());
        horizontalWindowMovement(coordinate.getXAbsolute());
        verticalWindowMovement(coordinate.getYAbsolute());
    }
    
    private void horizontalWindowMovement(int horizontal) {
        int sizeHor = Double.valueOf($(By.id("SceneWindowHorSize")).innerHtml()).intValue();
        int left = Double.valueOf($(By.id("SceneWindowLeft")).innerHtml()).intValue();
        logger.debug("focus horizontal " + left + " " + sizeHor);
        
        if (left + sizeHor * 2 / 3 < horizontal) {
            sendKey(Keys.RIGHT);
            updateCanvasInfo();
            if (Double.valueOf($(By.id("SceneWindowLeft")).innerHtml()).intValue() != left) {
                horizontalWindowMovement(horizontal);
            }
        }
        if (left + sizeHor / 3 > horizontal) {
            sendKey(Keys.LEFT);
            updateCanvasInfo();
            if (Double.valueOf($(By.id("SceneWindowLeft")).innerHtml()).intValue() != left) {
                horizontalWindowMovement(horizontal);
            }
        }
    }
    
    private void verticalWindowMovement(int vertical) {
        int sizeVer = Double.valueOf($(By.id("SceneWindowVerSize")).innerHtml()).intValue();
        int top = Double.valueOf($(By.id("SceneWindowTop")).innerHtml()).intValue();
        logger.info("focus vertical " + top + " " + sizeVer);
    
        if (top + sizeVer * 2 / 3 < vertical) {
            sendKey(Keys.DOWN);
            updateCanvasInfo();
            if (Double.valueOf($(By.id("SceneWindowTop")).innerHtml()).intValue() != top) {
                verticalWindowMovement(vertical);
            }
        }
        if (top + sizeVer / 3 > vertical) {
            sendKey(Keys.UP);
            updateCanvasInfo();
            if (Double.valueOf($(By.id("SceneWindowTop")).innerHtml()).intValue() != top) {
                verticalWindowMovement(vertical);
            }
        }
    }

    private Actions callDragAndDropByX(int src, int dst, int step, Actions actions, Keys key, SelenideElement element) {
        final List<Block> elements = scene.getBlocks();
        return Math.abs(src - dst) > Math.abs(step) ?
                callDragAndDropByX(src + step, dst, step,
                        actions.sendKeys(key)
                                .clickAndHold(element)
                                .moveByOffset(jump(elements, 2 * step, src), 0), key, element
                ) : actions.sendKeys(key, key);
    }

    private Actions callDragAndDropByY(int src, int dst, int step, Actions actions, Keys key, SelenideElement element) {
        final List<Block> elements = scene.getBlocks();
        return Math.abs(src - dst) > Math.abs(step) ?
                callDragAndDropByY(src + step, dst, step,
                        actions.sendKeys(key)
                                .clickAndHold(element)
                                .moveByOffset(0, jump(elements, 2 * step, src)), key, element
                ) : actions.sendKeys(key, key);
    }

    private int jump(final List<Block> elements, int step, int current) {
        if (elements.stream().anyMatch(x -> {
            try {
                return Math.abs(current - x.getCoordinateOnScene().getXAbsolute()) < Math.abs(2 * step);
            } catch (ElementNotOnTheSceneException e) {
                logger.error(e.getMessage());
            }
            return false;
        })) {
            return step + jump(elements, step, step + current);
        } else  {
            return step;
        }
    }
    
    private Coordinate finalJump(Block block, Coordinate dist) throws ElementNotOnTheSceneException {
        Coordinate currentPosition = block.getCoordinateOnScene();
    
        new Actions(driver).release()
                .clickAndHold(block.getInnerSeleniumElement())
                .moveByOffset(
                        dist.getXAbsolute() - currentPosition.getXAbsolute(),
                        dist.getYAbsolute() - currentPosition.getYAbsolute()
                ).release().perform();
        
        return  block.getCoordinateOnScene();
    }
    
    private void updateCanvasInfo() {
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript("var canvas = " +
                    "document.getElementsByClassName(\"scene-wrapper\")[0]; " +
                    "var BB=canvas.getBoundingClientRect();" +
                    "$('#SceneWindowLeft').html(canvas.scrollLeft);" +
                    "$('#SceneWindowTop').html(canvas.scrollTop);" +
                    "$('#SceneWindowHorSize').html(BB.right - BB.left);" +
                    "$('#SceneWindowVerSize').html(BB.bottom - BB.top);"
            );
        }
    }
    
    // todo: make it waits real time until action is completed
    private void sendKey(Keys key) {
        new Actions(driver).sendKeys(key).perform();
        try {
            // wait hard coded time until action is completed
            Thread.sleep(50);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
    
    private void updateSteps() {
        updateCanvasInfo();
        $(scene.getSelector()).click();
        focus(new Coordinate(0, 0));
        updateCanvasInfo();
        sendKey(Keys.DOWN);
        sendKey(Keys.RIGHT);
        updateCanvasInfo();
        stepHor = Double.valueOf($(By.id("SceneWindowLeft")).innerHtml()).intValue();
        stepVert = Double.valueOf($(By.id("SceneWindowTop")).innerHtml()).intValue();
    }
}