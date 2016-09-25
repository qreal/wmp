package com.qreal.wmp.db.user.exceptions;

/** NotFound exception says that object was not found in DB.*/
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