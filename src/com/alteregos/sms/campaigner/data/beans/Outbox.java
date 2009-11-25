package com.alteregos.sms.campaigner.data.beans;

import com.alteregos.sms.campaigner.business.MessageEncoding;
import com.alteregos.sms.campaigner.business.MessagePriority;
import com.alteregos.sms.campaigner.business.MessageStatus;
import com.alteregos.sms.campaigner.business.OutboundSmsType;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author John Emmanuel
 */
@Entity
@Table(name = "outbox", schema = "")
@NamedQueries({@NamedQuery(name = "Outbox.findAll", query = "SELECT o FROM Outbox o"), @NamedQuery(name = "Outbox.findByOutboxId", query = "SELECT o FROM Outbox o WHERE o.outboxId = :outboxId"), @NamedQuery(name = "Outbox.findByRecepient", query = "SELECT o FROM Outbox o WHERE o.recepient = :recepient"), @NamedQuery(name = "Outbox.findBySender", query = "SELECT o FROM Outbox o WHERE o.sender = :sender"), @NamedQuery(name = "Outbox.findByContent", query = "SELECT o FROM Outbox o WHERE o.content = :content"), @NamedQuery(name = "Outbox.findByEncoding", query = "SELECT o FROM Outbox o WHERE o.encoding = :encoding"), @NamedQuery(name = "Outbox.findByStatusReport", query = "SELECT o FROM Outbox o WHERE o.statusReport = :statusReport"), @NamedQuery(name = "Outbox.findByFlashSms", query = "SELECT o FROM Outbox o WHERE o.flashSms = :flashSms"), @NamedQuery(name = "Outbox.findBySrcPort", query = "SELECT o FROM Outbox o WHERE o.srcPort = :srcPort"), @NamedQuery(name = "Outbox.findByDstPort", query = "SELECT o FROM Outbox o WHERE o.dstPort = :dstPort"), @NamedQuery(name = "Outbox.findByCreatedDate", query = "SELECT o FROM Outbox o WHERE o.createdDate = :createdDate"), @NamedQuery(name = "Outbox.findBySentDate", query = "SELECT o FROM Outbox o WHERE o.sentDate = :sentDate"), @NamedQuery(name = "Outbox.findByRefNo", query = "SELECT o FROM Outbox o WHERE o.refNo = :refNo"), @NamedQuery(name = "Outbox.findByPriority", query = "SELECT o FROM Outbox o WHERE o.priority = :priority"), @NamedQuery(name = "Outbox.findByStatus", query = "SELECT o FROM Outbox o WHERE o.status = :status"), @NamedQuery(name = "Outbox.findByErrors", query = "SELECT o FROM Outbox o WHERE o.errors = :errors"), @NamedQuery(name = "Outbox.findByGatewayId", query = "SELECT o FROM Outbox o WHERE o.gatewayId = :gatewayId"), @NamedQuery(name = "Outbox.findByType", query = "SELECT o FROM Outbox o WHERE o.type = :type")})
public class Outbox implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outbox_id", nullable = false)
    private Integer outboxId;
    @Column(name = "recepient", nullable = false)
    private String recepient;
    @Column(name = "sender", nullable = true)
    private String sender;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "encoding", nullable = false)
    private String encoding = MessageEncoding.SEVEN_BIT.getLabel();
    @Column(name = "status_report", nullable = false)
    private boolean statusReport = false;
    @Column(name = "flash_sms", nullable = false)
    private boolean flashSms = false;
    @Column(name = "src_port", nullable = false)
    private short srcPort = -1;
    @Column(name = "dst_port", nullable = false)
    private short dstPort = -1;
    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();
    @Column(name = "sent_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentDate;
    @Column(name = "ref_no")
    private String refNo;
    @Column(name = "priority", nullable = false)
    private String priority = MessagePriority.NORMAL.toString();
    @Column(name = "status", nullable = false)
    private String status = MessageStatus.UNSENT.toString();
    @Column(name = "errors", nullable = false)
    private short errors = 0;
    @Column(name = "gateway_id", nullable = true)
    private String gatewayId;
    @Column(name = "type", nullable = false)
    private String type = OutboundSmsType.BULK.getCode();

    public Outbox() {
    }

    public Outbox(Integer outboxId) {
        this.outboxId = outboxId;
    }

    public Outbox(Integer outboxId, String recepient, String sender, String content, String encoding, boolean statusReport, boolean flashSms, short srcPort, short dstPort, Date createdDate, String priority, String status, short errors, String gatewayId, String type) {
        this.outboxId = outboxId;
        this.recepient = recepient;
        this.sender = sender;
        this.content = content;
        this.encoding = encoding;
        this.statusReport = statusReport;
        this.flashSms = flashSms;
        this.srcPort = srcPort;
        this.dstPort = dstPort;
        this.createdDate = createdDate;
        this.priority = priority;
        this.status = status;
        this.errors = errors;
        this.gatewayId = gatewayId;
        this.type = type;
    }

    public Integer getOutboxId() {
        return outboxId;
    }

    public void setOutboxId(Integer outboxId) {
        Integer oldOutboxId = this.outboxId;
        this.outboxId = outboxId;
        changeSupport.firePropertyChange("outboxId", oldOutboxId, outboxId);
    }

    public String getRecepient() {
        return recepient;
    }

    public void setRecepient(String recepient) {
        String oldRecepient = this.recepient;
        this.recepient = recepient;
        changeSupport.firePropertyChange("recepient", oldRecepient, recepient);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        String oldSender = this.sender;
        this.sender = sender;
        changeSupport.firePropertyChange("sender", oldSender, sender);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        String oldContent = this.content;
        this.content = content;
        changeSupport.firePropertyChange("content", oldContent, content);
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        String oldEncoding = this.encoding;
        this.encoding = encoding;
        changeSupport.firePropertyChange("encoding", oldEncoding, encoding);
    }

    public boolean getStatusReport() {
        return statusReport;
    }

    public void setStatusReport(boolean statusReport) {
        boolean oldStatusReport = this.statusReport;
        this.statusReport = statusReport;
        changeSupport.firePropertyChange("statusReport", oldStatusReport, statusReport);
    }

    public boolean getFlashSms() {
        return flashSms;
    }

    public void setFlashSms(boolean flashSms) {
        boolean oldFlashSms = this.flashSms;
        this.flashSms = flashSms;
        changeSupport.firePropertyChange("flashSms", oldFlashSms, flashSms);
    }

    public short getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(short srcPort) {
        short oldSrcPort = this.srcPort;
        this.srcPort = srcPort;
        changeSupport.firePropertyChange("srcPort", oldSrcPort, srcPort);
    }

    public short getDstPort() {
        return dstPort;
    }

    public void setDstPort(short dstPort) {
        short oldDstPort = this.dstPort;
        this.dstPort = dstPort;
        changeSupport.firePropertyChange("dstPort", oldDstPort, dstPort);
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        Date oldCreatedDate = this.createdDate;
        this.createdDate = createdDate;
        changeSupport.firePropertyChange("createdDate", oldCreatedDate, createdDate);
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        Date oldSentDate = this.sentDate;
        this.sentDate = sentDate;
        changeSupport.firePropertyChange("sentDate", oldSentDate, sentDate);
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        String oldRefNo = this.refNo;
        this.refNo = refNo;
        changeSupport.firePropertyChange("refNo", oldRefNo, refNo);
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        String oldPriority = this.priority;
        this.priority = priority;
        changeSupport.firePropertyChange("priority", oldPriority, priority);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        String oldStatus = this.status;
        this.status = status;
        changeSupport.firePropertyChange("status", oldStatus, status);
    }

    public short getErrors() {
        return errors;
    }

    public void setErrors(short errors) {
        short oldErrors = this.errors;
        this.errors = errors;
        changeSupport.firePropertyChange("errors", oldErrors, errors);
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        String oldGatewayId = this.gatewayId;
        this.gatewayId = gatewayId;
        changeSupport.firePropertyChange("gatewayId", oldGatewayId, gatewayId);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        String oldType = this.type;
        this.type = type;
        changeSupport.firePropertyChange("type", oldType, type);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outboxId != null ? outboxId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Outbox)) {
            return false;
        }
        Outbox other = (Outbox) object;
        if ((this.outboxId == null && other.outboxId != null) || (this.outboxId != null && !this.outboxId.equals(other.outboxId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.absolute.sms.views.Outbox[outboxId=" + outboxId + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
