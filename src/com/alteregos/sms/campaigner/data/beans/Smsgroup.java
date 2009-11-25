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
@Table(name = "smsgroup", schema = "")
@NamedQueries({@NamedQuery(name = "Smsgroup.findAll", query = "SELECT s FROM Smsgroup s"), @NamedQuery(name = "Smsgroup.findByGroupId", query = "SELECT s FROM Smsgroup s WHERE s.groupId = :groupId"), @NamedQuery(name = "Smsgroup.findByName", query = "SELECT s FROM Smsgroup s WHERE s.name = :name")})
public class Smsgroup implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false)
    private Integer groupId;
    @Column(name = "name", nullable = false)
    private String name;
    @JoinTable(name = "group_phonebook_join", joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "group_id")}, inverseJoinColumns = {@JoinColumn(name = "phonebook_id", referencedColumnName = "phonebook_id")})
    @ManyToMany
    private Collection<Phonebook> phoneBookCollection;

    public Smsgroup() {
    }

    public Smsgroup(Integer groupId) {
        this.groupId = groupId;
    }

    public Smsgroup(Integer groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        Integer oldGroupId = this.groupId;
        this.groupId = groupId;
        changeSupport.firePropertyChange("groupId", oldGroupId, groupId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        changeSupport.firePropertyChange("name", oldName, name);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupId != null ? groupId.hashCode() : 0);
        return hash;
    }

    public Collection<Phonebook> getPhoneBookCollection() {
        return phoneBookCollection;
    }

    public void setPhoneBookCollection(Collection<Phonebook> phoneBookCollection) {
        this.phoneBookCollection = phoneBookCollection;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Smsgroup)) {
            return false;
        }
        Smsgroup other = (Smsgroup) object;
        if ((this.groupId == null && other.groupId != null) || (this.groupId != null && !this.groupId.equals(other.groupId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.absolute.sms.views.Smsgroup[groupId=" + groupId + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
