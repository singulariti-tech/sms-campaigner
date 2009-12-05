package com.alteregos.sms.campaigner.data.dto;

import com.alteregos.sms.campaigner.business.Encoding;
import com.alteregos.sms.campaigner.business.IncomingMessageType;
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
public class IncomingMessage {

    private int incomingMessageId;
    private boolean process = false;
    private String senderNo;
    private Encoding encoding = Encoding.SEVEN_BIT;
    private Date messageDate;
    private Date receiptDate;
    private String content;
    private String gatewayId;
    private IncomingMessageType type;

    public IncomingMessage() {
    }

    public int getIncomingMessageId() {
        return incomingMessageId;
    }

    public void setIncomingMessageId(int incomingMessageId) {
        this.incomingMessageId = incomingMessageId;
    }

    public boolean isProcess() {
        return process;
    }

    public void setProcess(boolean process) {
        this.process = process;
    }

    public String getSenderNo() {
        return senderNo;
    }

    public void setSenderNo(String senderNo) {
        this.senderNo = senderNo;
    }

    public Encoding getEncoding() {
        return encoding;
    }

    public void setEncoding(Encoding encoding) {
        this.encoding = encoding;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public IncomingMessageType getType() {
        return type;
    }

    public void setType(IncomingMessageType type) {
        this.type = type;
    }

    public IncomingMessagePk createPk() {
        return new IncomingMessagePk(incomingMessageId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IncomingMessage that = (IncomingMessage) o;

        if (incomingMessageId != that.incomingMessageId) {
            return false;
        }
        if (process != that.process) {
            return false;
        }
        if (content != null ? !content.equals(that.content) : that.content != null) {
            return false;
        }
        if (encoding != that.encoding) {
            return false;
        }
        if (gatewayId != null ? !gatewayId.equals(that.gatewayId) : that.gatewayId != null) {
            return false;
        }
        if (messageDate != null ? !messageDate.equals(that.messageDate) : that.messageDate != null) {
            return false;
        }
        if (receiptDate != null ? !receiptDate.equals(that.receiptDate) : that.receiptDate != null) {
            return false;
        }
        if (senderNo != null ? !senderNo.equals(that.senderNo) : that.senderNo != null) {
            return false;
        }
        if (type != that.type) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = incomingMessageId;
        result = 31 * result + (process ? 1 : 0);
        result = 31 * result + (senderNo != null ? senderNo.hashCode() : 0);
        result = 31 * result + (encoding != null ? encoding.hashCode() : 0);
        result = 31 * result + (messageDate != null ? messageDate.hashCode() : 0);
        result = 31 * result + (receiptDate != null ? receiptDate.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (gatewayId != null ? gatewayId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("IncomingMessage");
        sb.append("{incomingMessageId=").append(incomingMessageId);
        sb.append(", process=").append(process);
        sb.append(", senderNo='").append(senderNo).append('\'');
        sb.append(", encoding=").append(encoding);
        sb.append(", messageDate=").append(messageDate);
        sb.append(", receiptDate=").append(receiptDate);
        sb.append(", content='").append(content).append('\'');
        sb.append(", gatewayId='").append(gatewayId).append('\'');
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }
}
