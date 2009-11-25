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
@Table(name = "dnd", schema = "")
@NamedQueries({@NamedQuery(name = "Dnd.findAll", query = "SELECT d FROM Dnd d"), @NamedQuery(name = "Dnd.findByDndId", query = "SELECT d FROM Dnd d WHERE d.dndId = :dndId"), @NamedQuery(name = "Dnd.findByMobileNo", query = "SELECT d FROM Dnd d WHERE d.mobileNo = :mobileNo"), @NamedQuery(name = "Dnd.findByRegisteredDate", query = "SELECT d FROM Dnd d WHERE d.registeredDate = :registeredDate")})
public class Dnd implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dnd_id", nullable = false)
    private Integer dndId;
    @Column(name = "mobile_no", nullable = false)
    private String mobileNo;
    @Column(name = "registered_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registeredDate;

    public Dnd() {
    }

    public Dnd(Integer dndId) {
        this.dndId = dndId;
    }

    public Dnd(Integer dndId, String mobileNo) {
        this.dndId = dndId;
        this.mobileNo = mobileNo;
    }

    public Integer getDndId() {
        return dndId;
    }

    public void setDndId(Integer dndId) {
        Integer oldDndId = this.dndId;
        this.dndId = dndId;
        changeSupport.firePropertyChange("dndId", oldDndId, dndId);
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        String oldMobileNo = this.mobileNo;
        this.mobileNo = mobileNo;
        changeSupport.firePropertyChange("mobileNo", oldMobileNo, mobileNo);
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        Date oldRegisteredDate = this.registeredDate;
        this.registeredDate = registeredDate;
        changeSupport.firePropertyChange("registeredDate", oldRegisteredDate, registeredDate);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dndId != null ? dndId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dnd)) {
            return false;
        }
        Dnd other = (Dnd) object;
        if ((this.dndId == null && other.dndId != null) || (this.dndId != null && !this.dndId.equals(other.dndId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.absolute.sms.views.Dnd[dndId=" + dndId + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
