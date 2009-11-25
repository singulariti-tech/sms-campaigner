package com.alteregos.sms.campaigner.data.dao;

import com.alteregos.sms.campaigner.data.dto.Group;
import com.alteregos.sms.campaigner.data.dto.GroupPk;

import java.util.List;

/**
 * @author john.emmanuel
 */
public interface GroupDao {
    Group findById (int contactGroupId);
    List<Group> findAll ();
    GroupPk insert(Group group);
    void update(GroupPk pk ,Group group);
}
