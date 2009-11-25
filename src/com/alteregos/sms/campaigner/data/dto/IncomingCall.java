package com.alteregos.sms.campaigner.data.dto;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Date: 22-Oct-2009
 * Time: 14:51:09
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */

@Service
public class IncomingCall {
    private int incomingCallId;
    private Date receiptDate;
    private String gatewayId;
    private String callerNo;
    private boolean process = false;

    public IncomingCall () {
    }

    public int getIncomingCallId () {
        return incomingCallId;
    }

    public void setIncomingCallId (int incomingCallId) {
        this.incomingCallId = incomingCallId;
    }

    public Date getReceiptDate () {
        return receiptDate;
    }

    public void setReceiptDate (Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getGatewayId () {
        return gatewayId;
    }

    public void setGatewayId (String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getCallerNo () {
        return callerNo;
    }

    public void setCallerNo (String callerNo) {
        this.callerNo = callerNo;
    }

    public boolean isProcess () {
        return process;
    }

    public void setProcess (boolean process) {
        this.process = process;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;

        IncomingCall that = (IncomingCall) o;

        if (gatewayId != that.gatewayId) return false;
        if (incomingCallId != that.incomingCallId) return false;
        if (process != that.process) return false;
        if (callerNo != null ? !callerNo.equals (that.callerNo) : that.callerNo != null) return false;
        if (receiptDate != null ? !receiptDate.equals (that.receiptDate) : that.receiptDate != null) return false;

        return true;
    }

    @Override
    public int hashCode () {
        int result = incomingCallId;
        result = 31 * result + (receiptDate != null ? receiptDate.hashCode () : 0);
        result = 31 * result + (gatewayId != null ? gatewayId.hashCode () : 0);
        result = 31 * result + (callerNo != null ? callerNo.hashCode () : 0);
        result = 31 * result + (process ? 1 : 0);
        return result;
    }

    @Override
    public String toString () {
        final StringBuffer sb = new StringBuffer ();
        sb.append ("IncomingCall");
        sb.append ("{incomingCallId=").append (incomingCallId);
        sb.append (", receiptDate=").append (receiptDate);
        sb.append (", gatewayId='").append (gatewayId).append ('\'');
        sb.append (", callerNo='").append (callerNo).append ('\'');
        sb.append (", process=").append (process);
        sb.append ('}');
        return sb.toString ();
    }
}
