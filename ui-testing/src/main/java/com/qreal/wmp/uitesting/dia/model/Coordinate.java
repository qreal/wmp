package com.qreal.wmp.uitesting.dia.model;


import com.codeborne.selenide.SelenideElement;

import java.util.Optional;

public class Coordinate {
	
	public static Optional<Coordinate> getCoordinateFromSeleniumObject(SelenideElement element) {
		final String position = element.attr("transform");
		final String[] pairStr = position.substring(position.indexOf('(') + 1, position.indexOf(')')).split(",");
		return Optional.of(new Coordinate(Integer.valueOf(pairStr[0]) / 25, Integer.valueOf(pairStr[1]) / 25));
	}
	
	private final int xCell;
	private final int yCell;
	
	public Coordinate(int xCell, int yCell) {
		this.xCell = xCell;
		this.yCell = yCell;
	}
	
	public int getXCell() {
		return xCell;
	}
	
	public int getYCell() {
		return yCell;
	}
}
