package com.alteregos.sms.campaigner.conf;

/**
 *
 * @author John Emmanuel
 */
public class EntryNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public EntryNotFoundException(Throwable cause) {
        super(cause);
    }

    public EntryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntryNotFoundException(String message) {
        super(message);
    }

    public EntryNotFoundException() {
    }
}
