package com.alteregos.sms.campaigner.data.sqlite;

import com.alteregos.sms.campaigner.data.dao.MultipleTableJoinDao;
import com.alteregos.sms.campaigner.data.dto.Contact;
import com.alteregos.sms.campaigner.data.dto.Group;
import com.alteregos.sms.campaigner.data.exceptions.DaoException;
import com.alteregos.sms.campaigner.data.mappers.ContactRowMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author john.emmanuel
 * @version
 * @since
 *
 */
public class SqliteMultipleTableJoinDao extends BaseSqliteDao implements MultipleTableJoinDao {

    @Override
    public List<Contact> findContacts(Group group) {
        List<Contact> contacts = new ArrayList<Contact>();
        try {
            String sql = "SELECT contact.contact_id, address, email, mobile_no, name FROM contact, contact_group_join WHERE "
                    + "contact_group_join.group_id = ? ORDER BY contact.contact_id ASC";
            contacts = jdbcTemplate.query(sql, new ContactRowMapper(), group.getGroupId());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return contacts;
    }
}
