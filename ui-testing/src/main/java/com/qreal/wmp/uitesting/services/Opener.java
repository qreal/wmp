package com.qreal.wmp.uitesting.services;

/**
 * Used for open needed page from wmp in current browser session.
 * Allows you to access as an authorized user and not.
 */
public interface Opener {
    /**
     * Opens page from wmp with authentication.
     *
     * @param page must be one of the keys from pages.property.
     */
    void open(final String page);
    
    /**
     * Opens page from wmp without authentication.
     *
     * @param page must be one of the keys from pages.property.
     */
    void cleanOpen(final String page);
}
