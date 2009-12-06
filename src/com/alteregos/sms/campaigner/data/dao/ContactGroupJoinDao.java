package com.alteregos.sms.campaigner.data.dao;

import com.alteregos.sms.campaigner.data.dto.ContactGroupJoin;
import com.alteregos.sms.campaigner.data.dto.ContactGroupJoinPk;

import com.alteregos.sms.campaigner.data.dto.Group;
import java.util.List;

/**
 * @author john.emmanuel
 */
public interface ContactGroupJoinDao {
    ContactGroupJoin findJoin (int contactId, int groupId);
    List<ContactGroupJoin> findAll ();
    List<ContactGroupJoin> findByGroup(int groupId);
    ContactGroupJoinPk insert(ContactGroupJoin join);
    List<ContactGroupJoinPk> insert(List<ContactGroupJoin> joins);
    void delete(ContactGroupJoin join);
    void delete(List<ContactGroupJoin> joins);
    void delete(Group group);
}
