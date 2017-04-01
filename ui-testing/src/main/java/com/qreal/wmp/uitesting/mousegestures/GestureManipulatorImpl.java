package com.qreal.wmp.uitesting.mousegestures;

import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import com.qreal.wmp.uitesting.pages.EditorPageFacade;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.awt.event.InputEvent;

public class GestureManipulatorImpl implements GestureManipulator {
    
    private final EditorPageFacade pageFacade;
    
    private GestureManipulatorImpl(EditorPageFacade pageFacade) {
        this.pageFacade = pageFacade;
    }
    
    @Override
    public Block draw(Coordinate point, String name) {
        return null;
    }
    
    @Override
    public Link drawLine(Block source, Block target) {
        Point coordinate = source.getInnerSeleniumElement().getLocation();
        Point coordinate2 = target.getInnerSeleniumElement().getLocation();
        Robot robot;
        try {
            robot = new Robot();
            robot.mouseMove(coordinate.getX() + RobotCalibration.getLastKnownPointX(),
                    coordinate.getY() + RobotCalibration.getLastKnownPointY());
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            robot.mouseMove(coordinate2.getX() + RobotCalibration.getLastKnownPointX(),
                    coordinate2.getY() + RobotCalibration.getLastKnownPointY());
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageFacade.addDrawnLink();
    }
    
    @Contract("_ -> !null")
    public static GestureManipulator getGestureManipulator(EditorPageFacade pageFacade) {
        return new GestureManipulatorImpl(pageFacade);
    }
}
