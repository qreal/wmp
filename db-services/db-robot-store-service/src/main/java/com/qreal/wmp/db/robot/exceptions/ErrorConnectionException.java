package com.qreal.wmp.db.robot.exceptions;

/** ErrorConnection informs about an error in connection between client and service. */
public class ErrorConnectionException extends Exception {

    /** Name of a client which produced an error. */
    private final String clientName;

    public ErrorConnectionException(String nameClient, String message) {
        super(message);
        this.clientName = nameClient;
    }

    public ErrorConnectionException(String nameClient, String message, Exception cause) {
        super(message, cause);
        this.clientName = nameClient;
    }

    public String getClientName() {
        return clientName;
    }
}
