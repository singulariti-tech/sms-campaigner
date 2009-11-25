package com.alteregos.sms.campaigner.views.helpers;

import com.alteregos.sms.campaigner.business.MessagePriority;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author John Emmanuel
 */
public class PriorityColumnCellRenderer extends DefaultTableCellRenderer {

    public PriorityColumnCellRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String priorityString = "";
        if (value != null) {
            MessagePriority priority = MessagePriority.getPriority((String) value);
            priorityString = priority != null ? priority.getMessage() : priorityString;
        }
        return super.getTableCellRendererComponent(table, priorityString, isSelected, hasFocus, row, column);
    }
}
