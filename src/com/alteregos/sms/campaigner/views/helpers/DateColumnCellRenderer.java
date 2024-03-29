package com.alteregos.sms.campaigner.views.helpers;

import com.alteregos.sms.campaigner.util.DateUtils;
import java.awt.Component;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author John Emmanuel
 */
public class DateColumnCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    public DateColumnCellRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {        
        String dateString = "-NA-";
        if (value != null) {
            Date date = (Date) value;
            DateFormat format = DateUtils.getDefaultDateFormat();
            dateString = format.format(date);
        }
        return super.getTableCellRendererComponent(table, dateString, isSelected, hasFocus, row, column);
    }
}
