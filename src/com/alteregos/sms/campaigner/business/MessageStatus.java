package com.alteregos.sms.campaigner.business;

/**
 * Date: 22-Oct-2009
 * Time: 15:30:51
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public enum MessageStatus {
    UNSENT ("U", "Not Sent"),
    QUEUED ("Q", "Queued"),
    SUCCESSFULLY_SENT ("S", "Successfully Sent"),
    FAILED ("F", "Failed");

    private String label;
    private String message;

    MessageStatus (String label, String message) {
        this.label = label;
        this.message = message;
    }

    public String getMessage () {
        return this.message;
    }

    public static MessageStatus getStatus (String label) {
        for (MessageStatus status : values ()) {
            if (status.toString ().equals (label)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString () {
        return this.label;
    }
}
