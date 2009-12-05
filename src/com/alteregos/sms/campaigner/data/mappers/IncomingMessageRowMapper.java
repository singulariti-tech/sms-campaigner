package com.alteregos.sms.campaigner.data.mappers;

import com.alteregos.sms.campaigner.business.Encoding;
import com.alteregos.sms.campaigner.business.IncomingMessageType;
import com.alteregos.sms.campaigner.data.dto.IncomingMessage;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 22-Oct-2009
 * Time: 15:54:55
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class IncomingMessageRowMapper implements ParameterizedRowMapper<IncomingMessage> {

    @Override
    public IncomingMessage mapRow (ResultSet rs, int rowNum) throws SQLException {
        IncomingMessage message = new IncomingMessage ();
        message.setIncomingMessageId (rs.getInt ("incoming_message_id"));
        message.setContent (rs.getString ("content"));
        message.setEncoding (Encoding.getEncoding (rs.getString ("encoding")));
        message.setGatewayId (rs.getString ("gateway_id"));
        message.setMessageDate (rs.getTimestamp ("message_date"));
        message.setReceiptDate (rs.getTimestamp ("receipt_date"));
        message.setProcess (rs.getBoolean ("process"));
        message.setSenderNo (rs.getString ("sender_no"));
        message.setType (IncomingMessageType.getType (rs.getString ("type")));

        return message;
    }
}
