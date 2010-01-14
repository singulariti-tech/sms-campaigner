package com.alteregos.sms.campaigner.data.validation;

import com.alteregos.sms.campaigner.views.helpers.TextComponentFocusListener;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John Emmanuel
 */
public class GroupValidator implements IValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupValidator.class);
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
            LOGGER.debug("Invalid group name");
            groupNameField.setBackground(new Color(255, 0, 0));
            validated = false;
        }

        int rowCount = groupMembersTable.getRowCount();
        if (rowCount < 1) {
            LOGGER.debug("Looks like group members were not selected");
            validated = false;
        }

        return validated;
    }

    private void initializeListeners() {
        groupNameField.addFocusListener(new TextComponentFocusListener(groupNameField));
    }
}
