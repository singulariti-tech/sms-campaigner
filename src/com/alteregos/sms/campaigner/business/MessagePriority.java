package com.alteregos.sms.campaigner.business;

import org.smslib.MessagePriorities;

/**
 * Date: 22-Oct-2009
 * Time: 15:08:15
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public enum MessagePriority {
    LOW ("L", "Low", MessagePriorities.LOW),
    NORMAL ("N", "Normal", MessagePriorities.NORMAL),
    HIGH ("H", "High", MessagePriorities.HIGH);

    private String label;
    private String message;
    private MessagePriorities priority;

    MessagePriority (String label, String message, MessagePriorities priority) {
        this.label = label;
        this.message = message;
        this.priority = priority;
    }

    public String getMessage () {
        return this.message;
    }

    public MessagePriorities translate () {
        return this.priority;
    }

    public static MessagePriority getPriority (String label) {
        for (MessagePriority priority : values ()) {
            if (priority.toString ().equals (label)) {
                return priority;
            }
        }
        return MessagePriority.NORMAL;
    }

    public static MessagePriority getPriorityForMessage (String message) {
        for (MessagePriority priority : values ()) {
            if (priority.getMessage ().equals (message)) {
                return priority;
            }
        }
        return MessagePriority.NORMAL;
    }

    @Override
    public String toString () {
        return this.label;
    }
}
