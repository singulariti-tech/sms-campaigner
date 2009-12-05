package com.alteregos.sms.campaigner.data.mappers;

import com.alteregos.sms.campaigner.data.dto.Dnd;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 22-Oct-2009
 * Time: 15:53:25
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class DndRowMapper implements ParameterizedRowMapper<Dnd> {

    @Override
    public Dnd mapRow (ResultSet rs, int rowNum) throws SQLException {
        Dnd dnd = new Dnd ();
        dnd.setDndId (rs.getInt ("dnd_id"));
        dnd.setMobileNo (rs.getString ("mobile_no"));
        dnd.setRegisteredDate (rs.getTimestamp ("registered_date"));

        return dnd;
    }
}
