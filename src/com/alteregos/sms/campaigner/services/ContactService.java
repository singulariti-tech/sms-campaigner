package com.alteregos.sms.campaigner.services;

import com.alteregos.sms.campaigner.data.dao.ContactDao;
import com.alteregos.sms.campaigner.data.dao.GroupDao;
import com.alteregos.sms.campaigner.data.dto.Contact;
import com.alteregos.sms.campaigner.data.dto.Group;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author john.emmanuel
 * @version
 * @since
 *
 */
@Service("contactService")
public class ContactService {

    @Autowired(required = true)
    @Qualifier("sqliteContactDao")
    private ContactDao contactDao;
    @Autowired(required = true)
    @Qualifier("sqliteGroupDao")
    private GroupDao groupDao;

    public ContactService() {
    }
    
    public void setContactDao(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public int newContact(Contact contact) {
        return contactDao.insert(contact).getContactId();
    }

    public List<Contact> getContacts() {
        return contactDao.findAll();
    }

    public int newGroup(Group group) {
        return groupDao.insert(group).getGroupId();
    }

    public List<Group> getGroups() {
        return groupDao.findAll();
    }
}
