package com.icl.integrator.gui.client.util;

/**
 * Created by e.shahmaev on 28.03.2014.
 */
public class CreationException extends RuntimeException {

    public CreationException(String message) {
        super(message);
    }

    public CreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreationException(Throwable cause) {
        super(cause);
    }

    public CreationException() {
    }
}
