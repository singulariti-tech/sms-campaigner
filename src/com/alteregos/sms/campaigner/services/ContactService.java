package com.alteregos.sms.campaigner.services;

import com.alteregos.sms.campaigner.data.dao.ContactDao;
import com.alteregos.sms.campaigner.data.dao.ContactGroupJoinDao;
import com.alteregos.sms.campaigner.data.dao.GroupDao;
import com.alteregos.sms.campaigner.data.dao.MultipleTableJoinDao;
import com.alteregos.sms.campaigner.data.dto.Contact;
import com.alteregos.sms.campaigner.data.dto.ContactGroupJoin;
import com.alteregos.sms.campaigner.data.dto.Group;
import com.alteregos.sms.campaigner.data.exceptions.DaoException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author john.emmanuel
 * @version
 * @since
 *
 */
public class ContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactService.class);
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
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        int contactId = 0;
        try {
            contactId = contactDao.insert(contact);
        } catch (Exception e) {
            LOGGER.error("-- newContact(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
        return contactId;
    }

    public void updateContact(Contact contact) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            contactDao.update(contact);
        } catch (Exception e) {
            LOGGER.error("-- updateContact(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
    }

    public void updateContacts(List<Contact> contacts) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            contactDao.update(contacts);
        } catch (Exception e) {
            LOGGER.error("-- updateContacts(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
    }

    public void deleteContact(Contact contact) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            contactDao.delete(contact);
        } catch (Exception e) {
            LOGGER.error("-- deleteContact(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
    }

    public void deleteContacts(List<Contact> contacts) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            contactDao.delete(contacts);
        } catch (Exception e) {
            LOGGER.error("-- deleteContacts(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);

    }

    public int newGroup(Group group, List<Contact> contacts) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        List<ContactGroupJoin> joins = new ArrayList<ContactGroupJoin>();
        int groupId = 0;
        try {
            groupId = groupDao.insert(group);
            for (Contact c : contacts) {
                ContactGroupJoin join = new ContactGroupJoin();
                join.setContactId(c.getContactId());
                join.setGroupId(groupId);
                joins.add(join);
            }
            contactGroupJoinDao.insert(joins);
        } catch (Exception e) {
            LOGGER.error("-- newGroup(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
        return groupId;
    }

    public List<Group> getGroups() {
        return groupDao.findAll();
    }

    public void updateGroup(Group group, List<Contact> contacts) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        //TODO Should we synchronize or just add new contacts. As of now just adding
        List<ContactGroupJoin> joins = new ArrayList<ContactGroupJoin>();
        try {
            for (Contact c : contacts) {
                ContactGroupJoin join = new ContactGroupJoin();
                join.setContactId(c.getContactId());
                join.setGroupId(group.getGroupId());
                joins.add(join);
            }
            contactGroupJoinDao.insert(joins);
        } catch (Exception e) {
            LOGGER.error("-- updateGroup(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
    }

    public void deleteGroup(Group group) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            contactGroupJoinDao.delete(group);
            groupDao.delete(group);
        } catch (Exception e) {
            LOGGER.error("-- deleteGroup(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
    }

    public void deleteGroups(List<Group> groups) {
        throw new UnsupportedOperationException("No code written yet");
    }

    public void deleteContacts(Group group, List<Contact> contacts) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        List<ContactGroupJoin> joins = new ArrayList<ContactGroupJoin>();
        try {
            for (Contact c : contacts) {
                ContactGroupJoin join = new ContactGroupJoin();
                join.setContactId(c.getContactId());
                join.setGroupId(group.getGroupId());
                joins.add(join);
            }
            contactGroupJoinDao.delete(joins);
        } catch (Exception e) {
            LOGGER.error("-- deleteContacts(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
    }
}
