package com.qreal.wmp.uitesting.services;

import com.qreal.wmp.uitesting.exceptions.WrongAuthException;

/** Used for authentication in current browser session. */
public interface Auther {
    
    /** Realizes authentication to the wmp.
     *
     * @param username login
     * @param password password
     * */
    void auth(final String username, final String password) throws WrongAuthException;
    
    /** Authentication with fixed login and password. */
    void auth() throws WrongAuthException;
}
