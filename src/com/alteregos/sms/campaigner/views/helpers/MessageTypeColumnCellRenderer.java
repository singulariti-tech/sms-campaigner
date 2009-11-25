/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.views.helpers;

import com.alteregos.sms.campaigner.business.OutboundSmsType;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author John Emmanuel
 */
public class MessageTypeColumnCellRenderer extends DefaultTableCellRenderer {

    public MessageTypeColumnCellRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String typeString = "";
        if (value != null) {
            OutboundSmsType type = OutboundSmsType.getType((String) value);
            typeString = type.getMessage();
        }
        return super.getTableCellRendererComponent(table, typeString, isSelected, hasFocus, row, column);
    }
}
