package com.alteregos.sms.campaigner.data.validation;

import com.alteregos.sms.campaigner.views.helpers.TextComponentFocusListener;
import java.awt.Color;
import javax.swing.text.JTextComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John Emmanuel
 */
public class PhonebookEntryValidator implements IValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhonebookEntryValidator.class);
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
            LOGGER.debug("Invalid contact name");
            nameField.setBackground(new Color(255, 0, 0));
            validated = false;
        }
        if (mobileNo == null || mobileNo.length() < 1 || mobileNo.length() > 16) {
            LOGGER.debug("Invalid mobile number");
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
