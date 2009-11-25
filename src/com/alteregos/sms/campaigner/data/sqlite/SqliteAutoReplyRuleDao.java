package com.alteregos.sms.campaigner.data.sqlite;

import com.alteregos.sms.campaigner.data.dao.AutoReplyRuleDao;
import com.alteregos.sms.campaigner.data.dto.AutoReplyRule;
import com.alteregos.sms.campaigner.data.dto.AutoReplyRulePk;
import com.alteregos.sms.campaigner.data.exceptions.DaoException;
import com.alteregos.sms.campaigner.data.mappers.AutoReplyRuleRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 23-Oct-2009
 * Time: 15:12:01
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
@Repository("sqliteAutoReplyRuleDao")
public class SqliteAutoReplyRuleDao extends BaseSqliteDao implements AutoReplyRuleDao {

    private final String TABLE_NAME = "auto_reply_rule";
    private final String DEFAULT_SELECTORS = " auto_reply_rule_id, content, created_date, enabled, modified_date, " +
            "primary_keyword, secondary_keyword ";

    private String findByIdQuery;
    private String findAllQuery;
    private String findEnabledQuery;
    private String insertStmt;
    private String updateStmt;

    public SqliteAutoReplyRuleDao () {
        findByIdQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE auto_reply_rule_id = ?";
        findAllQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " ORDER BY auto_reply_rule_id ASC";
        findEnabledQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE enabled = 1 ORDER BY " +
                "auto_reply_rule_id ASC";
        insertStmt = "INSERT INTO " + TABLE_NAME + " (" + DEFAULT_SELECTORS + ") VALUES(?,?,?,?,?,?,?)";
        updateStmt = "UPDATE " + TABLE_NAME + " SET content = ?, created_date = ?, enabled = ?, " +
                "modified_date = ?, primary_keyword = ?, secondary_keyword = ? WHERE auto_reply_rule_id = ?";
    }

    public AutoReplyRule findById (int autoReplyRuleId) {
        AutoReplyRule rule;
        try {
            rule = jdbcTemplate.queryForObject (findByIdQuery, new AutoReplyRuleRowMapper (), autoReplyRuleId);
        } catch (DataAccessException dataAccessException) {
            throw new DaoException (dataAccessException);
        }
        return rule;
    }

    public List<AutoReplyRule> findAll () {
        List<AutoReplyRule> rules;
        try {
            rules = jdbcTemplate.query (findAllQuery, new AutoReplyRuleRowMapper ());
        } catch (DataAccessException dataAccessException) {
            throw new DaoException (dataAccessException);
        }
        return rules;
    }

    public List<AutoReplyRule> findEnabled () {
        List<AutoReplyRule> rules;
        try {
            rules = jdbcTemplate.query (findEnabledQuery, new AutoReplyRuleRowMapper ());
        } catch (DataAccessException dataAccessException) {
            throw new DaoException (dataAccessException);
        }
        return rules;
    }

    public synchronized AutoReplyRulePk insert (AutoReplyRule rule) {
        int lastId = 0;
        try {
            jdbcTemplate.update (insertStmt, null, rule.getContent (), rule.getCreatedDate (),
                    rule.isEnabled (), rule.getModifiedDate (), rule.getPrimaryKeyword (), rule.getSecondaryKeyword ());
            lastId = getLastInsertedId ();
        } catch (DataAccessException e) {
            throw new DaoException (e);
        }
        return new AutoReplyRulePk (lastId);
    }

    public synchronized void update (AutoReplyRulePk pk, AutoReplyRule rule) {
        try {
            jdbcTemplate.update (updateStmt, rule.getContent (), rule.getCreatedDate (),
                    rule.isEnabled (), rule.getModifiedDate (), rule.getPrimaryKeyword (), rule.getSecondaryKeyword (),
                    rule.getAutoReplyRuleId ());
        } catch (DataAccessException e) {
            throw new DaoException (e);
        }
    }
}
