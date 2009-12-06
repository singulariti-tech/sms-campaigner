package com.alteregos.sms.campaigner.data.sqlite;

import com.alteregos.sms.campaigner.data.dao.ContactGroupJoinDao;
import com.alteregos.sms.campaigner.data.dto.ContactGroupJoin;
import com.alteregos.sms.campaigner.data.dto.ContactGroupJoinPk;
import com.alteregos.sms.campaigner.data.dto.Group;
import com.alteregos.sms.campaigner.data.exceptions.DaoException;
import com.alteregos.sms.campaigner.data.mappers.ContactGroupJoinRowMapper;
import java.util.ArrayList;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Date: 23-Oct-2009
 * Time: 15:56:55
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class SqliteContactGroupJoinDao extends BaseSqliteDao implements ContactGroupJoinDao {

    private final String TABLE_NAME = "contact_group_join";
    private final String DEFAULT_SELECTORS = " contact_id, group_id ";
    private String findAllQuery;
    private String findJoinQuery;
    private String findByGroupQuery;
    private String insertStmt;
    private String updateStmt;
    private String deleteStmt;

    public SqliteContactGroupJoinDao() {
        findJoinQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE contact_id = ? " + "AND group_id = ? ORDER BY group_id ASC, contact_id ASC";
        findAllQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " ORDER BY group_id ASC, " + "contact_id ASC";
        findByGroupQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE group_id = ? " + "ORDER BY contact_id ASC";
        insertStmt = "INSERT INTO " + TABLE_NAME + "(" + DEFAULT_SELECTORS + ") VALUES(?,?)";
        deleteStmt = "DELETE FROM " + TABLE_NAME + " WHERE contact_id = ? AND group_id = ?";
    }

    @Override
    public ContactGroupJoin findJoin(int contactId, int groupId) {
        ContactGroupJoin join;
        try {
            join = jdbcTemplate.queryForObject(findJoinQuery, new ContactGroupJoinRowMapper(), contactId, groupId);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return join;
    }

    @Override
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

    @Override
    public ContactGroupJoinPk insert(ContactGroupJoin join) {
        try {
            jdbcTemplate.update(insertStmt, join.getContactId(), join.getGroupId());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return join.createPk();
    }

    @Override
    public List<ContactGroupJoinPk> insert(List<ContactGroupJoin> joins) {
        List<ContactGroupJoinPk> pks = new ArrayList<ContactGroupJoinPk>();
        try {
            List<Object[]> batch = new ArrayList<Object[]>();
            for (ContactGroupJoin join : joins) {
                Object[] oa = new Object[]{
                    join.getContactId(), join.getGroupId()
                };
                batch.add(oa);
            }
            int[] counts = jdbcTemplate.batchUpdate(insertStmt, batch);
            for (ContactGroupJoin join : joins) {
                pks.add(join.createPk());
            }
            //TODO Verify update counts
        } catch (DataAccessException e) {
            throw new DaoException(e);
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return pks;
    }

    @Override
    public void delete(ContactGroupJoin join) {
        try {
            jdbcTemplate.update(deleteStmt, join.getContactId(), join.getGroupId());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(List<ContactGroupJoin> joins) {
        try {
            int[] counts = null;
            List<Object[]> batch = new ArrayList<Object[]>();
            for (ContactGroupJoin join : joins) {
                Object[] oa = new Object[]{
                    join.getContactId(), join.getGroupId()
                };
                batch.add(oa);
            }
            counts = jdbcTemplate.batchUpdate(deleteStmt, batch);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Group group) {
        try {
            String deleteGroupStmt = "DELETE FROM " + TABLE_NAME + " WHERE group_id = ?";
            jdbcTemplate.update(deleteGroupStmt, group.getGroupId());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }
}
