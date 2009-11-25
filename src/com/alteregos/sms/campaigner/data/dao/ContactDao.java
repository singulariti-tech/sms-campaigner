package com.alteregos.sms.campaigner.data.dao;

import com.alteregos.sms.campaigner.data.dto.Contact;
import com.alteregos.sms.campaigner.data.dto.ContactPk;

import java.util.List;

/**
 * @author john.emmanuel
 */
public interface ContactDao {
    Contact findById (int contactId);
    List<Contact> findAll ();
    List<Contact> findByMobileNo (String mobileNo);
    ContactPk insert(Contact contact);
    void update(ContactPk pk, Contact contact);
}
