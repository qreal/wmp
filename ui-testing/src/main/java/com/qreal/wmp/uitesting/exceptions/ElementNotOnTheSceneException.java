package com.qreal.wmp.uitesting.exceptions;

public class ElementNotOnTheSceneException extends Exception {
    
    public ElementNotOnTheSceneException() {
        super("It is impossible to get Coordinates of element, which is not on the Scene");
    }
    
}
