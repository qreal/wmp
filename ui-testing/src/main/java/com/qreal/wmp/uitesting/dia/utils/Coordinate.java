package com.qreal.wmp.uitesting.dia.utils;

import com.codeborne.selenide.SelenideElement;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Coordinate {
    
    private static final int POINT_IN_CELL = 25;
    
    /** Returns coordinate of object on scene. */
    @NotNull
    public static Optional<Coordinate> getCoordinateFromSeleniumObject(SelenideElement element) {
        final String position = element.attr("transform");
        final String[] pairStr = position.substring(position.indexOf('(') + 1, position.indexOf(')')).split(",");
        return Optional.of(
                new Coordinate(
                    Double.valueOf(pairStr[0]).intValue(),
                    Double.valueOf(pairStr[1]).intValue()
                )
        );
    }
    
    private final int xAbsolute;
    
    private final int yAbsolute;
    
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
}
