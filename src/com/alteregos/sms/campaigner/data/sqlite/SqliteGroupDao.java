package com.alteregos.sms.campaigner.data.sqlite;

import com.alteregos.sms.campaigner.data.dao.GroupDao;
import com.alteregos.sms.campaigner.data.dto.Group;
import com.alteregos.sms.campaigner.data.exceptions.DaoException;
import com.alteregos.sms.campaigner.data.mappers.GroupRowMapper;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Date: 23-Oct-2009
 * Time: 16:51:39
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class SqliteGroupDao extends BaseSqliteDao implements GroupDao {

    private final String TABLE_NAME = "contact_group";
    private final String DEFAULT_SELECTORS = " group_id, name ";
    private String findByIdQuery;
    private String findAllQuery;
    private String insertStmt;
    private String updateStmt;
    private String deleteStmt;

    public SqliteGroupDao() {
        findByIdQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE group_id = ?";
        findAllQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " ORDER BY group_id ASC";
        insertStmt = "INSERT INTO " + TABLE_NAME + " (" + DEFAULT_SELECTORS + ") VALUES(?, ?)";
        updateStmt = "UPDATE " + TABLE_NAME + " SET  name = ? WHERE group_id = ?";
        deleteStmt = "DELETE FROM " + TABLE_NAME + " WHERE group_id = ?";
    }

    @Override
    public Group findById(int contactGroupId) {
        Group group;
        try {
            group = jdbcTemplate.queryForObject(findByIdQuery, new GroupRowMapper(), contactGroupId);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return group;
    }

    @Override
    public List<Group> findAll() {
        List<Group> groups;
        try {
            groups = jdbcTemplate.query(findAllQuery, new GroupRowMapper());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return groups;
    }

    @Override
    public synchronized int insert(Group group) {
        int lastId;
        try {
            jdbcTemplate.update(insertStmt, null, group.getName());
            lastId = getLastInsertedId();
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return lastId;
    }

    @Override
    public synchronized void update(Group group) {
        try {
            jdbcTemplate.update(updateStmt, group.getName(), group.getGroupId());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Group group) {
        try {
            jdbcTemplate.update(deleteStmt, group.getGroupId());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(List<Group> groups) {
        try {
            StringBuffer deleteBulkStmt = new StringBuffer("DELETE FROM " + TABLE_NAME + " WHERE group_id in (");
            int size = groups.size();
            int lessOne = size - 1;
            for (int i = 0; i < size; i++) {
                Group r = groups.get(i);
                deleteBulkStmt.append(r.getGroupId());
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
