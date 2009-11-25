package com.alteregos.sms.campaigner.data.sqlite;

import com.alteregos.sms.campaigner.data.dao.ContactGroupJoinDao;
import com.alteregos.sms.campaigner.data.dto.ContactGroupJoin;
import com.alteregos.sms.campaigner.data.dto.ContactGroupJoinPk;
import com.alteregos.sms.campaigner.data.exceptions.DaoException;
import com.alteregos.sms.campaigner.data.mappers.ContactGroupJoinRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 23-Oct-2009
 * Time: 15:56:55
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
@Repository("sqliteContactGroupJoinDao")
public class SqliteContactGroupJoinDao extends BaseSqliteDao implements ContactGroupJoinDao {

    private final String TABLE_NAME = "contact_group_join";
    private final String DEFAULT_SELECTORS = " contact_id, group_id ";

    private String findAllQuery;
    private String findJoinQuery;
    private String findByGroupQuery;
    private String insertStmt;
    private String updateStmt;

    public SqliteContactGroupJoinDao() {
        findJoinQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE contact_id = ? " +
                "AND group_id = ? ORDER BY group_id ASC, contact_id ASC";
        findAllQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " ORDER BY group_id ASC, " +
                "contact_id ASC";
        findByGroupQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE group_id = ? " +
                "ORDER BY contact_id ASC";
        insertStmt = "INSERT INTO " + TABLE_NAME + "(" + DEFAULT_SELECTORS + ") VALUES(?,?)";
    }

    public ContactGroupJoin findJoin(int contactId, int groupId) {
        ContactGroupJoin join;
        try {
            join = jdbcTemplate.queryForObject(findJoinQuery, new ContactGroupJoinRowMapper(), contactId, groupId);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return join;
    }

    public List<ContactGroupJoin> findAll() {
        List<ContactGroupJoin> joins;
        try {
            joins = jdbcTemplate.query(findAllQuery, new ContactGroupJoinRowMapper());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return joins;
    }

    @Override
    public List<ContactGroupJoin> findByGroup(int groupId) {
        List<ContactGroupJoin> joins;
        try {
            joins = jdbcTemplate.query(findByGroupQuery, new ContactGroupJoinRowMapper(), groupId);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return joins;
    }

    public ContactGroupJoinPk insert(ContactGroupJoin join) {
        try {
            jdbcTemplate.update(insertStmt, join.getContactId(), join.getGroupId());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return join.createPk();
    }
}