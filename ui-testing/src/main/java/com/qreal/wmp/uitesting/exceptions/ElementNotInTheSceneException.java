package com.qreal.wmp.uitesting.exceptions;

public class ElementNotInTheSceneException extends Exception {
	
	public ElementNotInTheSceneException() {
		super("It is impossible to get Coordinate of element, which is not on the Scene");
	}
	
}
