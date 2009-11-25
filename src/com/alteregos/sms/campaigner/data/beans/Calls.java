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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author John
 */
@Entity
@Table(name = "calls", schema = "")
@NamedQueries({@NamedQuery(name = "Calls.findAll", query = "SELECT c FROM Calls c"), @NamedQuery(name = "Calls.findByCallId", query = "SELECT c FROM Calls c WHERE c.callId = :callId"), @NamedQuery(name = "Calls.findByReceiptDate", query = "SELECT c FROM Calls c WHERE c.receiptDate = :receiptDate"), @NamedQuery(name = "Calls.findByGatewayId", query = "SELECT c FROM Calls c WHERE c.gatewayId = :gatewayId"), @NamedQuery(name = "Calls.findByCaller", query = "SELECT c FROM Calls c WHERE c.caller = :caller"), @NamedQuery(name = "Calls.findByProcess", query = "SELECT c FROM Calls c WHERE c.process = :process")})
public class Calls implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "call_id", nullable = false)
    private Integer callId;
    @Column(name = "receipt_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiptDate;
    @Column(name = "gateway_id", nullable = false)
    private String gatewayId;
    @Column(name = "caller", nullable = false)
    private String caller;
    @Column(name = "process", nullable = false)
    private boolean process = false;

    public Calls() {
    }

    public Calls(Integer callId) {
        this.callId = callId;
    }

    public Calls(Integer callId, Date receiptDate, String gatewayId, String caller, boolean process) {
        this.callId = callId;
        this.receiptDate = receiptDate;
        this.gatewayId = gatewayId;
        this.caller = caller;
        this.process = process;
    }

    public Integer getCallId() {
        return callId;
    }

    public void setCallId(Integer callId) {
        Integer oldCallId = this.callId;
        this.callId = callId;
        changeSupport.firePropertyChange("callId", oldCallId, callId);
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        Date oldReceiptDate = this.receiptDate;
        this.receiptDate = receiptDate;
        changeSupport.firePropertyChange("receiptDate", oldReceiptDate, receiptDate);
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        String oldGatewayId = this.gatewayId;
        this.gatewayId = gatewayId;
        changeSupport.firePropertyChange("gatewayId", oldGatewayId, gatewayId);
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        String oldCaller = this.caller;
        this.caller = caller;
        changeSupport.firePropertyChange("caller", oldCaller, caller);
    }

    public boolean getProcess() {
        return process;
    }

    public void setProcess(boolean process) {
        boolean oldProcess = this.process;
        this.process = process;
        changeSupport.firePropertyChange("process", oldProcess, process);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (callId != null ? callId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Calls)) {
            return false;
        }
        Calls other = (Calls) object;
        if ((this.callId == null && other.callId != null) || (this.callId != null && !this.callId.equals(other.callId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.absolute.sms.views.Calls[callId=" + callId + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
