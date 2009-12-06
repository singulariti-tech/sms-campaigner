package com.alteregos.sms.campaigner.data.dao;

import com.alteregos.sms.campaigner.data.dto.AutoReplyRule;

import java.util.List;

/**
 * @author john.emmanuel
 */
public interface AutoReplyRuleDao {
    AutoReplyRule findById (int autoReplyRuleId);
    List<AutoReplyRule> findAll ();
    List<AutoReplyRule> findEnabled ();
    int insert(AutoReplyRule rule);
    void update(AutoReplyRule rule);
    void update(List<AutoReplyRule> rules);
}
