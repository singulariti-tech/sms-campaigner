package com.alteregos.sms.campaigner.data.dto;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Date: 22-Oct-2009
 * Time: 14:51:09
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
@Service
public class Dnd {

    private int dndId;
    private String mobileNo;
    private Date registeredDate = new Date();

    public Dnd() {
    }

    public int getDndId() {
        return dndId;
    }

    public void setDndId(int dndId) {
        this.dndId = dndId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public DndPk createPk() {
        return new DndPk(dndId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Dnd dnd = (Dnd) o;

        if (dndId != dnd.dndId) {
            return false;
        }
        if (mobileNo != null ? !mobileNo.equals(dnd.mobileNo) : dnd.mobileNo != null) {
            return false;
        }
        if (registeredDate != null ? !registeredDate.equals(dnd.registeredDate) : dnd.registeredDate != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = dndId;
        result = 31 * result + (mobileNo != null ? mobileNo.hashCode() : 0);
        result = 31 * result + (registeredDate != null ? registeredDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Dnd");
        sb.append("{dndId=").append(dndId);
        sb.append(", mobileNo='").append(mobileNo).append('\'');
        sb.append(", registeredDate=").append(registeredDate);
        sb.append('}');
        return sb.toString();
    }
}
