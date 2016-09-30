package com.qreal.wmp.editor.database.exceptions;

/** ErrorConnection informs about an error in connection between client and service. */
public class ErrorConnectionException extends Exception {

    /** Name of a client which produced an error. */
    private final String nameClient;

    public ErrorConnectionException(String nameClient, String message) {
        super(message);
        this.nameClient = nameClient;
    }

    public ErrorConnectionException(String nameClient, String message, Exception cause) {
        super(message, cause);
        this.nameClient = nameClient;
    }

    public String getNameClient() {
        return nameClient;
    }
}
