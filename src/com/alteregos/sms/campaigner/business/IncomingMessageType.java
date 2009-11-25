package com.alteregos.sms.campaigner.business;

/**
 * Date: 22-Oct-2009
 * Time: 14:51:09
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public enum IncomingMessageType {
    USER_MESSAGE ("I"),
    STATUS_REPORT ("S");

    private String label;

    IncomingMessageType (String label) {
        this.label = label;
    }

    public static IncomingMessageType getType (String label) {
        for (IncomingMessageType t : values ()) {
            if (t.toString ().equals (label)) {
                return t;
            }
        }
        return USER_MESSAGE;
    }

    @Override
    public String toString () {
        return this.label;
    }
}
