package com.alteregos.sms.campaigner.data.dto;

/**
 * Date: Oct 24, 2009
 * Time: 12:47:49 PM
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class DndPk {

    private int dndId;

    public DndPk(int dndId) {
        this.dndId = dndId;
    }

    public int getDndId() {
        return dndId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DndPk dndPk = (DndPk) o;

        if (dndId != dndPk.dndId) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return dndId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DndPk");
        sb.append("{dndId=").append(dndId);
        sb.append('}');
        return sb.toString();
    }
}
