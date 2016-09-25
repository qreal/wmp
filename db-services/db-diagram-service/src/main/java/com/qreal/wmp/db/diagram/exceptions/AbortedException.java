package com.qreal.wmp.db.diagram.exceptions;

/** Aborted exception inform about inconsistency of DB or any other situation lead to abort of operation.*/
public class AbortedException extends Exception {

    /** Cause of operation abort. */
    private final String textCause;

    /** Name of class where operation met exceptional situation.*/
    private final String fullClassName;

    public AbortedException(String textCause, String message, String fullClassName) {
        super(message);
        this.textCause = textCause;
        this.fullClassName = fullClassName;
    }

    public AbortedException(String textCause, String message, String fullClassName, Exception cause) {
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
