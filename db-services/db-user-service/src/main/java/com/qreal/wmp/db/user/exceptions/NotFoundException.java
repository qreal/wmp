package com.qreal.wmp.db.user.exceptions;

/** An exception saying that an object was not found in the DB. */
public class NotFoundException extends Exception {
    private final String id;

    public NotFoundException(String id, String message) {
        super(message);
        this.id = id;
    }

    public String getId() {
        return  id;
    }
}