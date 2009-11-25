package com.alteregos.sms.campaigner.data.dto;

import org.springframework.stereotype.Service;

/**
 * Date: 22-Oct-2009
 * Time: 14:51:09
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */

@Service
public class Group {
    private int groupId;
    private String name;

    public Group () {
    }

    public int getGroupId () {
        return groupId;
    }

    public void setGroupId (int groupId) {
        this.groupId = groupId;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;

        Group group = (Group) o;

        if (groupId != group.groupId) return false;
        if (name != null ? !name.equals (group.name) : group.name != null) return false;

        return true;
    }

    @Override
    public int hashCode () {
        int result = groupId;
        result = 31 * result + (name != null ? name.hashCode () : 0);
        return result;
    }

    @Override
    public String toString () {
        final StringBuffer sb = new StringBuffer ();
        sb.append ("Group");
        sb.append ("{groupId=").append (groupId);
        sb.append (", name='").append (name).append ('\'');
        sb.append ('}');
        return sb.toString ();
    }
}
