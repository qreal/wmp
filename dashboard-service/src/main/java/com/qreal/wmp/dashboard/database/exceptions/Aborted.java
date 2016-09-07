package com.qreal.wmp.dashboard.database.exceptions;

public class Aborted extends Exception {

    private final String textCause;

    private final String fullClassName;

    public Aborted(String textCause, String message, String fullClassName) {
        super(message);
        this.textCause = textCause;
        this.fullClassName = fullClassName;
    }

    public Aborted(String textCause, String message, String fullClassName, Exception cause) {
        super(message, cause);
        this.textCause = textCause;
        this.fullClassName = fullClassName;

    }

    public String getTextCause() {
        return  textCause;
    }

    public String getFullClassName() {
        return fullClassName;
    }
}
