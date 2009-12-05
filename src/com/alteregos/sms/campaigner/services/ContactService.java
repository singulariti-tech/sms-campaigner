package com.alteregos.sms.campaigner.services;

import com.alteregos.sms.campaigner.data.dao.ContactDao;
import com.alteregos.sms.campaigner.data.dao.GroupDao;
import com.alteregos.sms.campaigner.data.dto.Contact;
import com.alteregos.sms.campaigner.data.dto.Group;
import java.util.List;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * @author john.emmanuel
 * @version
 * @since
 *
 */
public class ContactService {

    private ContactDao contactDao;
    private GroupDao groupDao;
    private PlatformTransactionManager transactionManager;

    public ContactService() {
    }

    public void setContactDao(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
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
