package com.alteregos.sms.campaigner.data.dao;

import com.alteregos.sms.campaigner.data.dto.Contact;

import java.util.List;

/**
 * @author john.emmanuel
 */
public interface ContactDao {
    Contact findById (int contactId);
    List<Contact> findAll ();
    List<Contact> findByMobileNo (String mobileNo);
    int insert(Contact contact);
    void update(Contact contact);
    void update(List<Contact> contacts);
    void delete(Contact contact);
    void delete(List<Contact> contacts);
}
