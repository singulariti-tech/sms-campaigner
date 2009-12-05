package com.alteregos.sms.campaigner.data.mappers;

import com.alteregos.sms.campaigner.data.dto.IncomingCall;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 22-Oct-2009
 * Time: 15:54:42
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class IncomingCallRowMapper implements ParameterizedRowMapper<IncomingCall> {

    @Override
    public IncomingCall mapRow (ResultSet rs, int rowNum) throws SQLException {
        IncomingCall call = new IncomingCall ();
        call.setIncomingCallId (rs.getInt ("call_id"));
        call.setGatewayId (rs.getString ("gateway_id"));
        call.setCallerNo (rs.getString ("caller_no"));
        call.setProcess (rs.getBoolean ("process"));
        call.setReceiptDate (rs.getTimestamp ("receipt_date"));

        return call;
    }
}
