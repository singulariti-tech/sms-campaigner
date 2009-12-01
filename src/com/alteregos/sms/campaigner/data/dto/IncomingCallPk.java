package com.alteregos.sms.campaigner.data.dto;

/**
 * Date: Oct 24, 2009
 * Time: 12:49:38 PM
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class IncomingCallPk {

    private int incomingCallId;

    public IncomingCallPk(int incomingCallId) {
        this.incomingCallId = incomingCallId;
    }

    public int getIncomingCallId() {
        return incomingCallId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IncomingCallPk that = (IncomingCallPk) o;

        if (incomingCallId != that.incomingCallId) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return incomingCallId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IncomingCallId");
        sb.append("{incomingCallId=").append(incomingCallId);
        sb.append('}');
        return sb.toString();
    }
}
