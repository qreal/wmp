package com.qreal.wmp.uitesting.mousegestures;

import java.awt.*;

public class DrawingTable {
    
    private static final int STEP_LENGTH = 20;

    private final Point leftUpCorner;
    
    public DrawingTable(Point leftUpCorner) {
        this.leftUpCorner = leftUpCorner;
    }
    
    public Point getPoint(String cell) {
        System.out.println("Point " + leftUpCorner.x + "," + leftUpCorner.y);
        int cellOffsetX = (cell.charAt(0) - 'A') * STEP_LENGTH;
        int cellOffsetY = (cell.charAt(1) - '0') * STEP_LENGTH;
        Point result = new Point(leftUpCorner.x + cellOffsetX, leftUpCorner.y + cellOffsetY);
        System.out.println(cell + ": " + result.x + "," + result.y);
        return result;
    }
}
