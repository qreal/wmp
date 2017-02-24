package com.qreal.wmp.uitesting.dia.scene.window;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MoveHelper {
    
    private static final Logger logger = LoggerFactory.getLogger(MoveHelper.class);
    
    private final Scene scene;
    
    private final WebDriver driver;
    
    private MoveHelper(Scene scene, WebDriver driver) {
        this.scene = scene;
        this.driver = driver;
    }
    
    /** Horizontal move. */
    public Actions callDragAndDropByX(int src, int dst, int step, Actions actions, Keys key, SelenideElement element) {
        final List<Block> elements = scene.getBlocks();
        return Math.abs(src - dst) > Math.abs(step) ?
                callDragAndDropByX(src + step, dst, step,
                        actions.sendKeys(key)
                                .clickAndHold(element)
                                .moveByOffset(jump(elements, 2 * step, src), 0), key, element
                ) : actions.sendKeys(key, key);
    }
    
    /** Vertical move. */
    public Actions callDragAndDropByY(int src, int dst, int step, Actions actions, Keys key, SelenideElement element) {
        final List<Block> elements = scene.getBlocks();
        return Math.abs(src - dst) > Math.abs(step) ?
                callDragAndDropByY(src + step, dst, step,
                        actions.sendKeys(key)
                                .clickAndHold(element)
                                .moveByOffset(0, jump(elements, 2 * step, src)), key, element
                ) : actions.sendKeys(key, key);
    }
    
    /** Movement to the exact point when element in the SceneWindow. */
    public Coordinate finalJump(Block block, Coordinate dist) throws ElementNotOnTheSceneException {
        Coordinate currentPosition = block.getCoordinateOnScene();
        
        new Actions(driver).release()
                .clickAndHold(block.getInnerSeleniumElement())
                .moveByOffset(
                        dist.getXAbsolute() - currentPosition.getXAbsolute(),
                        dist.getYAbsolute() - currentPosition.getYAbsolute()
                ).release().perform();
        
        return  block.getCoordinateOnScene();
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
    
    public static MoveHelper getMoveHelper(Scene scene, WebDriver webDriver) {
        return new MoveHelper(scene, webDriver);
    }
}
