package com.alteregos.sms.campaigner.data.sqlite;

import com.alteregos.sms.campaigner.business.MessageStatus;
import com.alteregos.sms.campaigner.data.dao.OutgoingMessageDao;
import com.alteregos.sms.campaigner.data.dto.OutgoingMessage;
import com.alteregos.sms.campaigner.data.exceptions.DaoException;
import com.alteregos.sms.campaigner.data.mappers.OutgoingMessageRowMapper;
import java.util.ArrayList;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Date: 23-Oct-2009
 * Time: 17:33:34
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class SqliteOutgoingMessageDao extends BaseSqliteDao implements OutgoingMessageDao {

    private final String TABLE_NAME = "outgoing_message";
    private final String DEFAULT_SELECTORS = " outgoing_message_id, content, created_date, sent_date, errors, " + "gateway_id, dst_port, src_port, recepient_no, sender_no, status_report, status, encoding, " + "flash_message, priority, ref_no, message_type ";
    private String findByIdQuery;
    private String findAllQuery;
    private String findByStatus;
    private String insertStmt;
    private String updateStmt;

    public SqliteOutgoingMessageDao() {
        findByIdQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE outgoing_message_id = ? " + "ORDER BY outgoing_message_id ASC";
        findAllQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " ORDER BY outgoing_message_id ASC";
        findByStatus = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE status = ? " + "ORDER BY outgoing_message_id ASC";
        insertStmt = "INSERT INTO " + TABLE_NAME + " (" + DEFAULT_SELECTORS + ") " + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        updateStmt = "UPDATE " + TABLE_NAME + " SET content = ?, created_date = ?, sent_date = ?, errors = ?, " + "gateway_id = ?, dst_port = ?, src_port = ?, recepient_no = ?, sender_no = ?, status_report = ?, " + "status = ?, encoding = ?, flash_message = ?, priority = ?, ref_no = ?, message_type = ? " + "WHERE outgoing_message_id = ?";
    }

    @Override
    public OutgoingMessage findById(int outgoingMessageId) {
        OutgoingMessage message;
        try {
            message = jdbcTemplate.queryForObject(findByIdQuery, new OutgoingMessageRowMapper(), outgoingMessageId);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return message;
    }

    @Override
    public List<OutgoingMessage> findAll() {
        List<OutgoingMessage> messages;
        try {
            messages = jdbcTemplate.query(findAllQuery, new OutgoingMessageRowMapper());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return messages;
    }

    @Override
    public List<OutgoingMessage> findByStatus(MessageStatus status) {
        List<OutgoingMessage> messages;
        try {
            messages = jdbcTemplate.query(findByStatus, new OutgoingMessageRowMapper(), status.toString());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return messages;
    }

    @Override
    public synchronized int insert(OutgoingMessage m) {
        int lastId;
        try {
            jdbcTemplate.update(insertStmt, null, m.getContent(), m.getCreatedDate(), m.getSentDate(), m.getErrors(),
                    m.getGatewayId(), m.getDstPort(), m.getSrcPort(), m.getRecepientNo(), m.getSenderNo(),
                    m.isStatusReport(), m.getStatus().toString(), m.getEncoding().toString(), m.isFlashMessage(),
                    m.getPriority().toString(), m.getReferenceNo(), m.getType().toString());
            lastId = getLastInsertedId();
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return lastId;
    }

    @Override
    public synchronized int[] insert(List<OutgoingMessage> messages) {
        int[] counts = null;
        try {
            List<Object[]> batch = new ArrayList<Object[]>();
            for (OutgoingMessage m : messages) {
                Object[] oa = new Object[]{
                    null, m.getContent(), m.getCreatedDate(), m.getSentDate(), m.getErrors(),
                    m.getGatewayId(), m.getDstPort(), m.getSrcPort(), m.getRecepientNo(), m.getSenderNo(),
                    m.isStatusReport(), m.getStatus().toString(), m.getEncoding().toString(), m.isFlashMessage(),
                    m.getPriority().toString(), m.getReferenceNo(), m.getType().toString()
                };
                batch.add(oa);
            }
            counts = jdbcTemplate.batchUpdate(insertStmt, batch);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new DaoException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException(e);
        }
        return counts;
    }

    @Override
    public synchronized void update(OutgoingMessage m) {
        try {
            jdbcTemplate.update(updateStmt, m.getContent(), m.getCreatedDate(), m.getSentDate(), m.getErrors(),
                    m.getGatewayId(), m.getDstPort(), m.getSrcPort(), m.getRecepientNo(), m.getSenderNo(),
                    m.isStatusReport(), m.getStatus().toString(), m.getEncoding().toString(), m.isFlashMessage(),
                    m.getPriority().toString(), m.getReferenceNo(), m.getType().toString(), m.getOutgoingMessageId());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int[] update(List<OutgoingMessage> messages) {
        int[] counts = null;
        try {
            List<Object[]> batch = new ArrayList<Object[]>();
            for (OutgoingMessage m : messages) {
                Object[] oa = new Object[]{
                    m.getContent(), m.getCreatedDate(), m.getSentDate(), m.getErrors(),
                    m.getGatewayId(), m.getDstPort(), m.getSrcPort(), m.getRecepientNo(), m.getSenderNo(),
                    m.isStatusReport(), m.getStatus().toString(), m.getEncoding().toString(), m.isFlashMessage(),
                    m.getPriority().toString(), m.getReferenceNo(), m.getType().toString(),
                    m.getOutgoingMessageId()
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
