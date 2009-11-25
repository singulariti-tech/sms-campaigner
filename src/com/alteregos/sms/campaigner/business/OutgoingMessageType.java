package com.alteregos.sms.campaigner.business;

/**
 * Date: 22-Oct-2009
 * Time: 15:34:12
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public enum OutgoingMessageType {
    AUTO_REPLY ("A", "Auto Reply"),
    BULK ("B", "Bulk");

    private String label;
    private String message;

    OutgoingMessageType (String label, String message) {
        this.label = label;
        this.message = message;
    }

    public String getMessage () {
        return this.message;
    }

    public static OutgoingMessageType getType (String code) {
        for (OutgoingMessageType smsType : values ()) {
            if (smsType.toString ().equals (code)) {
                return smsType;
            }
        }
        return null;
    }

    @Override
    public String toString () {
        return this.label;
    }
}
