package com.alteregos.sms.campaigner.data.dto;

/**
 * Date: Oct 24, 2009
 * Time: 12:50:30 PM
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class IncomingMessagePk {

    private int incomingMessageId;

    public IncomingMessagePk(int incomingMessageId) {
        this.incomingMessageId = incomingMessageId;
    }

    public int getIncomingMessageId() {
        return incomingMessageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IncomingMessagePk that = (IncomingMessagePk) o;

        if (incomingMessageId != that.incomingMessageId) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return incomingMessageId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IncomingMessageId");
        sb.append("{incomingMessageId=").append(incomingMessageId);
        sb.append('}');
        return sb.toString();
    }
}
