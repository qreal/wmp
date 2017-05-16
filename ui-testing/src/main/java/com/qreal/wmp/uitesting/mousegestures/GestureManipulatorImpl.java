package com.qreal.wmp.uitesting.mousegestures;

import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.dia.scene.SceneProxy;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import com.qreal.wmp.uitesting.dia.scene.elements.SceneElement;
import com.qreal.wmp.uitesting.pages.editor.EditorPageFacade;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;

/** {@inheritDoc} */
public class GestureManipulatorImpl implements GestureManipulator {
    
    private static final Logger logger = LoggerFactory.getLogger(GestureManipulator.class);
    
    private final EditorPageFacade pageFacade;
    
    private final Painter painter;
    
    private final Map<String, Gesture> gestureMap = new HashMap<>();
    
    private GestureManipulatorImpl(EditorPageFacade pageFacade) {
        this.pageFacade = pageFacade;
        painter = new Painter();
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("gestures.json")) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            Gesture[] gestures = mapper.readValue(is, Gesture[].class);
            gestureMap.putAll(Arrays.stream(gestures).collect(
                    Collectors.toMap(Gesture::getName, gesture -> gesture)));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    
    @Override
    public Block draw(String name) {
        pageFacade.update();
        int sizeHor = Double.valueOf($(By.id("SceneWindowHorSize")).innerHtml()).intValue();
        int sizeVer = Double.valueOf($(By.id("SceneWindowVerSize")).innerHtml()).intValue();
        Point screenCoordinate = getScreenPosition();
        painter.paint(
                gestureMap.get(name).getKey(),
                new Point(screenCoordinate.x + sizeVer / 3, screenCoordinate.y + sizeHor / 3));
        return pageFacade.addDrawnBlock(name);
    }
    
    @Override
    public Link drawLine(Block source, Block target) {
        org.openqa.selenium.Point coordinate = source.getInnerSeleniumElement().getLocation();
        org.openqa.selenium.Point coordinate2 = target.getInnerSeleniumElement().getLocation();
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
            logger.error("Robot error:" + e.getMessage());
        }
        return pageFacade.addDrawnLink();
    }
    
    @Override
    public Optional<SceneElement> drawByOffsets(Coordinate start, List<Integer> offsetsX, List<Integer> offsetsY) {
        if (offsetsX.size() != offsetsY.size()) {
            throw new IllegalArgumentException("Lists of offset must have the same size");
        }
        Robot robot;
        try {
            robot = new Robot();
            robot.setAutoWaitForIdle(true);
            robot.setAutoDelay(300);
            Point screenPosition = getScreenPosition();
            int currentX = screenPosition.x + start.getXAbsolute();
            int currentY = screenPosition.y + start.getYAbsolute();
            robot.mouseMove(currentX, currentY);
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            robot.mouseMove(currentX - 1, currentY - 1);
            for (int i = 0; i < offsetsX.size(); ++i) {
                currentX += offsetsX.get(i);
                currentY += offsetsY.get(i);
                robot.mouseMove(currentX, currentY);
            }
            robot.mouseMove(currentX - 1, currentY - 1);
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
            robot.delay(100);
        } catch (Exception e) {
            logger.error("Robot error:" + e.getMessage());
        }
        try {
            return Optional.of(pageFacade.addDrawnBlock());
        } catch (NoSuchElementException | NotFoundException ignored) {
            try {
                return Optional.of(pageFacade.addDrawnLink());
            } catch (NoSuchElementException | NotFoundException e) {
                return Optional.empty();
            }
        }
    }
    
    @Contract("_ -> !null")
    public static GestureManipulator getGestureManipulator(EditorPageFacade pageFacade) {
        return new GestureManipulatorImpl(pageFacade);
    }
    
    private Point getScreenPosition() {
        SelenideElement element = $(By.cssSelector(SceneProxy.SELECTOR));
        return new Point(
                element.getLocation().x + RobotCalibration.getLastKnownPointX(),
                element.getLocation().y + RobotCalibration.getLastKnownPointY());
    }
}
