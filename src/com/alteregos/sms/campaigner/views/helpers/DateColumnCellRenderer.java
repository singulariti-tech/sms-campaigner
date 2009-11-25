/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.views.helpers;

import com.alteregos.sms.campaigner.util.*;
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

    public DateColumnCellRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Date date = null;
        String dateString = "";
        if (value != null) {
            date = (Date) value;
            DateFormat format = DateUtils.getDefaultDateFormat();
            dateString = format.format(date);
        }
        return super.getTableCellRendererComponent(table, dateString, isSelected, hasFocus, row, column);
    }
}
