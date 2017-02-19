package com.qreal.wmp.uitesting.dia.scene.elements;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.utils.Coordinate;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;

/** Describes any element on the Scene. */
public interface SceneElement {
	SelenideElement getInnerSeleniumElement();
	
	Coordinate getCoordinateOnScene() throws ElementNotOnTheSceneException;
	
	String getType();
}
