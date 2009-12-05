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
public class ContactGroupJoin {

    private int contactId;
    private int groupId;

    public ContactGroupJoin() {
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public ContactGroupJoinPk createPk() {
        return new ContactGroupJoinPk(contactId, groupId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContactGroupJoin that = (ContactGroupJoin) o;

        if (contactId != that.contactId) {
            return false;
        }
        if (groupId != that.groupId) {
            return false;
        }

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
        final StringBuffer sb = new StringBuffer();
        sb.append("ContactGroupJoin");
        sb.append("{contactId=").append(contactId);
        sb.append(", groupId=").append(groupId);
        sb.append('}');
        return sb.toString();
    }
}
