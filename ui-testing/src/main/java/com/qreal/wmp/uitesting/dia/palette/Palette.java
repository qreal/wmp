package com.qreal.wmp.uitesting.dia.palette;

import org.openqa.selenium.NoSuchElementException;

/**
 * Describes Palette.
 * For any manipulating with it.
 */
public interface Palette {
    
    /**
     * Chooses an element from Palette.
     *
     * @param elementName name of block
     * @return block
     */
    PaletteElement getElement(String elementName) throws NoSuchElementException;
}
