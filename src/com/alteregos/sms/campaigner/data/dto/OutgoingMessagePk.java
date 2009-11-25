package com.alteregos.sms.campaigner.data.dto;

/**
 * Date: Oct 24, 2009
 * Time: 12:51:30 PM
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class OutgoingMessagePk {

    private int outgoingMessageId;

    public OutgoingMessagePk(int outgoingMessageId) {
        this.outgoingMessageId = outgoingMessageId;
    }

    public int getOutgoingMessageId() {
        return outgoingMessageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OutgoingMessagePk that = (OutgoingMessagePk) o;

        if (outgoingMessageId != that.outgoingMessageId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return outgoingMessageId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OutgoingMessagePk");
        sb.append("{outgoingMessageId=").append(outgoingMessageId);
        sb.append('}');
        return sb.toString();
    }
}
