package com.alteregos.sms.campaigner.data.sqlite;

import com.alteregos.sms.campaigner.data.dao.IncomingMessageDao;
import com.alteregos.sms.campaigner.data.dto.IncomingMessage;
import com.alteregos.sms.campaigner.data.exceptions.DaoException;
import com.alteregos.sms.campaigner.data.mappers.IncomingMessageRowMapper;
import java.util.ArrayList;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Date: 23-Oct-2009
 * Time: 17:18:42
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class SqliteIncomingMessageDao extends BaseSqliteDao implements IncomingMessageDao {

    private final String TABLE_NAME = "incoming_message";
    private final String DEFAULT_SELECTORS = " incoming_message_id, content, encoding, gateway_id, message_date, " + "receipt_date, process, sender_no, message_type ";
    private String findByIdQuery;
    private String findAllQuery;
    private String insertStmt;
    private String updateStmt;

    public SqliteIncomingMessageDao() {
        findByIdQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE incoming_message_id = ? " + "ORDER BY incoming_message_id ASC";
        findAllQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " ORDER BY incoming_message_id ASC";
        insertStmt = "INSERT INTO " + TABLE_NAME + "(" + DEFAULT_SELECTORS + ") VALUES(?,?,?,?,?,?,?,?,?)";
        updateStmt = "UPDATE " + TABLE_NAME + "SET content = ?, encoding = ?, gateway_id = ?, " + "message_date = ?, receipt_date = ?, process = ?, sender_no = ?, message_type = ? WHERE " + "incoming_message_id = ?";
    }

    @Override
    public IncomingMessage findById(int incomingMessageId) {
        IncomingMessage message;
        try {
            message = jdbcTemplate.queryForObject(findByIdQuery, new IncomingMessageRowMapper(), incomingMessageId);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return message;
    }

    @Override
    public List<IncomingMessage> findAll() {
        List<IncomingMessage> messages;
        try {
            messages = jdbcTemplate.query(findAllQuery, new IncomingMessageRowMapper());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return messages;
    }

    @Override
    public synchronized int insert(IncomingMessage m) {
        int lastId;
        try {
            jdbcTemplate.update(insertStmt, null, m.getContent(), m.getEncoding().toString(), m.getGatewayId(),
                    m.getMessageDate(), m.getReceiptDate(), m.isProcess(), m.getSenderNo(), m.getType().toString());
            lastId = getLastInsertedId();
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return lastId;
    }

    @Override
    public synchronized int[] insert(List<IncomingMessage> messages) {
        int[] counts = null;
        try {
            List<Object[]> batch = new ArrayList<Object[]>();
            for (IncomingMessage m : messages) {
                Object[] oa = new Object[]{
                    null, m.getContent(), m.getEncoding().toString(), m.getGatewayId(),
                    m.getMessageDate(), m.getReceiptDate(), m.isProcess(), m.getSenderNo(), m.getType().toString()
                };
                batch.add(oa);
            }
            counts = jdbcTemplate.batchUpdate(insertStmt, batch);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException(e);
        }
        return counts;
    }

    @Override
    public synchronized void update(IncomingMessage m) {
        try {
            jdbcTemplate.update(updateStmt, m.getContent(), m.getEncoding().toString(), m.getGatewayId(),
                    m.getMessageDate(), m.getReceiptDate(), m.isProcess(), m.getSenderNo(), m.getType().toString(),
                    m.getIncomingMessageId());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int[] update(List<IncomingMessage> messages) {
        int[] counts = null;
        try {
            List<Object[]> batch = new ArrayList<Object[]>();
            for (IncomingMessage m : messages) {
                Object[] oa = new Object[]{
                    m.getContent(), m.getEncoding().toString(), m.getGatewayId(),
                    m.getMessageDate(), m.getReceiptDate(), m.isProcess(), m.getSenderNo(),
                    m.getType().toString(), m.getIncomingMessageId()
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
