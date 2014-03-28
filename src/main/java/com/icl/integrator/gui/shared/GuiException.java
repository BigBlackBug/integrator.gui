package com.icl.integrator.gui.shared;

/**
 * Created by e.shahmaev on 28.03.2014.
 */
public class GuiException extends RuntimeException {

    public GuiException() {
    }

    public GuiException(String message) {
        super(message);
    }

    public GuiException(Throwable cause) {
        super(cause);
    }

    public GuiException(String message, Throwable cause) {
        super(message, cause);
    }
}
