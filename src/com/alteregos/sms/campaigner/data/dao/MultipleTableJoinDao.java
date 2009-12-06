package com.alteregos.sms.campaigner.data.dao;

import com.alteregos.sms.campaigner.data.dto.Contact;
import com.alteregos.sms.campaigner.data.dto.Group;
import java.util.List;

/**
 *
 * @author john.emmanuel
 * @version
 * @since
 */
public interface MultipleTableJoinDao {
    List<Contact> findContacts(Group group);
}
