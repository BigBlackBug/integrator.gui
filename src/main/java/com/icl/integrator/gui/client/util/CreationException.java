package com.icl.integrator.gui.client.util;

/**
 * Created by e.shahmaev on 28.03.2014.
 */
public class CreationException extends RuntimeException {

    private final String failedSubjectDescription;

    public CreationException(String message, String failedSubjectDescription) {
        super(message);
        this.failedSubjectDescription = failedSubjectDescription;
    }

    public CreationException(String message, String failedSubjectDescription, Throwable cause) {
        super(message, cause);
        this.failedSubjectDescription = failedSubjectDescription;
    }

    public CreationException(Throwable cause, String failedSubjectDescription) {
        super(cause);
        this.failedSubjectDescription = failedSubjectDescription;
    }

    public CreationException() {
        this.failedSubjectDescription = "";
    }

    public String getFailedSubjectDescription() {
        return failedSubjectDescription;
    }
}
