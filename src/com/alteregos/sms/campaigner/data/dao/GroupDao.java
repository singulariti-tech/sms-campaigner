package com.alteregos.sms.campaigner.data.dao;

import com.alteregos.sms.campaigner.data.dto.Group;

import java.util.List;

/**
 * @author john.emmanuel
 */
public interface GroupDao {
    Group findById (int contactGroupId);
    List<Group> findAll ();
    int insert(Group group);
    void update(Group group);
    void delete(Group group);
    void delete(List<Group> groups);
}
