package com.alteregos.sms.campaigner.data.dto;

/**
 * Date: Oct 24, 2009
 * Time: 12:48:41 PM
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class GroupPk {

    private int groupId;

    public GroupPk(int groupId) {
        this.groupId = groupId;
    }

    public int getGroupId() {
        return groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GroupPk groupPk = (GroupPk) o;

        if (groupId != groupPk.groupId) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return groupId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("GroupPk");
        sb.append("{groupId=").append(groupId);
        sb.append('}');
        return sb.toString();
    }
}
