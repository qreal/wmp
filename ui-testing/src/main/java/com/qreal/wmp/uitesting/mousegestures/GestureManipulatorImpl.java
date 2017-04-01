package com.qreal.wmp.uitesting.mousegestures;

import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.dia.scene.SceneProxy;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import com.qreal.wmp.uitesting.dia.scene.window.SceneWindowImpl;
import com.qreal.wmp.uitesting.pages.EditorPageFacade;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;

public class GestureManipulatorImpl implements GestureManipulator {
    
    private final EditorPageFacade pageFacade;
    
    private final Painter painter;
    
    private final Map<String, Gesture> gestureMap = new HashMap<>();
    
    private final WebDriver driver;
    
    private GestureManipulatorImpl(WebDriver driver, EditorPageFacade pageFacade) {
        this.driver = driver;
        this.pageFacade = pageFacade;
        painter = new Painter();
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("gestures.json")) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            Gesture[] gestures = mapper.readValue(is, Gesture[].class);
            gestureMap.putAll(Arrays.stream(gestures).collect(
                    Collectors.toMap(Gesture::getName, gesture -> gesture)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Block draw(String name) {
        SelenideElement element = $(By.cssSelector(SceneProxy.SELECTOR));
        SceneWindowImpl.updateCanvasInfo(driver);
        int sizeHor = Double.valueOf($(By.id("SceneWindowHorSize")).innerHtml()).intValue();
        int sizeVer = Double.valueOf($(By.id("SceneWindowVerSize")).innerHtml()).intValue();
        Point screenCoordinate = new Point(
                element.getLocation().x + RobotCalibration.getLastKnownPointX(),
                element.getLocation().y + RobotCalibration.getLastKnownPointY());
        painter.paint(
                gestureMap.get(name).getKey(),
                new java.awt.Point(screenCoordinate.x +  sizeVer / 2, screenCoordinate.y + sizeHor / 2)
        );
        return pageFacade.addDrawnBlock();
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
    public static GestureManipulator getGestureManipulator(WebDriver driver, EditorPageFacade pageFacade) {
        return new GestureManipulatorImpl(driver, pageFacade);
    }
}
