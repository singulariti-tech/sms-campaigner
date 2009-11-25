package com.alteregos.sms.campaigner.data.dto;

import com.alteregos.sms.campaigner.rules.IRule;
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
public class AutoReplyRule implements IRule {
    private int autoReplyRuleId;
    private String primaryKeyword;
    private String secondaryKeyword;
    private boolean enabled;
    private String content;
    private Date createdDate = new Date ();
    private Date modifiedDate = new Date ();

    public AutoReplyRule () {
    }

    public int getAutoReplyRuleId () {
        return autoReplyRuleId;
    }

    public void setAutoReplyRuleId (int autoReplyRuleId) {
        this.autoReplyRuleId = autoReplyRuleId;
    }

    public String getPrimaryKeyword () {
        return primaryKeyword;
    }

    public void setPrimaryKeyword (String primaryKeyword) {
        this.primaryKeyword = primaryKeyword;
    }

    public String getSecondaryKeyword () {
        return secondaryKeyword;
    }

    public void setSecondaryKeyword (String secondaryKeyword) {
        this.secondaryKeyword = secondaryKeyword;
    }

    public boolean isEnabled () {
        return enabled;
    }

    public void setEnabled (boolean enabled) {
        this.enabled = enabled;
    }

    public String getContent () {
        return content;
    }

    public void setContent (String content) {
        this.content = content;
    }

    public Date getCreatedDate () {
        return createdDate;
    }

    public void setCreatedDate (Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate () {
        return modifiedDate;
    }

    public void setModifiedDate (Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;

        AutoReplyRule that = (AutoReplyRule) o;

        if (autoReplyRuleId != that.autoReplyRuleId) return false;
        if (enabled != that.enabled) return false;
        if (content != null ? !content.equals (that.content) : that.content != null) return false;
        if (createdDate != null ? !createdDate.equals (that.createdDate) : that.createdDate != null) return false;
        if (modifiedDate != null ? !modifiedDate.equals (that.modifiedDate) : that.modifiedDate != null) return false;
        if (primaryKeyword != null ? !primaryKeyword.equals (that.primaryKeyword) : that.primaryKeyword != null)
            return false;
        if (secondaryKeyword != null ? !secondaryKeyword.equals (that.secondaryKeyword) : that.secondaryKeyword != null)
            return false;

        return true;
    }

    @Override
    public int hashCode () {
        int result = autoReplyRuleId;
        result = 31 * result + (primaryKeyword != null ? primaryKeyword.hashCode () : 0);
        result = 31 * result + (secondaryKeyword != null ? secondaryKeyword.hashCode () : 0);
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + (content != null ? content.hashCode () : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode () : 0);
        result = 31 * result + (modifiedDate != null ? modifiedDate.hashCode () : 0);
        return result;
    }

    @Override
    public String toString () {
        final StringBuffer sb = new StringBuffer ();
        sb.append ("AutoReplyRule");
        sb.append ("{autoReplyRuleId=").append (autoReplyRuleId);
        sb.append (", primaryKeyword='").append (primaryKeyword).append ('\'');
        sb.append (", secondaryKeyword='").append (secondaryKeyword).append ('\'');
        sb.append (", enabled=").append (enabled);
        sb.append (", content='").append (content).append ('\'');
        sb.append (", createdDate=").append (createdDate);
        sb.append (", modifiedDate=").append (modifiedDate);
        sb.append ('}');
        return sb.toString ();
    }
}
