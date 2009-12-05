package com.alteregos.sms.campaigner.data.mappers;

import com.alteregos.sms.campaigner.data.dto.Contact;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 22-Oct-2009
 * Time: 15:52:59
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class ContactRowMapper implements ParameterizedRowMapper<Contact> {

    @Override
    public Contact mapRow (ResultSet rs, int rowNum) throws SQLException {
        Contact contact = new Contact ();
        contact.setContactId (rs.getInt ("contact_id"));
        contact.setAddress (rs.getString ("address"));
        contact.setEmail (rs.getString ("email"));
        contact.setMobileNo (rs.getString ("mobile_no"));
        contact.setName (rs.getString ("name"));

        return contact;
    }
}
