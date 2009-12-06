package com.alteregos.sms.campaigner.data.mappers;

import com.alteregos.sms.campaigner.business.Encoding;
import com.alteregos.sms.campaigner.business.MessagePriority;
import com.alteregos.sms.campaigner.business.MessageStatus;
import com.alteregos.sms.campaigner.business.OutgoingMessageType;
import com.alteregos.sms.campaigner.data.dto.OutgoingMessage;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 22-Oct-2009
 * Time: 15:55:05
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class OutgoingMessageRowMapper implements ParameterizedRowMapper<OutgoingMessage> {

    @Override
    public OutgoingMessage mapRow (ResultSet rs, int rowNum) throws SQLException {
        OutgoingMessage message = new OutgoingMessage ();
        message.setOutgoingMessageId (rs.getInt ("outgoing_message_id"));
        message.setContent (rs.getString ("content"));
        message.setCreatedDate (rs.getTimestamp ("created_date"));
        message.setSentDate (rs.getTimestamp ("sent_date"));
        message.setErrors (rs.getInt ("errors"));
        message.setGatewayId (rs.getString ("gateway_id"));
        message.setDstPort (rs.getInt ("dst_port"));
        message.setSrcPort (rs.getInt ("src_port"));
        message.setRecepientNo (rs.getString ("recepient_no"));
        message.setSenderNo (rs.getString ("sender_no"));
        message.setStatusReport (rs.getBoolean ("status_report"));
        message.setStatus (MessageStatus.getStatus (rs.getString ("status")));
        message.setEncoding (Encoding.getEncoding (rs.getString ("encoding")));
        message.setFlashMessage (rs.getBoolean ("flash_message"));
        message.setPriority (MessagePriority.getPriority (rs.getString ("priority")));
        message.setReferenceNo (rs.getString ("ref_no"));
        message.setType (OutgoingMessageType.getType (rs.getString ("message_type")));

        return message;
    }
}
