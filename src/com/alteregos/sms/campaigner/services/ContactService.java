package com.alteregos.sms.campaigner.services;

import com.alteregos.sms.campaigner.data.dao.ContactDao;
import com.alteregos.sms.campaigner.data.dao.ContactGroupJoinDao;
import com.alteregos.sms.campaigner.data.dao.GroupDao;
import com.alteregos.sms.campaigner.data.dao.MultipleTableJoinDao;
import com.alteregos.sms.campaigner.data.dto.Contact;
import com.alteregos.sms.campaigner.data.dto.ContactGroupJoin;
import com.alteregos.sms.campaigner.data.dto.Group;
import java.util.ArrayList;
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
    private ContactGroupJoinDao contactGroupJoinDao;
    private MultipleTableJoinDao multipleTableJoinDao;
    private PlatformTransactionManager transactionManager;

    public ContactService() {
    }

    public void setContactDao(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public void setContactGroupJoinDao(ContactGroupJoinDao contactGroupJoinDao) {
        this.contactGroupJoinDao = contactGroupJoinDao;
    }

    public void setMultipleTableJoinDao(MultipleTableJoinDao multipleTableJoinDao) {
        this.multipleTableJoinDao = multipleTableJoinDao;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public List<Contact> getContacts() {
        return contactDao.findAll();
    }

    public List<Contact> getContacts(Group group) {
        return multipleTableJoinDao.findContacts(group);
    }

    public int newContact(Contact contact) {
        return contactDao.insert(contact);
    }

    public void updateContact(Contact contact) {
        contactDao.update(contact);
    }

    public void updateContacts(List<Contact> contacts) {
        contactDao.update(contacts);
    }

    public void deleteContact(Contact contact) {
        contactDao.delete(contact);
    }

    public void deleteContacts(List<Contact> contacts) {
        contactDao.delete(contacts);
    }

    public int newGroup(Group group, List<Contact> contacts) {
        List<ContactGroupJoin> joins = new ArrayList<ContactGroupJoin>();
        int groupId = groupDao.insert(group);
        for (Contact c : contacts) {
            ContactGroupJoin join = new ContactGroupJoin();
            join.setContactId(c.getContactId());
            join.setGroupId(groupId);
            joins.add(join);
        }
        contactGroupJoinDao.insert(joins);
        return groupId;
    }

    public List<Group> getGroups() {
        return groupDao.findAll();
    }

    public void updateGroup(Group group, List<Contact> contacts) {
        //TODO Should we synchronize or just add new contacts. As of now just adding
        List<ContactGroupJoin> joins = new ArrayList<ContactGroupJoin>();
        for (Contact c : contacts) {
            ContactGroupJoin join = new ContactGroupJoin();
            join.setContactId(c.getContactId());
            join.setGroupId(group.getGroupId());
            joins.add(join);
        }
        contactGroupJoinDao.insert(joins);
    }

    public void deleteGroup(Group group) {
        contactGroupJoinDao.delete(group);
        groupDao.delete(group);
    }

    public void deleteGroups(List<Group> groups) {
        throw new UnsupportedOperationException("No code written yet");
    }

    public void deleteContacts(Group group, List<Contact> contacts) {
        List<ContactGroupJoin> joins = new ArrayList<ContactGroupJoin>();
        for (Contact c : contacts) {
            ContactGroupJoin join = new ContactGroupJoin();
            join.setContactId(c.getContactId());
            join.setGroupId(group.getGroupId());
            joins.add(join);
        }
        contactGroupJoinDao.delete(joins);
    }
}
