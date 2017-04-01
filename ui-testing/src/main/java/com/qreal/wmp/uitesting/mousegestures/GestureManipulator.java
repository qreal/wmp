package com.qreal.wmp.uitesting.mousegestures;

import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;

public interface GestureManipulator {

    Block draw(String name);
    
    Link drawLine(Block source, Block target);
}
