package com.qreal.wmp.uitesting.dia.scene.window;

import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;

public interface SceneWindow {
    
    /**
     * Moves element to the requested position.
     *
     * @param element element to move
     * @param dist position to move
     * */
    void move(final Block element, final Coordinate dist) throws ElementNotOnTheSceneException;
    
    /**
     * Move the screen to requested position.
     *
     * @param coordinate coordinate to move
     */
    void focus(final Coordinate coordinate);
    
    void update();
}
