package com.alteregos.sms.campaigner.data.sqlite;

import com.alteregos.sms.campaigner.business.MessageStatus;
import com.alteregos.sms.campaigner.data.dao.OutgoingMessageDao;
import com.alteregos.sms.campaigner.data.dto.OutgoingMessage;
import com.alteregos.sms.campaigner.data.dto.OutgoingMessagePk;
import com.alteregos.sms.campaigner.data.exceptions.DaoException;
import com.alteregos.sms.campaigner.data.mappers.OutgoingMessageRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 23-Oct-2009
 * Time: 17:33:34
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
@Repository("sqliteOutgoingMessageDao")
public class SqliteOutgoingMessageDao extends BaseSqliteDao implements OutgoingMessageDao {

    private final String TABLE_NAME = "outgoing_message";
    private final String DEFAULT_SELECTORS = " outgoing_message_id, content, created_date, sent_date, errors, " +
            "gateway_id, dst_port, src_port, recepient_no, sender_no, status_report, status, encoding, " +
            "flash_message, priority, ref_no, type ";

    private String findByIdQuery;
    private String findAllQuery;
    private String findByStatus;
    private String insertStmt;
    private String updateStmt;

    public SqliteOutgoingMessageDao () {
        findByIdQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE outgoing_message_id = ? " +
                "ORDER BY outgoing_message_id ASC";
        findAllQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " ORDER BY outgoing_message_id ASC";
        findByStatus = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE status = ? " +
                "ORDER BY outgoing_message_id ASC";
        insertStmt = "INSERT INTO " + TABLE_NAME + " (" + DEFAULT_SELECTORS + ") " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        updateStmt = "UPDATE " + TABLE_NAME + " SET content = ?, created_date = ?, sent_date = ?, errors = ?, " +
                "gateway_id = ?, dst_port = ?, src_port = ?, recepient_no = ?, sender_no = ?, status_report = ?, " +
                "status = ?, encoding = ?, flash_message = ?, priority = ?, ref_no = ?, type = ? " +
                "WHERE outgoing_message_id = ?";
    }

    public OutgoingMessage findById (int outgoingMessageId) {
        OutgoingMessage message;
        try {
            message = jdbcTemplate.queryForObject (findByIdQuery, new OutgoingMessageRowMapper (), outgoingMessageId);
        } catch (DataAccessException e) {
            throw new DaoException (e);
        }
        return message;
    }

    public List<OutgoingMessage> findAll () {
        List<OutgoingMessage> messages;
        try {
            messages = jdbcTemplate.query (findAllQuery, new OutgoingMessageRowMapper ());
        } catch (DataAccessException e) {
            throw new DaoException (e);
        }
        return messages;
    }

    @Override
    public List<OutgoingMessage> findByStatus (MessageStatus status) {
        List<OutgoingMessage> messages;
        try {
            messages = jdbcTemplate.query (findByStatus, new OutgoingMessageRowMapper (), status.toString ());
        } catch (DataAccessException e) {
            throw new DaoException (e);
        }
        return messages;
    }

    public OutgoingMessagePk insert (OutgoingMessage m) {
        int lastId;
        try {
            jdbcTemplate.update (insertStmt, null, m.getContent (), m.getCreatedDate (), m.getSentDate (), m.getErrors (),
                    m.getGatewayId (), m.getDstPort (), m.getSrcPort (), m.getRecepientNo (), m.getSenderNo (),
                    m.getStatus (), m.getEncoding (), m.isFlashMessage (), m.getPriority (), m.getReferenceNo (),
                    m.getType ());
            lastId = getLastInsertedId ();
        } catch (DataAccessException e) {
            throw new DaoException (e);
        }
        return new OutgoingMessagePk (lastId);
    }

    public void update (OutgoingMessagePk pk, OutgoingMessage m) {
        try {
            jdbcTemplate.update (updateStmt, m.getContent (), m.getCreatedDate (), m.getSentDate (), m.getErrors (),
                    m.getGatewayId (), m.getDstPort (), m.getSrcPort (), m.getRecepientNo (), m.getSenderNo (),
                    m.getStatus (), m.getEncoding (), m.isFlashMessage (), m.getPriority (), m.getReferenceNo (),
                    m.getType (), m.getOutgoingMessageId ());
        } catch (DataAccessException e) {
            throw new DaoException (e);
        }
    }
}
