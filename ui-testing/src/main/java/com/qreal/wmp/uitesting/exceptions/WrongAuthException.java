package com.qreal.wmp.uitesting.exceptions;

/** Throw if we cannot authorize. */
public class WrongAuthException extends Exception {

    public WrongAuthException(final String login, final String password) {
        super("Unable to authorize with login: " + login + " and password: " + password);
    }
}
