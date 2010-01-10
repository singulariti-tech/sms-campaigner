package com.alteregos.sms.campaigner.data.validation;

import com.alteregos.sms.campaigner.util.LoggerHelper;
import com.alteregos.sms.campaigner.views.helpers.TextComponentFocusListener;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;

/**
 *
 * @author John Emmanuel
 */
public class GroupValidator implements IValidator {

    private static Logger log = LoggerHelper.getLogger();
    private JTextComponent groupNameField;
    private JTable groupMembersTable;        

    public GroupValidator(JTextComponent groupNameField, JTable membersList) {
        this.groupNameField = groupNameField;
        this.groupMembersTable = membersList;
        initializeListeners();
    }

    @Override
    public boolean validate() {
        boolean validated = true;
        String groupName = groupNameField.getText();
        if (groupName == null || groupName.length() < 1) {
            log.debug("Group name not valid");
            groupNameField.setBackground(new Color(255, 0, 0));
            validated = false;
        }

        int rowCount = groupMembersTable.getRowCount();
        if (rowCount < 1) {
            log.debug("Group members not valid");            
            validated = false;
        }

        return validated;
    }

    private void initializeListeners() {
        groupNameField.addFocusListener(new TextComponentFocusListener(groupNameField));
    }
}
