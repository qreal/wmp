package com.qreal.wmp.dashboard.database.exceptions;

/** ErrorConnection inform about error of connection between client and service.*/
public class ErrorConnectionException extends Exception {

    /** Name of client in which error occurred.*/
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
