package com.qreal.wmp.uitesting.dia.scene.window;

import org.jetbrains.annotations.Contract;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;

public class FocusHelper {
    
    private static final Logger logger = LoggerFactory.getLogger(FocusHelper.class);
    
    private final PageInfoUpdator updator;
    
    private FocusHelper(PageInfoUpdator updator) {
        this.updator = updator;
    }
    
    /** Horizontal movement. */
    public void horizontalWindowMovement(int horizontal) {
        int sizeHor = Double.valueOf($(By.id("SceneWindowHorSize")).innerHtml()).intValue();
        int left = Double.valueOf($(By.id("SceneWindowLeft")).innerHtml()).intValue();
        logger.info("focus horizontal " + left + " " + sizeHor + " " + horizontal);
        
        if (left + sizeHor * 2 / 3 < horizontal) {
            updator.sendKey(Keys.RIGHT);
            updator.updateCanvasInfo();
            if (Double.valueOf($(By.id("SceneWindowLeft")).innerHtml()).intValue() != left) {
                horizontalWindowMovement(horizontal);
            }
        }
        if (left + sizeHor / 3 > horizontal) {
            updator.sendKey(Keys.LEFT);
            updator.updateCanvasInfo();
            if (Double.valueOf($(By.id("SceneWindowLeft")).innerHtml()).intValue() != left) {
                horizontalWindowMovement(horizontal);
            }
        }
    }
    
    /** Vertical movement. */
    public void verticalWindowMovement(int vertical) {
        int sizeVer = Double.valueOf($(By.id("SceneWindowVerSize")).innerHtml()).intValue();
        int top = Double.valueOf($(By.id("SceneWindowTop")).innerHtml()).intValue();
        logger.info("focus vertical " + top + " " + sizeVer + " " + vertical);
        
        if (top + sizeVer * 2 / 3 < vertical) {
            updator.sendKey(Keys.DOWN);
            updator.updateCanvasInfo();
            if (Double.valueOf($(By.id("SceneWindowTop")).innerHtml()).intValue() != top) {
                verticalWindowMovement(vertical);
            }
        }
        if (top + sizeVer / 3 > vertical) {
            updator.sendKey(Keys.UP);
            updator.updateCanvasInfo();
            if (Double.valueOf($(By.id("SceneWindowTop")).innerHtml()).intValue() != top) {
                verticalWindowMovement(vertical);
            }
        }
    }
    
    @Contract("_ -> !null")
    public static FocusHelper getFocusHelper(PageInfoUpdator updator) {
        return new FocusHelper(updator);
    }
}
