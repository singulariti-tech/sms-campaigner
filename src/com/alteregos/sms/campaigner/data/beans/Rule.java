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
@Table(name = "rule", schema = "")
@NamedQueries({@NamedQuery(name = "Rule.findAll", query = "SELECT r FROM Rule r"), @NamedQuery(name = "Rule.findByRuleId", query = "SELECT r FROM Rule r WHERE r.ruleId = :ruleId"), @NamedQuery(name = "Rule.findByPrimaryKeyword", query = "SELECT r FROM Rule r WHERE r.primaryKeyword = :primaryKeyword"), @NamedQuery(name = "Rule.findBySecondaryKeyword", query = "SELECT r FROM Rule r WHERE r.secondaryKeyword = :secondaryKeyword"), @NamedQuery(name = "Rule.findByEnabled", query = "SELECT r FROM Rule r WHERE r.enabled = :enabled"), @NamedQuery(name = "Rule.findByContent", query = "SELECT r FROM Rule r WHERE r.content = :content"), @NamedQuery(name = "Rule.findByCreatedDate", query = "SELECT r FROM Rule r WHERE r.createdDate = :createdDate"), @NamedQuery(name = "Rule.findByModifiedDate", query = "SELECT r FROM Rule r WHERE r.modifiedDate = :modifiedDate")})
public class Rule implements Serializable, IRule {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id", nullable = false)
    private Integer ruleId;
    @Column(name = "primary_keyword", nullable = false)
    private String primaryKeyword;
    @Column(name = "secondary_keyword", nullable = true)
    private String secondaryKeyword = null;
    @Column(name = "enabled", nullable = false)
    private boolean enabled;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    public Rule() {
    }

    public Rule(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Rule(Integer ruleId, String primaryKeyword, String secondaryKeyword, boolean enabled, String content, Date createdDate) {
        this.ruleId = ruleId;
        this.primaryKeyword = primaryKeyword;
        this.secondaryKeyword = secondaryKeyword;
        this.enabled = enabled;
        this.content = content;
        this.createdDate = createdDate;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        Integer oldRuleId = this.ruleId;
        this.ruleId = ruleId;
        changeSupport.firePropertyChange("ruleId", oldRuleId, ruleId);
    }

    public String getPrimaryKeyword() {
        return primaryKeyword;
    }

    public void setPrimaryKeyword(String primaryKeyword) {
        String oldPrimaryKeyword = this.primaryKeyword;
        this.primaryKeyword = primaryKeyword;
        changeSupport.firePropertyChange("primaryKeyword", oldPrimaryKeyword, primaryKeyword);
    }

    public String getSecondaryKeyword() {
        return secondaryKeyword;
    }

    public void setSecondaryKeyword(String secondaryKeyword) {
        String oldSecondaryKeyword = this.secondaryKeyword;
        this.secondaryKeyword = secondaryKeyword;
        changeSupport.firePropertyChange("secondaryKeyword", oldSecondaryKeyword, secondaryKeyword);
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        boolean oldEnabled = this.enabled;
        this.enabled = enabled;
        changeSupport.firePropertyChange("enabled", oldEnabled, enabled);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        String oldContent = this.content;
        this.content = content;
        changeSupport.firePropertyChange("content", oldContent, content);
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        Date oldCreatedDate = this.createdDate;
        this.createdDate = createdDate;
        changeSupport.firePropertyChange("createdDate", oldCreatedDate, createdDate);
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        Date oldModifiedDate = this.modifiedDate;
        this.modifiedDate = modifiedDate;
        changeSupport.firePropertyChange("modifiedDate", oldModifiedDate, modifiedDate);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ruleId != null ? ruleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rule)) {
            return false;
        }
        Rule other = (Rule) object;
        if ((this.ruleId == null && other.ruleId != null) || (this.ruleId != null && !this.ruleId.equals(other.ruleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.absolute.sms.views.Rule[ruleId=" + ruleId + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
