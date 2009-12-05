package com.alteregos.sms.campaigner.views.helpers;

import com.alteregos.sms.campaigner.business.MessageStatus;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author John Emmanuel
 */
public class StatusColumnCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    public StatusColumnCellRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String status = "";
        if (value != null) {
            MessageStatus mStatus = MessageStatus.getStatus((String) value);
            status = mStatus != null ? mStatus.getMessage() : status;
        }
        return super.getTableCellRendererComponent(table, status, isSelected, hasFocus, row, column);
    }
}
