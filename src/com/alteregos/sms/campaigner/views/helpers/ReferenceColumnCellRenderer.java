package com.alteregos.sms.campaigner.views.helpers;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author john.emmanuel
 * @version
 * @since
 *
 */
public class ReferenceColumnCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    public ReferenceColumnCellRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String priorityString = "-NA-";
        if (value != null) {
            priorityString = (String) value;
        }
        return super.getTableCellRendererComponent(table, priorityString, isSelected, hasFocus, row, column);
    }
}
