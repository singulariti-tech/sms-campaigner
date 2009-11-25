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

    private int incomingCallPk;

    public IncomingCallPk(int incomingCallPk) {
        this.incomingCallPk = incomingCallPk;
    }

    public int getIncomingCallPk() {
        return incomingCallPk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IncomingCallPk that = (IncomingCallPk) o;

        if (incomingCallPk != that.incomingCallPk) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return incomingCallPk;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IncomingCallPk");
        sb.append("{incomingCallPk=").append(incomingCallPk);
        sb.append('}');
        return sb.toString();
    }
}
