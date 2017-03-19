package com.qreal.wmp.uitesting.dia.scene.window;

import com.google.common.base.Predicate;
import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.SceneElement;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static com.codeborne.selenide.Selenide.$;

/**
 * Describes part of the scene, which is shown on browser.
 */
public class SceneWindowImpl implements SceneWindow {
    
    private static final Logger logger = LoggerFactory.getLogger(SceneWindowImpl.class);
    
    private final WebDriver driver;
    
    /** Constructor takes links to current scene and current driver. */
    private SceneWindowImpl(final WebDriver driver) {
        this.driver = driver;
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
        int sizeHor = Double.valueOf($(By.id("SceneWindowHorSize")).innerHtml()).intValue();
        int sizeVer = Double.valueOf($(By.id("SceneWindowVerSize")).innerHtml()).intValue();
    
        Actions actions = new Actions(driver);
        actions.clickAndHold(element.getInnerSeleniumElement());
        if (isDistanceLessThenBarrier(
                dist,
                element.getCoordinateOnScene(),
                new OffsetObject(sizeHor / 2, sizeVer / 2))) {
            
            jump(actions, element, dist);
        } else {
            java.util.function.Predicate<Coordinate> condX = x ->
                    isDistanceLessThenBarrier(x.getXAbsolute(), dist.getXAbsolute(), sizeHor / 3);
            
            if (src.getXAbsolute() < dist.getXAbsolute()) {
                innerMove(actions, element, new OffsetObject(sizeHor / 4, 0), condX);
            } else {
                innerMove(actions, element, new OffsetObject(-sizeHor / 4, 0), condX);
            }
        }
        if (Math.abs(dist.getXAbsolute() - element.getCoordinateOnScene().getXAbsolute()) < sizeHor / 2
                && Math.abs(dist.getYAbsolute() - element.getCoordinateOnScene().getYAbsolute()) < sizeVer / 2) {
            jump(actions, element, dist);
        } else {
            java.util.function.Predicate<Coordinate> condY = x ->
                    isDistanceLessThenBarrier(x.getYAbsolute(), dist.getYAbsolute(), sizeVer / 3);
            if (src.getYAbsolute() < dist.getYAbsolute()) {
                innerMove(actions, element, new OffsetObject(0, sizeVer / 4), condY);
            } else {
                innerMove(actions, element, new OffsetObject(0, -sizeVer / 4), condY);
            }
        }
        
        jump(actions, element, dist);

        (new WebDriverWait(driver, 20))
                .until((Predicate<WebDriver>) webDriver -> {
                    try {
                        return element.getCoordinateOnScene().equals(dist);
                    } catch (ElementNotOnTheSceneException e) {
                        logger.error("It is impossible to move element, which is not on the Scene");
                    }
                    return false;
                });
    }

    @Override
    public void focus(final Coordinate coordinate) {
        updateCanvasInfo();
        int sizeHor = Double.valueOf($(By.id("SceneWindowHorSize")).innerHtml()).intValue();
        int sizeVer = Double.valueOf($(By.id("SceneWindowVerSize")).innerHtml()).intValue();
    
        logger.info("Focus to " + coordinate.getXAbsolute() + " " + coordinate.getYAbsolute());
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript("var canvas = " +
                    "document.getElementsByClassName(\"scene-wrapper\")[0]; " +
                    "var BB=canvas.getBoundingClientRect();" +
                    "canvas.scrollLeft = " + Math.max(0, (coordinate.getXAbsolute() - sizeHor / 2)) + "; " +
                    "canvas.scrollTop = " + Math.max(0, (coordinate.getYAbsolute() - sizeVer / 2)) + ";"
            );
        }
        updateCanvasInfo();
    }
    
    @Contract("_ -> !null")
    public static SceneWindow getSceneWindow(WebDriver webDriver) {
        return new SceneWindowImpl(webDriver);
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
    
    private void innerMove(Actions actions, SceneElement element,
                           OffsetObject offset, java.util.function.Predicate<Coordinate> cond) {
        
        (new WebDriverWait(driver, 40))
                .until((Predicate<WebDriver>) webDriver -> {
                    try {
                        Coordinate current = element.getCoordinateOnScene();
                        actions.moveToElement(element.getInnerSeleniumElement()).perform();
                        actions.moveByOffset(offset.offsetX, offset.offsetY).perform();
                       
                        focus(element.getCoordinateOnScene());
                        if (element.getCoordinateOnScene().equals(current)) {
                            Random random = new Random();
                            int signX = (int) Math.signum(offset.offsetX);
                            int signY = (int) Math.signum(offset.offsetY);
                            if (offset.offsetX != 0) {
                                offset.offsetX = signX * random.nextInt(Math.abs(offset.offsetX));
                            }
                            if (offset.offsetY != 0) {
                                offset.offsetY = signY * random.nextInt(Math.abs(offset.offsetY));
                            }
                        }
                        return cond.test(element.getCoordinateOnScene());
                    } catch (ElementNotOnTheSceneException e) {
                        logger.error("It is impossible to move element, which is not on the Scene");
                    }
                    return false;
                });
    }
    
    private void jump(Actions actions, SceneElement element, Coordinate dist) throws ElementNotOnTheSceneException {
        if (!element.getCoordinateOnScene().equals(dist)) {
            focus(element.getCoordinateOnScene());
            actions.moveToElement(element.getInnerSeleniumElement()).moveByOffset(
                    dist.getXAbsolute() - element.getCoordinateOnScene().getXAbsolute(),
                    dist.getYAbsolute() - element.getCoordinateOnScene().getYAbsolute()
            ).release().build().perform();
        }
    }
    
    private class OffsetObject {
        private int offsetX;
        
        private int offsetY;
    
        OffsetObject(int offsetX, int offsetY) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }
    }
    
    private boolean isDistanceLessThenBarrier(double fst, double snd, double barrier) {
        return Math.abs(fst - snd) <= barrier;
    }
    
    private boolean isDistanceLessThenBarrier(Coordinate fst, Coordinate snd, OffsetObject barrier) {
        return isDistanceLessThenBarrier(fst.getXAbsolute(), snd.getXAbsolute(), barrier.offsetX) &&
                isDistanceLessThenBarrier(fst.getYAbsolute(), snd.getYAbsolute(), barrier.offsetY);
    }
}