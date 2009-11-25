package com.alteregos.sms.campaigner.data.dao;

import com.alteregos.sms.campaigner.data.dto.AutoReplyRule;
import com.alteregos.sms.campaigner.data.dto.AutoReplyRulePk;

import java.util.List;

/**
 * @author john.emmanuel
 */
public interface AutoReplyRuleDao {
    AutoReplyRule findById (int autoReplyRuleId);
    List<AutoReplyRule> findAll ();
    List<AutoReplyRule> findEnabled ();
    AutoReplyRulePk insert(AutoReplyRule rule);
    void update(AutoReplyRulePk pk, AutoReplyRule rule);
}
