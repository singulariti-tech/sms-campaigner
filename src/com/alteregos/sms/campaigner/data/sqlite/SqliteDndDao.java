package com.alteregos.sms.campaigner.data.sqlite;

import com.alteregos.sms.campaigner.data.dao.DndDao;
import com.alteregos.sms.campaigner.data.dto.Dnd;
import com.alteregos.sms.campaigner.data.dto.DndPk;
import com.alteregos.sms.campaigner.data.exceptions.DaoException;
import com.alteregos.sms.campaigner.data.mappers.DndRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 23-Oct-2009
 * Time: 16:35:56
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class SqliteDndDao extends BaseSqliteDao implements DndDao {

    private final String TABLE_NAME = "dnd";
    private final String DEFAULT_SELECTORS = " dnd_id, mobile_no, registered_date ";
    private String findByIdQuery;
    private String findAllQuery;
    private String insertStmt;
    private String updateStmt;

    public SqliteDndDao() {
        findByIdQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE dnd_id = ? ORDER BY dnd_id ASC";
        findAllQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " ORDER BY dnd_id ASC";
        insertStmt = "INSERT INTO " + TABLE_NAME + "(" + DEFAULT_SELECTORS + ") VALUES(?, ?, ?)";
        updateStmt = "UPDATE " + TABLE_NAME + " SET mobile_no = ?, registered_date = ? WHERE dnd_id = ?";
    }

    @Override
    public Dnd findById(int dndId) {
        Dnd dnd;
        try {
            dnd = jdbcTemplate.queryForObject(findByIdQuery, new DndRowMapper(), dndId);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return dnd;
    }

    @Override
    public List<Dnd> findAll() {
        List<Dnd> dnds;
        try {
            dnds = jdbcTemplate.query(findAllQuery, new DndRowMapper());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return dnds;
    }

    @Override
    public DndPk insert(Dnd dnd) {
        int lastId;
        try {
            jdbcTemplate.update(insertStmt, null, dnd.getMobileNo(), dnd.getRegisteredDate());
            lastId = getLastInsertedId();
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return new DndPk(lastId);
    }

    @Override
    public void update(DndPk pk, Dnd dnd) {
        try {
            jdbcTemplate.update(updateStmt, dnd.getMobileNo(), dnd.getRegisteredDate(), dnd.getDndId());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }
}
