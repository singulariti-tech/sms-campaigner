package com.alteregos.sms.campaigner.data.mappers;

import com.alteregos.sms.campaigner.data.dto.ContactGroupJoin;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 22-Oct-2009
 * Time: 15:53:11
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class ContactGroupJoinRowMapper implements ParameterizedRowMapper<ContactGroupJoin> {

    @Override
    public ContactGroupJoin mapRow (ResultSet rs, int rowNum) throws SQLException {
        ContactGroupJoin join = new ContactGroupJoin ();
        join.setContactId (rs.getInt ("contact_id"));
        join.setGroupId (rs.getInt ("group_id"));
        return join;
    }
}
