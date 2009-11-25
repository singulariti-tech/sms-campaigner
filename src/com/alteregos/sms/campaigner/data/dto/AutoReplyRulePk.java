package com.alteregos.sms.campaigner.data.dto;

/**
 * Date: Oct 24, 2009
 * Time: 12:43:28 PM
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class AutoReplyRulePk {

    private int autoReplyRuleId;

    public AutoReplyRulePk(int autoReplyRuleId) {
        this.autoReplyRuleId = autoReplyRuleId;
    }

    public int getAutoReplyRuleId() {
        return autoReplyRuleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AutoReplyRulePk that = (AutoReplyRulePk) o;

        if (autoReplyRuleId != that.autoReplyRuleId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return autoReplyRuleId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AutoReplyRulePk");
        sb.append("{autoReplyRuleId=").append(autoReplyRuleId);
        sb.append('}');
        return sb.toString();
    }
}
