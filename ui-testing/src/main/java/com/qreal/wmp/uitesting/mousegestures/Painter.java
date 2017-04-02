package com.qreal.wmp.uitesting.mousegestures;

import org.jetbrains.annotations.Contract;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;

/** Class for simulate painting. */
public class Painter {
    
    private static final Logger logger = LoggerFactory.getLogger(Painter.class);
    
    private Point currentPoint;
    
    private Robot robot;
    
    private DrawingTable drawingTable;
    
    /** Right mouse button down and draw gesture (by items from gestures parameter) at the point point. */
    public void paint(List<String> gestures, Point point) {
        try {
            robot = new Robot();
            robot.setAutoWaitForIdle(true);
            robot.setAutoDelay(100);
            drawingTable = new DrawingTable(point);
            if (gestures.isEmpty()) {
                return;
            }
            String currentCell = gestures.get(0);
            currentPoint = drawingTable.getPoint(currentCell);
            robot.mouseMove(currentPoint.x, currentPoint.y);
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            robot.mouseMove(currentPoint.x - 1, currentPoint.y - 1);
            for (int i = 1; i < gestures.size(); ++ i) {
                String next = gestures.get(i);
                if (areNeigbors(currentCell, next)) {
                    drawToCell(next);
                } else {
                    jumpToCell(next);
                }
                currentCell = next;
            }
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        } catch (AWTException e) {
            logger.error("Robot error: " + e.getMessage());
        }
    }
    
    private void drawToCell(String targetCell) {
        Point targetPoint = drawingTable.getPoint(targetCell);
        robot.mouseMove(targetPoint.x, targetPoint.y);
        currentPoint = MouseInfo.getPointerInfo().getLocation();
    }
    
    private void jumpToCell(String targetCell) {
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        drawToCell(targetCell);
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.mouseMove(currentPoint.x - 1, currentPoint.y - 1);
    }
    
    private boolean areNeigbors(String firstCell, String secondCell) {
        Point diffPoint = diffBetweenCells(firstCell, secondCell);
        return Math.abs(diffPoint.getX()) <= 1 && Math.abs(diffPoint.getY()) <= 1;
    }
    
    @Contract("_, _ -> !null")
    private Point diffBetweenCells(String firstCell, String secondCell) {
        return new Point(secondCell.charAt(0) - firstCell.charAt(0), secondCell.charAt(1) - firstCell.charAt(1));
    }
    
    private class DrawingTable {
        
        private final int stepLength;
        
        private final Point leftUpCorner;
        
        public DrawingTable(Point leftUpCorner) {
            this.leftUpCorner = leftUpCorner;
            int sizeHor = Double.valueOf($(By.id("SceneWindowHorSize")).innerHtml()).intValue();
            int sizeVer = Double.valueOf($(By.id("SceneWindowVerSize")).innerHtml()).intValue();
            int minSize = Math.min(sizeHor, sizeVer);
            stepLength = Math.max(20, minSize / 4 / 6);
        }
        
        public Point getPoint(String cell) {
            int cellOffsetX = (cell.charAt(0) - 'A') * stepLength;
            int cellOffsetY = (cell.charAt(1) - '0') * stepLength;
            return new Point(leftUpCorner.x + cellOffsetX, leftUpCorner.y + cellOffsetY);
        }
    }
    
}
