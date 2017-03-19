package com.qreal.wmp.uitesting.dia.pallete;

import org.openqa.selenium.NoSuchElementException;

/**
 * Describes Pallete.
 * For any manipulating with it.
 */
public interface Pallete {
    
    /**
     * Chooses an element from Pallete.
     *
     * @param elementName name of block
     * @return block
     */
    PalleteElement getElement(String elementName) throws NoSuchElementException;
}
