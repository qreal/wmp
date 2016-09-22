package com.qreal.wmp.db.user.exceptions;

/** ErrorConnection inform about error of connection between client and service.*/
public class ErrorConnection extends Exception {

    /** Name of client in which error occurred.*/
    private final String nameClient;

    public ErrorConnection(String nameClient, String message) {
        super(message);
        this.nameClient = nameClient;
    }

    public ErrorConnection(String nameClient, String message, Exception cause) {
        super(message, cause);
        this.nameClient = nameClient;
    }

    public String getNameClient() {
        return nameClient;
    }
}
