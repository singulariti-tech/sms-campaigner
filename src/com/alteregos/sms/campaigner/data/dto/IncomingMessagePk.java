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

    private int incomingMessagePk;

    public IncomingMessagePk(int incomingMessagePk) {
        this.incomingMessagePk = incomingMessagePk;
    }

    public int getIncomingMessagePk() {
        return incomingMessagePk;
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

        if (incomingMessagePk != that.incomingMessagePk) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return incomingMessagePk;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IncomingMessagePk");
        sb.append("{incomingMessagePk=").append(incomingMessagePk);
        sb.append('}');
        return sb.toString();
    }
}
