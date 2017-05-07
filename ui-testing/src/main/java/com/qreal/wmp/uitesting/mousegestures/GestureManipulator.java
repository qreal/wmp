package com.qreal.wmp.uitesting.mousegestures;

import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;

/** Provides interface for working with gestures. */
public interface GestureManipulator {

    /** Draw figure which name is in the parameter. */
    Block draw(String name);
    
    /** Draw line between two blocks. */
    Link drawLine(Block source, Block target);
}
