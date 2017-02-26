package com.qreal.wmp.uitesting.dia.scene.window;

import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;

/**
 * Describes part of the scene, which is shown on browser.
 */
public class SceneWindowImpl implements SceneWindow, PageInfoUpdator {
    
    private static final Logger logger = LoggerFactory.getLogger(SceneWindowImpl.class);
    
    private final WebDriver driver;
    
    private final String selector;
    
    private final FocusHelper focusHelper;
    
    private final MoveHelper moveHelper;
    
    private int stepVert = 40;

    private int stepHor = 40;
    
    /** Constructor takes links to current scene and current driver. */
    private SceneWindowImpl(final Scene scene, final WebDriver driver, String selector) {
        this.driver = driver;
        this.selector = selector;
        focusHelper = FocusHelper.getFocusHelper(this);
        moveHelper = MoveHelper.getMoveHelper(scene, driver);
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
            moveHelper.callDragAndDropByX(
                    src.getXAbsolute(), dist.getXAbsolute(), stepHor,
                    new Actions(driver), Keys.ARROW_RIGHT, element.getInnerSeleniumElement()).perform();
        } else {
            moveHelper.callDragAndDropByX(src.getXAbsolute(), dist.getXAbsolute(), -stepHor,
                    new Actions(driver), Keys.ARROW_LEFT, element.getInnerSeleniumElement()).perform();
        }

        if (src.getYAbsolute() < dist.getYAbsolute()) {
            moveHelper.callDragAndDropByY(src.getYAbsolute(), dist.getYAbsolute(), stepVert,
                    new Actions(driver), Keys.ARROW_DOWN, element.getInnerSeleniumElement()).perform();
        } else {
            moveHelper.callDragAndDropByY(src.getYAbsolute(), dist.getYAbsolute(), -stepVert,
                    new Actions(driver), Keys.ARROW_UP, element.getInnerSeleniumElement()).perform();
        }

        if (!moveHelper.finalJump(element, dist).equals(dist)) {
            move(element, dist);
        }
    }

    @Override
    public void focus(final Coordinate coordinate) {
        logger.info("Focus to " + coordinate.getXAbsolute() + " " + coordinate.getYAbsolute());
        focusHelper.horizontalWindowMovement(coordinate.getXAbsolute());
        focusHelper.verticalWindowMovement(coordinate.getYAbsolute());
    }
    
    @Contract("_, _, _ -> !null")
    public static SceneWindow getSceneWindow(Scene scene, WebDriver webDriver, String selector) {
        return new SceneWindowImpl(scene, webDriver, selector);
    }
    
    @Override
    public void updateCanvasInfo() {
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
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
    
    // todo: make it waits real time until action is completed
    @Override
    public void sendKey(Keys key) {
        $(selector).click();
        new Actions(driver).sendKeys(key).build().perform();
        try {
            // wait hard coded time until action is completed
            Thread.sleep(100);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
    
    @Override
    public void updateSteps() {
        updateCanvasInfo();
        $(selector).click();
        focus(new Coordinate(0, 0));
        updateCanvasInfo();
        sendKey(Keys.DOWN);
        sendKey(Keys.RIGHT);
        updateCanvasInfo();
        stepHor = Double.valueOf($(By.id("SceneWindowLeft")).innerHtml()).intValue();
        stepVert = Double.valueOf($(By.id("SceneWindowTop")).innerHtml()).intValue();
        logger.info("stepHor = " + stepHor + "; stepVert = " + stepVert);
    }
}