package com.alteregos.sms.campaigner.data.dto;

import com.alteregos.sms.campaigner.business.Encoding;
import com.alteregos.sms.campaigner.business.MessagePriority;
import com.alteregos.sms.campaigner.business.MessageStatus;
import com.alteregos.sms.campaigner.business.OutgoingMessageType;
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
public class OutgoingMessage {

    private int outgoingMessageId;
    private String recepientNo;
    private String senderNo;
    private String content;
    private Encoding encoding = Encoding.SEVEN_BIT;
    private boolean statusReport = false;
    private boolean flashMessage = false;
    private int srcPort = -1;
    private int dstPort = -1;
    private Date createdDate = new Date();
    private Date sentDate;
    private String referenceNo;
    private MessagePriority priority = MessagePriority.NORMAL;
    private MessageStatus status = MessageStatus.UNSENT;
    private int errors = 0;
    private String gatewayId;
    private OutgoingMessageType type = OutgoingMessageType.BULK;

    public OutgoingMessage() {
    }

    public int getOutgoingMessageId() {
        return outgoingMessageId;
    }

    public void setOutgoingMessageId(int outgoingMessageId) {
        this.outgoingMessageId = outgoingMessageId;
    }

    public String getRecepientNo() {
        return recepientNo;
    }

    public void setRecepientNo(String recepientNo) {
        this.recepientNo = recepientNo;
    }

    public String getSenderNo() {
        return senderNo;
    }

    public void setSenderNo(String senderNo) {
        this.senderNo = senderNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Encoding getEncoding() {
        return encoding;
    }

    public void setEncoding(Encoding encoding) {
        this.encoding = encoding;
    }

    public boolean isStatusReport() {
        return statusReport;
    }

    public void setStatusReport(boolean statusReport) {
        this.statusReport = statusReport;
    }

    public boolean isFlashMessage() {
        return flashMessage;
    }

    public void setFlashMessage(boolean flashMessage) {
        this.flashMessage = flashMessage;
    }

    public int getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(int srcPort) {
        if (srcPort < 1000) {
            this.srcPort = srcPort;
        }
    }

    public int getDstPort() {
        return dstPort;
    }

    public void setDstPort(int dstPort) {
        if (dstPort < 1000) {
            this.dstPort = dstPort;
        }
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public MessagePriority getPriority() {
        return priority;
    }

    public void setPriority(MessagePriority priority) {
        this.priority = priority;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        if (errors < 1000) {
            this.errors = errors;
        }
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public OutgoingMessageType getType() {
        return type;
    }

    public void setType(OutgoingMessageType type) {
        this.type = type;
    }

    public OutgoingMessagePk createPk() {
        return new OutgoingMessagePk(outgoingMessageId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OutgoingMessage that = (OutgoingMessage) o;

        if (dstPort != that.dstPort) {
            return false;
        }
        if (errors != that.errors) {
            return false;
        }
        if (flashMessage != that.flashMessage) {
            return false;
        }
        if (outgoingMessageId != that.outgoingMessageId) {
            return false;
        }
        if (srcPort != that.srcPort) {
            return false;
        }
        if (statusReport != that.statusReport) {
            return false;
        }
        if (content != null ? !content.equals(that.content) : that.content != null) {
            return false;
        }
        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) {
            return false;
        }
        if (encoding != that.encoding) {
            return false;
        }
        if (gatewayId != null ? !gatewayId.equals(that.gatewayId) : that.gatewayId != null) {
            return false;
        }
        if (priority != that.priority) {
            return false;
        }
        if (recepientNo != null ? !recepientNo.equals(that.recepientNo) : that.recepientNo != null) {
            return false;
        }
        if (referenceNo != null ? !referenceNo.equals(that.referenceNo) : that.referenceNo != null) {
            return false;
        }
        if (senderNo != null ? !senderNo.equals(that.senderNo) : that.senderNo != null) {
            return false;
        }
        if (sentDate != null ? !sentDate.equals(that.sentDate) : that.sentDate != null) {
            return false;
        }
        if (status != that.status) {
            return false;
        }
        if (type != that.type) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = outgoingMessageId;
        result = 31 * result + (recepientNo != null ? recepientNo.hashCode() : 0);
        result = 31 * result + (senderNo != null ? senderNo.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (encoding != null ? encoding.hashCode() : 0);
        result = 31 * result + (statusReport ? 1 : 0);
        result = 31 * result + (flashMessage ? 1 : 0);
        result = 31 * result + srcPort;
        result = 31 * result + dstPort;
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (sentDate != null ? sentDate.hashCode() : 0);
        result = 31 * result + (referenceNo != null ? referenceNo.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + errors;
        result = 31 * result + (gatewayId != null ? gatewayId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("OutgoingMessage");
        sb.append("{outgoingMessageId=").append(outgoingMessageId);
        sb.append(", recepientNo='").append(recepientNo).append('\'');
        sb.append(", senderNo='").append(senderNo).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", encoding=").append(encoding);
        sb.append(", statusReport=").append(statusReport);
        sb.append(", flashMessage=").append(flashMessage);
        sb.append(", srcPort=").append(srcPort);
        sb.append(", dstPort=").append(dstPort);
        sb.append(", createdDate=").append(createdDate);
        sb.append(", sentDate=").append(sentDate);
        sb.append(", referenceNo='").append(referenceNo).append('\'');
        sb.append(", priority=").append(priority);
        sb.append(", status=").append(status);
        sb.append(", errors=").append(errors);
        sb.append(", gatewayId='").append(gatewayId).append('\'');
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }
}
