package com.alteregos.sms.campaigner.data.sqlite;

import com.alteregos.sms.campaigner.data.dao.IncomingCallDao;
import com.alteregos.sms.campaigner.data.dto.IncomingCall;
import com.alteregos.sms.campaigner.data.exceptions.DaoException;
import com.alteregos.sms.campaigner.data.mappers.IncomingCallRowMapper;
import java.util.ArrayList;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Date: 23-Oct-2009
 * Time: 17:05:15
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class SqliteIncomingCallDao extends BaseSqliteDao implements IncomingCallDao {

    private final String TABLE_NAME = "incoming_call";
    private final String DEFAULT_SELECTORS = " call_id, receipt_date, gateway_id, caller_no, process ";
    private String findByIdQuery;
    private String findAllQuery;
    private String insertStmt;
    private String updateStmt;

    public SqliteIncomingCallDao() {
        findByIdQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE call_id = ?";
        findAllQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " ORDER BY call_id";
        insertStmt = "INSERT INTO " + TABLE_NAME + "(" + DEFAULT_SELECTORS + ") VALUES(?,?,?,?,?)";
        updateStmt = "UPDATE " + TABLE_NAME + " SET receipt_date = ?, gateway_id = ?, caller_no = ?, "
                + "process = ? WHERE call_id = ?";
    }

    @Override
    public IncomingCall findById(int incomingCallId) {
        IncomingCall call;
        try {
            call = jdbcTemplate.queryForObject(findByIdQuery, new IncomingCallRowMapper(), incomingCallId);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return call;
    }

    @Override
    public List<IncomingCall> findAll() {
        List<IncomingCall> calls;
        try {
            calls = jdbcTemplate.query(findAllQuery, new IncomingCallRowMapper());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return calls;
    }

    @Override
    public synchronized int insert(IncomingCall call) {
        int lastId;
        try {
            jdbcTemplate.update(insertStmt, null, call.getReceiptDate(), call.getGatewayId(), call.getCallerNo(),
                    call.isProcess());
            lastId = getLastInsertedId();
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return lastId;
    }

    @Override
    public synchronized void update(IncomingCall call) {
        try {
            jdbcTemplate.update(updateStmt, call.getReceiptDate(), call.getGatewayId(), call.getCallerNo(),
                    call.isProcess(), call.getIncomingCallId());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int[] update(List<IncomingCall> calls) {
        int[] counts = null;
        try {
            List<Object[]> batch = new ArrayList<Object[]>();
            for (IncomingCall call : calls) {
                Object[] oa = new Object[]{
                    call.getReceiptDate(), call.getGatewayId(), call.getCallerNo(),
                    call.isProcess(), call.getIncomingCallId()
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
}
