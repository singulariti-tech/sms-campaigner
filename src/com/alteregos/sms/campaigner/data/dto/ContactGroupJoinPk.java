package com.alteregos.sms.campaigner.data.dto;

/**
 * Date: Oct 24, 2009
 * Time: 12:46:12 PM
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class ContactGroupJoinPk {

    private int contactId;
    private int groupId;

    public ContactGroupJoinPk(int contactId, int groupId) {
        this.contactId = contactId;
        this.groupId = groupId;
    }

    public int getContactId() {
        return contactId;
    }

    public int getGroupId() {
        return groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactGroupJoinPk that = (ContactGroupJoinPk) o;

        if (contactId != that.contactId) return false;
        if (groupId != that.groupId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = contactId;
        result = 31 * result + groupId;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ContactGroupJoinPk");
        sb.append("{contactId=").append(contactId);
        sb.append(", groupId=").append(groupId);
        sb.append('}');
        return sb.toString();
    }
}
