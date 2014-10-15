package com.connalle.android.application.notepad.exceptions;

/**
 * Created by vicky on 10/4/2014.
 */
public class DataBaseException extends RuntimeException {
    public DataBaseException() {
        super();
    }

    public DataBaseException(final String message) {
        super(message);
    }

    public DataBaseException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public DataBaseException(final Throwable throwable) {
        super(throwable);
    }
}
