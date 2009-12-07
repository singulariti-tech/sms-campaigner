package com.alteregos.sms.campaigner.services;

import com.alteregos.sms.campaigner.data.dao.AutoReplyRuleDao;
import com.alteregos.sms.campaigner.data.dto.AutoReplyRule;
import java.util.List;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author john.emmanuel
 * @version
 * @since
 *
 */
public class RuleService {

    private AutoReplyRuleDao ruleDao;
    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

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
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        int ruleId = 0;
        try {
            ruleId = ruleDao.insert(rule);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
        transactionManager.commit(transactionStatus);
        return ruleId;
    }

    public void update(AutoReplyRule rule) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            ruleDao.update(rule);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
        transactionManager.commit(transactionStatus);
    }

    public int[] update(List<AutoReplyRule> rules) {
        int[] counts = null;
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            counts = ruleDao.update(rules);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
        transactionManager.commit(transactionStatus);
        return counts;
    }

    public void delete(AutoReplyRule rule) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            ruleDao.delete(rule);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
        transactionManager.commit(transactionStatus);
    }

    public void delete(List<AutoReplyRule> rules) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            ruleDao.delete(rules);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
        transactionManager.commit(transactionStatus);
    }
}
