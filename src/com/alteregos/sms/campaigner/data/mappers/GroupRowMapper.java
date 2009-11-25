package com.alteregos.sms.campaigner.data.mappers;

import com.alteregos.sms.campaigner.data.dto.Group;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 22-Oct-2009
 * Time: 15:54:28
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class GroupRowMapper implements ParameterizedRowMapper<Group> {

    public Group mapRow (ResultSet rs, int rowNum) throws SQLException {
        Group group = new Group ();
        group.setGroupId (rs.getInt ("group_id"));
        group.setName (rs.getString ("name"));

        return group;
    }
}
