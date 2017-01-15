package com.qreal.wmp.uitesting.dia.model;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.exceptions.ElementNotInTheSceneException;

public class Block {

	private final String name;
	private final SelenideElement innerSeleniumObject;
	private final String type;
	
	private final Coordinate coordinateOnScene;
	
	public Block(SelenideElement innerSeleniumObject, String name, String type) throws ElementNotInTheSceneException {
		this.name = name;
		this.innerSeleniumObject = innerSeleniumObject;
		coordinateOnScene = Coordinate.getCoordinateFromSeleniumObject(innerSeleniumObject)
				.orElseThrow(ElementNotInTheSceneException::new);
		this.type = type;
	}
	
	public SelenideElement getInnerSeleniumObject() {
		return innerSeleniumObject;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public Coordinate getCoordinateOnScene() {
		return coordinateOnScene;
	}
}
