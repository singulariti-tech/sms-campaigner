package com.alteregos.sms.campaigner.exceptions;

/**
 *
 * @author John Emmanuel
 */
public class CommPortTestException extends Exception {

    private static final long serialVersionUID = 1L;

    public CommPortTestException(Throwable cause) {
        super(cause);
    }

    public CommPortTestException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommPortTestException(String message) {
        super(message);
    }

    public CommPortTestException() {
    }
}
