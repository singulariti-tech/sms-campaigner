package com.alteregos.sms.campaigner.data.sqlite;

import com.alteregos.sms.campaigner.data.dao.IncomingMessageDao;
import com.alteregos.sms.campaigner.data.dto.IncomingMessage;
import com.alteregos.sms.campaigner.data.dto.IncomingMessagePk;
import com.alteregos.sms.campaigner.data.exceptions.DaoException;
import com.alteregos.sms.campaigner.data.mappers.IncomingMessageRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 23-Oct-2009
 * Time: 17:18:42
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
@Repository("sqliteIncomingMessageDao")
public class SqliteIncomingMessageDao extends BaseSqliteDao implements IncomingMessageDao {

    private final String TABLE_NAME = "incoming_message";
    private final String DEFAULT_SELECTORS = " incoming_message_id, content, encoding, gateway_id, message_date, " +
            "receipt_date, process, sender_no, type ";

    private String findByIdQuery;
    private String findAllQuery;
    private String insertStmt;
    private String updateStmt;

    public SqliteIncomingMessageDao () {
        findByIdQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE incoming_message_id = ? " +
                "ORDER BY incoming_message_id ASC";
        findAllQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " ORDER BY incoming_message_id ASC";
        insertStmt = "INSERT INTO " + TABLE_NAME + "(" + DEFAULT_SELECTORS + ") VALUES(?,?,?,?,?,?,?,?,?)";
        updateStmt = "UPDATE " + TABLE_NAME + "SET content = ?, encoding = ?, gateway_id = ?, " +
                "message_date = ?, receipt_date = ?, process = ?, sender_no = ?, type = ? WHERE " +
                "incoming_message_id = ?";
    }

    public IncomingMessage findById (int incomingMessageId) {
        IncomingMessage message;
        try {
            message = jdbcTemplate.queryForObject (findByIdQuery, new IncomingMessageRowMapper (), incomingMessageId);
        } catch (DataAccessException e) {
            throw new DaoException (e);
        }
        return message;
    }

    public List<IncomingMessage> findAll () {
        List<IncomingMessage> messages;
        try {
            messages = jdbcTemplate.query (findAllQuery, new IncomingMessageRowMapper ());
        } catch (DataAccessException e) {
            throw new DaoException (e);
        }
        return messages;
    }

    public IncomingMessagePk insert (IncomingMessage m) {
        int lastId;
        try {
            jdbcTemplate.update (insertStmt, null, m.getContent (), m.getEncoding (), m.getGatewayId (),
                    m.getMessageDate (), m.getReceiptDate (), m.isProcess (), m.getSenderNo (), m.getType ());
            lastId = getLastInsertedId ();
        } catch (DataAccessException e) {
            throw new DaoException (e);
        }
        return new IncomingMessagePk (lastId);
    }

    public void update (IncomingMessagePk pk, IncomingMessage m) {
        try {
            jdbcTemplate.update (updateStmt, m.getContent (), m.getEncoding (), m.getGatewayId (),
                    m.getMessageDate (), m.getReceiptDate (), m.isProcess (), m.getSenderNo (), m.getType (),
                    m.getIncomingMessageId ());
        } catch (DataAccessException e) {
            throw new DaoException (e);
        }
    }
}
