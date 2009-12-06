package com.alteregos.sms.campaigner.data.sqlite;

import com.alteregos.sms.campaigner.data.dao.AutoReplyRuleDao;
import com.alteregos.sms.campaigner.data.dto.AutoReplyRule;
import com.alteregos.sms.campaigner.data.exceptions.DaoException;
import com.alteregos.sms.campaigner.data.mappers.AutoReplyRuleRowMapper;
import java.util.ArrayList;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Date: 23-Oct-2009
 * Time: 15:12:01
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class SqliteAutoReplyRuleDao extends BaseSqliteDao implements AutoReplyRuleDao {

    private final String TABLE_NAME = "auto_reply_rule";
    private final String DEFAULT_SELECTORS = " auto_reply_rule_id, content, created_date, enabled, modified_date, " + "primary_keyword, secondary_keyword ";
    private String findByIdQuery;
    private String findAllQuery;
    private String findEnabledQuery;
    private String insertStmt;
    private String updateStmt;
    private String deleteStmt;

    public SqliteAutoReplyRuleDao() {
        findByIdQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE auto_reply_rule_id = ? LIMIT 1";
        findAllQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " ORDER BY auto_reply_rule_id ASC";
        findEnabledQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE enabled = 1 ORDER BY " + "auto_reply_rule_id ASC";
        insertStmt = "INSERT INTO " + TABLE_NAME + " (" + DEFAULT_SELECTORS + ") VALUES(?,?,?,?,?,?,?)";
        updateStmt = "UPDATE " + TABLE_NAME + " SET content = ?, created_date = ?, enabled = ?, " + "modified_date = ?, primary_keyword = ?, secondary_keyword = ? WHERE auto_reply_rule_id = ?";
        deleteStmt = "DELETE FROM " + TABLE_NAME + " WHERE auto_reply_rule_id = ?";
    }

    @Override
    public AutoReplyRule findById(int autoReplyRuleId) {
        AutoReplyRule rule;
        try {
            rule = jdbcTemplate.queryForObject(findByIdQuery, new AutoReplyRuleRowMapper(), autoReplyRuleId);
        } catch (DataAccessException dataAccessException) {
            throw new DaoException(dataAccessException);
        }
        return rule;
    }

    @Override
    public List<AutoReplyRule> findAll() {
        List<AutoReplyRule> rules;
        try {
            rules = jdbcTemplate.query(findAllQuery, new AutoReplyRuleRowMapper());
        } catch (DataAccessException dataAccessException) {
            throw new DaoException(dataAccessException);
        }
        return rules;
    }

    @Override
    public List<AutoReplyRule> findEnabled() {
        List<AutoReplyRule> rules;
        try {
            rules = jdbcTemplate.query(findEnabledQuery, new AutoReplyRuleRowMapper());
        } catch (DataAccessException dataAccessException) {
            throw new DaoException(dataAccessException);
        }
        return rules;
    }

    @Override
    public synchronized int insert(AutoReplyRule rule) {
        int lastId = 0;
        try {
            jdbcTemplate.update(insertStmt, null, rule.getContent(), rule.getCreatedDate(),
                    rule.isEnabled(), rule.getModifiedDate(), rule.getPrimaryKeyword(), rule.getSecondaryKeyword());
            lastId = getLastInsertedId();
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return lastId;
    }

    @Override
    public synchronized void update(AutoReplyRule rule) {
        try {
            jdbcTemplate.update(updateStmt, rule.getContent(), rule.getCreatedDate(),
                    rule.isEnabled(), rule.getModifiedDate(), rule.getPrimaryKeyword(), rule.getSecondaryKeyword(),
                    rule.getAutoReplyRuleId());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public synchronized int[] update(List<AutoReplyRule> rules) {
        int[] counts = null;
        try {
            List<Object[]> batch = new ArrayList<Object[]>();
            for (AutoReplyRule rule : rules) {
                Object[] oa = new Object[]{
                    rule.getContent(), rule.getCreatedDate(),
                    rule.isEnabled(), rule.getModifiedDate(), rule.getPrimaryKeyword(), rule.getSecondaryKeyword(),
                    rule.getAutoReplyRuleId()
                };
                batch.add(oa);
            }
            counts = jdbcTemplate.batchUpdate(updateStmt, batch);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException(e);
        }
        return counts;
    }

    @Override
    public synchronized void delete(AutoReplyRule rule) {
        try {
            jdbcTemplate.update(deleteStmt, rule.getAutoReplyRuleId());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public synchronized void delete(List<AutoReplyRule> rules) {
        try {
            StringBuffer deleteBulkStmt = new StringBuffer("DELETE FROM " + TABLE_NAME + " WHERE auto_reply_rule_id in (");
            int size = rules.size();
            int lessOne = size - 1;
            for (int i = 0; i < size; i++) {
                AutoReplyRule r = rules.get(i);
                deleteBulkStmt.append(r.getAutoReplyRuleId());
                if (i < lessOne) {
                    deleteBulkStmt.append(", ");
                }
            }
            deleteBulkStmt.append(")");
            jdbcTemplate.update(deleteBulkStmt.toString());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }
}
