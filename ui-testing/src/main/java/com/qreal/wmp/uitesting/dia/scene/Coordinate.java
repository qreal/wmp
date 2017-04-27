package com.qreal.wmp.uitesting.dia.scene;

import com.codeborne.selenide.SelenideElement;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Describes element's position on the Scene.
 * Contains absolute coordinates of scene, which are written in 'transform' tag on the html representation.
 * Also contains cell's position (the Scene is represented by a mesh of cells)
 */
public class Coordinate {
    
    public static final String SELECTOR = "transform";
    
    public static final int POINT_IN_CELL = 25;
    
    private final int xAbsolute;
    
    private final int yAbsolute;
    
    /** Returns coordinate of object on scene. */
    @NotNull
    public static Optional<Coordinate> getCoordinateFromSeleniumObject(SelenideElement element) {
        final String position = element.attr(SELECTOR);
        final String[] pairStr = position.substring(position.indexOf('(') + 1, position.indexOf(')')).split(",");
        return Optional.of(
                new Coordinate(
                    Double.valueOf(pairStr[0]).intValue(),
                    Double.valueOf(pairStr[1]).intValue()
                )
        );
    }
    
    public Coordinate(int xAbsolute, int yAbsolute) {
        this.xAbsolute = xAbsolute;
        this.yAbsolute = yAbsolute;
    }
    
    public int getXCell() {
        return xAbsolute / POINT_IN_CELL;
    }
    
    public int getYCell() {
        return yAbsolute / POINT_IN_CELL;
    }
    
    public int getXAbsolute() {
        return xAbsolute;
    }
    
    public int getYAbsolute() {
        return yAbsolute;
    }
    
    public boolean equals(final Coordinate other) {
        return xAbsolute == other.getXAbsolute() && yAbsolute == other.getYAbsolute();
    }
    
    public String toString() {
        return "(" + getXAbsolute() + "," + getYAbsolute() + ")";
    }
}
