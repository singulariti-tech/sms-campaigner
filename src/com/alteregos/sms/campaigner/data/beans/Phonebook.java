/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.data.beans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author John Emmanuel
 */
@Entity
@Table(name = "phonebook", schema = "")
@NamedQueries({@NamedQuery(name = "Phonebook.findAll", query = "SELECT p FROM Phonebook p"), @NamedQuery(name = "Phonebook.findByPhonebookId", query = "SELECT p FROM Phonebook p WHERE p.phonebookId = :phonebookId"), @NamedQuery(name = "Phonebook.findByName", query = "SELECT p FROM Phonebook p WHERE p.name = :name"), @NamedQuery(name = "Phonebook.findByMobileNo", query = "SELECT p FROM Phonebook p WHERE p.mobileNo = :mobileNo"), @NamedQuery(name = "Phonebook.findByEmail", query = "SELECT p FROM Phonebook p WHERE p.email = :email"), @NamedQuery(name = "Phonebook.findByAddress", query = "SELECT p FROM Phonebook p WHERE p.address = :address")})
public class Phonebook implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phonebook_id", nullable = false)
    private Integer phonebookId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "mobile_no", nullable = false)
    private String mobileNo;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "address", nullable = false)
    private String address;
    @ManyToMany(mappedBy = "phoneBookCollection")
    private Collection<Smsgroup> groupCollection;

    public Phonebook() {
    }

    public Phonebook(Integer phonebookId) {
        this.phonebookId = phonebookId;
    }

    public Phonebook(Integer phonebookId, String name, String mobileNo, String email, String address) {
        this.phonebookId = phonebookId;
        this.name = name;
        this.mobileNo = mobileNo;
        this.email = email;
        this.address = address;
    }

    public Integer getPhonebookId() {
        return phonebookId;
    }

    public void setPhonebookId(Integer phonebookId) {
        Integer oldPhonebookId = this.phonebookId;
        this.phonebookId = phonebookId;
        changeSupport.firePropertyChange("phonebookId", oldPhonebookId, phonebookId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        changeSupport.firePropertyChange("name", oldName, name);
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        String oldMobileNo = this.mobileNo;
        this.mobileNo = mobileNo;
        changeSupport.firePropertyChange("mobileNo", oldMobileNo, mobileNo);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String oldEmail = this.email;
        this.email = email;
        changeSupport.firePropertyChange("email", oldEmail, email);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        String oldAddress = this.address;
        this.address = address;
        changeSupport.firePropertyChange("address", oldAddress, address);
    }

    public Collection<Smsgroup> getGroupCollection() {
        return groupCollection;
    }

    public void setGroupCollection(Collection<Smsgroup> groupCollection) {
        this.groupCollection = groupCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (phonebookId != null ? phonebookId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Phonebook)) {
            return false;
        }
        Phonebook other = (Phonebook) object;
        if ((this.phonebookId == null && other.phonebookId != null) || (this.phonebookId != null && !this.phonebookId.equals(other.phonebookId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.absolute.sms.views.Phonebook[phonebookId=" + phonebookId + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
