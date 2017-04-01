package com.qreal.wmp.uitesting.mousegestures;

import org.jetbrains.annotations.Contract;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.List;

public class Painter {
    
    private static final int STEP_LENGTH = 20;
    
    private String currentCell;
    
    private Point currentPoint;
    
    private Robot robot;
    
    public void paint(List<String> gestures, Point point) {
        try {
            robot = new Robot();
            robot.mouseMove(point.x, point.y);
            sleep(100);
            currentPoint = MouseInfo.getPointerInfo().getLocation();
            if (gestures.isEmpty()) {
                return;
            }
            robot.mouseMove(currentPoint.x + STEP_LENGTH / 2, currentPoint.y + STEP_LENGTH / 2);
            sleep(100);
            currentCell = gestures.get(0);
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            sleep(100);
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
            e.printStackTrace();
        }
    }
    
    private void drawToCell(String targetCell) {
        Point diffBetweenCells = diffBetweenCells(currentCell, targetCell);
        robot.mouseMove(
                currentPoint.x + diffBetweenCells.x * STEP_LENGTH,
                currentPoint.y + diffBetweenCells.y * STEP_LENGTH
        );
        currentPoint = MouseInfo.getPointerInfo().getLocation();
        sleep(100);
    }
    
    private void jumpToCell(String targetCell) {
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        drawToCell(targetCell);
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
    }
    
    private boolean areNeigbors(String firstCell, String secondCell) {
        Point diffPoint = diffBetweenCells(firstCell, secondCell);
        return Math.abs(diffPoint.getX()) <= 1 && Math.abs(diffPoint.getY()) <= 1;
    }
    
    @Contract("_, _ -> !null")
    private Point diffBetweenCells(String firstCell, String secondCell) {
        return new Point(firstCell.charAt(0) - secondCell.charAt(0), firstCell.charAt(1) - secondCell.charAt(1));
    }
    
    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
