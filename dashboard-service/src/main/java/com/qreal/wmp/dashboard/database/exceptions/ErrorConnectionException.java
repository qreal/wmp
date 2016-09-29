package com.qreal.wmp.dashboard.database.exceptions;

/** ErrorConnection informs about an error in connection between client and service. */
public class ErrorConnectionException extends Exception {

    /** Name of a client which produced an error. */
    private final String clientName;

    public ErrorConnectionException(String clientName, String message) {
        super(message);
        this.clientName = clientName;
    }

    public ErrorConnectionException(String clientName, String message, Exception cause) {
        super(message, cause);
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }
}
