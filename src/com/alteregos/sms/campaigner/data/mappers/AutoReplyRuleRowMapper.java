package com.alteregos.sms.campaigner.data.mappers;

import com.alteregos.sms.campaigner.data.dto.AutoReplyRule;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 22-Oct-2009
 * Time: 15:52:51
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class AutoReplyRuleRowMapper implements ParameterizedRowMapper<AutoReplyRule> {

    public AutoReplyRule mapRow (ResultSet rs, int rowNum) throws SQLException {
        AutoReplyRule rule = new AutoReplyRule ();
        rule.setAutoReplyRuleId (rs.getInt ("auto_reply_rule_id"));
        rule.setContent (rs.getString ("content"));
        rule.setCreatedDate (rs.getTimestamp ("created_date"));
        rule.setEnabled (rs.getBoolean ("enabled"));
        rule.setModifiedDate (rs.getTimestamp ("modified_date"));
        rule.setPrimaryKeyword (rs.getString ("primary_keyword"));
        rule.setSecondaryKeyword (rs.getString ("secondary_keyword"));

        return rule;
    }
}
