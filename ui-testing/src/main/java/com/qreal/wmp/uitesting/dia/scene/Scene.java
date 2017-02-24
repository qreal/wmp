package com.qreal.wmp.uitesting.dia.scene;

import com.qreal.wmp.uitesting.dia.pallete.PalleteElement;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import com.qreal.wmp.uitesting.dia.scene.elements.SceneElement;

import java.util.List;

public interface Scene {
    /** Return selector of Scene in html-page. */
    String getSelector();
    
    /** Drag element from scene or pallete and put it on the center of scene. */
    Block dragAndDrop(PalleteElement palleteElement);
    
    /** Drag element from scene or pallete and put it in cell of the scene. */
    Block dragAndDrop(PalleteElement element, int cell_x, int cell_y);
    
    /** Move element from scene to the cell. */
    void moveToCell(Block block, int cell_x, int cell_y);
    
    /** Focus the element. */
    void focus(SceneElement element);
    
    /** Check if element exist on the scene. */
    boolean exist(SceneElement element);
    
    /** Remove block from the scene. */
    void remove(SceneElement element);
    
    /** Add link between two elements. */
    Link addLink(Block source, Block target);
    
    /** Return all blocks. */
    List<Block> getBlocks();
    
    /** Remove all elements from the scene. */
    void clean();
}
