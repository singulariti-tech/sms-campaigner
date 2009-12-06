package com.alteregos.sms.campaigner.services;

import com.alteregos.sms.campaigner.data.dao.AutoReplyRuleDao;
import com.alteregos.sms.campaigner.data.dto.AutoReplyRule;
import java.util.List;

/**
 *
 * @author john.emmanuel
 * @version
 * @since
 *
 */
public class RuleService {

    private AutoReplyRuleDao ruleDao;

    public void setRuleDao(AutoReplyRuleDao ruleDao) {
        this.ruleDao = ruleDao;
    }

    public List<AutoReplyRule> getRules() {
        return ruleDao.findAll();
    }

    public List<AutoReplyRule> getRules(boolean enabled) {
        List<AutoReplyRule> rules = null;
        if (enabled) {
            rules = ruleDao.findEnabled();
        } else {
            rules = getRules();
        }
        return rules;
    }

    public int newRule(AutoReplyRule rule) {
        return ruleDao.insert(rule);
    }

    public void update(AutoReplyRule rule) {
        ruleDao.update(rule);
    }

    public int[] update(List<AutoReplyRule> rules) {
        return ruleDao.update(rules);
    }

    public void delete(AutoReplyRule rule) {
        ruleDao.delete(rule);
    }

    public void delete(List<AutoReplyRule> rules) {
        ruleDao.delete(rules);
    }
}
