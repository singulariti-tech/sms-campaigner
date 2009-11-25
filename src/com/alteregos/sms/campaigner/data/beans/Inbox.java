/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.data.beans;

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
@Table(name = "inbox", schema = "")
@NamedQueries({@NamedQuery(name = "Inbox.findAll", query = "SELECT i FROM Inbox i"), @NamedQuery(name = "Inbox.findByInboxId", query = "SELECT i FROM Inbox i WHERE i.inboxId = :inboxId"), @NamedQuery(name = "Inbox.findByProcess", query = "SELECT i FROM Inbox i WHERE i.process = :process"), @NamedQuery(name = "Inbox.findBySender", query = "SELECT i FROM Inbox i WHERE i.sender = :sender"), @NamedQuery(name = "Inbox.findByType", query = "SELECT i FROM Inbox i WHERE i.type = :type"), @NamedQuery(name = "Inbox.findByEncoding", query = "SELECT i FROM Inbox i WHERE i.encoding = :encoding"), @NamedQuery(name = "Inbox.findByMessageDate", query = "SELECT i FROM Inbox i WHERE i.messageDate = :messageDate"), @NamedQuery(name = "Inbox.findByReceiptDate", query = "SELECT i FROM Inbox i WHERE i.receiptDate = :receiptDate"), @NamedQuery(name = "Inbox.findByContent", query = "SELECT i FROM Inbox i WHERE i.content = :content"), @NamedQuery(name = "Inbox.findByGatewayId", query = "SELECT i FROM Inbox i WHERE i.gatewayId = :gatewayId")})
public class Inbox implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inbox_id", nullable = false)
    private Integer inboxId;
    @Column(name = "process", nullable = false)
    private boolean process = false;
    @Column(name = "sender", nullable = false)
    private String sender;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "encoding", nullable = false)
    private String encoding;
    @Column(name = "message_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date messageDate;
    @Column(name = "receipt_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiptDate;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "gateway_id", nullable = false)
    private String gatewayId;

    public Inbox() {
    }

    public Inbox(Integer inboxId) {
        this.inboxId = inboxId;
    }

    public Inbox(Integer inboxId, boolean process, String sender, String type, String encoding, Date messageDate, Date receiptDate, String content, String gatewayId) {
        this.inboxId = inboxId;
        this.process = process;
        this.sender = sender;
        this.type = type;
        this.encoding = encoding;
        this.messageDate = messageDate;
        this.receiptDate = receiptDate;
        this.content = content;
        this.gatewayId = gatewayId;
    }

    public Integer getInboxId() {
        return inboxId;
    }

    public void setInboxId(Integer inboxId) {
        Integer oldInboxId = this.inboxId;
        this.inboxId = inboxId;
        changeSupport.firePropertyChange("inboxId", oldInboxId, inboxId);
    }

    public boolean getProcess() {
        return process;
    }

    public void setProcess(boolean process) {
        boolean oldProcess = this.process;
        this.process = process;
        changeSupport.firePropertyChange("process", oldProcess, process);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        String oldSender = this.sender;
        this.sender = sender;
        changeSupport.firePropertyChange("sender", oldSender, sender);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        String oldType = this.type;
        this.type = type;
        changeSupport.firePropertyChange("type", oldType, type);
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        String oldEncoding = this.encoding;
        this.encoding = encoding;
        changeSupport.firePropertyChange("encoding", oldEncoding, encoding);
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        Date oldMessageDate = this.messageDate;
        this.messageDate = messageDate;
        changeSupport.firePropertyChange("messageDate", oldMessageDate, messageDate);
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        Date oldReceiptDate = this.receiptDate;
        this.receiptDate = receiptDate;
        changeSupport.firePropertyChange("receiptDate", oldReceiptDate, receiptDate);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        String oldContent = this.content;
        this.content = content;
        changeSupport.firePropertyChange("content", oldContent, content);
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        String oldGatewayId = this.gatewayId;
        this.gatewayId = gatewayId;
        changeSupport.firePropertyChange("gatewayId", oldGatewayId, gatewayId);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (inboxId != null ? inboxId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inbox)) {
            return false;
        }
        Inbox other = (Inbox) object;
        if ((this.inboxId == null && other.inboxId != null) || (this.inboxId != null && !this.inboxId.equals(other.inboxId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.absolute.sms.views.Inbox[inboxId=" + inboxId + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
