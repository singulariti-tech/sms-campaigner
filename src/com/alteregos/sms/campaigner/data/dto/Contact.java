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
public class Contact {

    private int contactId;
    private String name;
    private String mobileNo;
    private String email;
    private String address;

    public Contact() {
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ContactPk createPk() {
        return new ContactPk(contactId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Contact contact = (Contact) o;

        if (contactId != contact.contactId) {
            return false;
        }
        if (address != null ? !address.equals(contact.address) : contact.address != null) {
            return false;
        }
        if (email != null ? !email.equals(contact.email) : contact.email != null) {
            return false;
        }
        if (mobileNo != null ? !mobileNo.equals(contact.mobileNo) : contact.mobileNo != null) {
            return false;
        }
        if (name != null ? !name.equals(contact.name) : contact.name != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = contactId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (mobileNo != null ? mobileNo.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Contact");
        sb.append("{contactId=").append(contactId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", mobileNo='").append(mobileNo).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
