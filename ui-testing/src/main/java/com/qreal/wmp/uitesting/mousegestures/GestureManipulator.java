package com.qreal.wmp.uitesting.mousegestures;

import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import com.qreal.wmp.uitesting.dia.scene.elements.SceneElement;

import java.util.List;
import java.util.Optional;

/** Provides interface for working with gestures. */
public interface GestureManipulator {

    /** Draw figure which name is in the parameter. */
    Block draw(String name);
    
    /** Draw line between two blocks. */
    Link drawLine(Block source, Block target);
    
    /** Draw user's figure. */
    Optional<SceneElement> drawByOffsets(Coordinate start, List<Integer> offsetsX, List<Integer> offsetsY);
}
