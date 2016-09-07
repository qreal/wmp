package com.qreal.wmp.dashboard.database.exceptions;

public class ErrorConnection extends Exception {

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
