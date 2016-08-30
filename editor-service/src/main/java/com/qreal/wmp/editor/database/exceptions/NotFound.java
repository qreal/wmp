package com.qreal.wmp.editor.database.exceptions;

public class NotFound extends Exception {
    String id;

    public NotFound(String id, String message) {
        super(message);
        this.id = id;
    }

    public String getId() {
        return  id;
    }
}
