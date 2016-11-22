package com.qreal.wmp.uitesting.exceptions;

public class WrongAuthException extends Exception {

    public WrongAuthException(String login, String password) {
        super("Unable to authorize with login: " + login + " and password: " + password);
    }
}
