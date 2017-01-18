package com.qreal.wmp.uitesting.dia.model;

import com.codeborne.selenide.SelenideElement;

import java.util.Optional;

public class Coordinate {
    
    public static Optional<Coordinate> getCoordinateFromSeleniumObject(SelenideElement element) {
        final String position = element.attr("transform");
        final String[] pairStr = position.substring(position.indexOf('(') + 1, position.indexOf(')')).split(",");
        return Optional.of(new Coordinate(Integer.valueOf(pairStr[0]), Integer.valueOf(pairStr[1])));
    }
    
    private final int xAbsolute;
    
    private final int yAbsolute;
    
    public Coordinate(int xAbsolute, int yAbsolute) {
        this.xAbsolute = xAbsolute;
        this.yAbsolute = yAbsolute;
    }
    
    public int getXCell() {
        return xAbsolute / 25;
    }
    
    public int getYCell() {
        return yAbsolute / 25;
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
