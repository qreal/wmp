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
    
    private DrawingTable drawingTable;
    
    public void paint(List<String> gestures, Point point) {
        try {
            robot = new Robot();
            robot.setAutoWaitForIdle(true);
            robot.setAutoDelay(100);
            drawingTable = new DrawingTable(point);
            if (gestures.isEmpty()) {
                return;
            }
            currentCell = gestures.get(0);
            currentPoint = drawingTable.getPoint(currentCell);
            robot.mouseMove(currentPoint.x, currentPoint.y);
            System.out.println("CurrentPoint " + currentPoint.x + "," + currentPoint.y);
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            robot.mouseMove(currentPoint.x - 1, currentPoint.y - 1);
            for (int i = 1; i < gestures.size(); ++ i) {
                String next = gestures.get(i);
                if (areNeigbors(currentCell, next)) {
                    System.out.println("Draw to " + next);
                    drawToCell(next);
                } else {
                    System.out.println("Jump to " + next);
                    jumpToCell(next);
                }
                currentCell = next;
                System.out.println("CurrentPoint " + currentPoint.x + "," + currentPoint.y);
            }
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        } catch (AWTException e) {
            e.printStackTrace();
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
    
    @SuppressWarnings("SameParameterValue")
    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
