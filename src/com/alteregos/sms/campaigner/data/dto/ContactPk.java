package com.alteregos.sms.campaigner.data.dto;

/**
 * Date: Oct 24, 2009
 * Time: 12:45:13 PM
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class ContactPk {

    private int contactId;

    public ContactPk(int contactId) {
        this.contactId = contactId;
    }

    public int getContactId() {
        return contactId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactPk contactPk = (ContactPk) o;

        if (contactId != contactPk.contactId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return contactId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ContactPk");
        sb.append("{contactId=").append(contactId);
        sb.append('}');
        return sb.toString();
    }
}
