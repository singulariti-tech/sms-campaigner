package com.alteregos.sms.campaigner.data.validation;

import com.alteregos.sms.campaigner.util.LoggerHelper;
import com.alteregos.sms.campaigner.views.helpers.TextComponentFocusListener;
import java.awt.Color;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;

/**
 *
 * @author John Emmanuel
 */
public class PhonebookEntryValidator implements IValidator {

    private static Logger log = LoggerHelper.getLogger();
    private JTextComponent nameField;
    private JTextComponent mobileNoField;

    public PhonebookEntryValidator(JTextComponent nameField, JTextComponent mobileNoField) {
        this.nameField = nameField;
        this.mobileNoField = mobileNoField;
        initializeListeners();
    }

    @Override
    public boolean validate() {
        boolean validated = true;
        String contact = nameField.getText();
        String mobileNo = mobileNoField.getText();
        if (contact == null || contact.length() < 1 || contact.length() > 64) {
            log.debug("Contact name is invalid");
            nameField.setBackground(new Color(255, 0, 0));
            validated = false;
        }
        if (mobileNo == null || mobileNo.length() < 1 || mobileNo.length() > 16) {
            log.debug("Contact mobile no. is invalid");
            mobileNoField.setBackground(new Color(255, 0, 0));
            validated = false;
        }
        return validated;
    }

    private void initializeListeners() {
        nameField.addFocusListener(new TextComponentFocusListener(nameField));
        mobileNoField.addFocusListener(new TextComponentFocusListener(mobileNoField));
    }
}
