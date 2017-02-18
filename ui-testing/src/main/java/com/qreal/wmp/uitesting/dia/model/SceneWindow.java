package com.qreal.wmp.uitesting.dia.model;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.services.Scene;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;

/**
 * Describes part of the scene, which is shown on browser.
 */
public class SceneWindow {

    /** Link to full scene. */
    private final Scene scene;

    private int stepVert;

    private int stepHor;

    private final WebDriver driver;
    
    private static final Logger logger = LoggerFactory.getLogger(SceneWindow.class);
    
    
    /** Constructor takes links to current scene and current driver. */
    public SceneWindow(final Scene scene, final WebDriver driver) {
        this.scene = scene;
        this.driver = driver;
        stepHor = 40;
        stepVert = 40;
        updateCanvasInfo(driver);
    }

    /**
     * Moves element to the requested position.
     *
     * @param element element to move
     * @param dist position to move
     */
    public void move(final Block element, final Coordinate dist) throws ElementNotOnTheSceneException {
        Coordinate src = element.getCoordinateOnScene();
        focus(src);

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
     * @param coordinate coordinate to move
     */
    public void focus(final Coordinate coordinate) {
        logger.info("Focus to " + coordinate.getXAbsolute() + " " + coordinate.getYAbsolute());
        horizontalWindowMovement(coordinate.getXAbsolute());
        verticalWindowMovement(coordinate.getYAbsolute());
    }
    
    private void horizontalWindowMovement(int horizontal) {
        int sizeHor = Double.valueOf($(By.id("SceneWindowHorSize")).innerHtml()).intValue();
        int left = Double.valueOf($(By.id("SceneWindowLeft")).innerHtml()).intValue();
        logger.info("focus horizontal " + left + " " + sizeHor);
        
        if (left + sizeHor * 2 / 3 < horizontal) {
            sendKey(Keys.RIGHT);
            updateCanvasInfo(driver);
            if (Double.valueOf($(By.id("SceneWindowLeft")).innerHtml()).intValue() != left) {
                horizontalWindowMovement(horizontal);
            }
        }
        if (left + sizeHor / 3 > horizontal) {
            sendKey(Keys.LEFT);
            updateCanvasInfo(driver);
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
            updateCanvasInfo(driver);
            if (Double.valueOf($(By.id("SceneWindowTop")).innerHtml()).intValue() != top) {
                verticalWindowMovement(vertical);
            }
        }
        if (top + sizeVer / 3 > vertical) {
            sendKey(Keys.UP);
            updateCanvasInfo(driver);
            if (Double.valueOf($(By.id("SceneWindowTop")).innerHtml()).intValue() != top) {
                verticalWindowMovement(vertical);
            }
        }
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
        if (elements.stream().anyMatch(x -> {
            try {
                return Math.abs(current - x.getCoordinateOnScene().getXAbsolute())
                        < Math.abs(2 * step);
            } catch (ElementNotOnTheSceneException e) {
                e.printStackTrace();
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
    
        new Actions(driver).release().clickAndHold(block.getInnerSeleniumElement()).moveByOffset(
                dist.getXAbsolute() - currentPosition.getXAbsolute(),
                dist.getYAbsolute() - currentPosition.getYAbsolute()).release().perform();
        
        return  block.getCoordinateOnScene();
    }
    
    private void updateCanvasInfo(WebDriver driver) {
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
    
    private void sendKey(Keys key) {
        new Actions(driver).sendKeys(key).perform();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}