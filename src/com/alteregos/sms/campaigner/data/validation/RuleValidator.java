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
public class RuleValidator implements IValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleValidator.class);
    private JTextComponent primaryKeywordField;
    private JTextComponent messageTextArea;

    public RuleValidator(JTextComponent primaryKeywordField, JTextComponent messageTextArea) {
        this.primaryKeywordField = primaryKeywordField;
        this.messageTextArea = messageTextArea;
        initializeListeners();
    }

    @Override
    public boolean validate() {
        boolean validated = true;
        String primary = primaryKeywordField.getText();
        String message = messageTextArea.getText();
        if (primary == null || primary.length() < 1 || primary.length() > 45) {
            LOGGER.debug("Invalid primary keyword");
            primaryKeywordField.setBackground(new Color(255, 0, 0));
            validated = false;
        }
        if (message == null || message.length() < 1) {
            LOGGER.debug("Empty or no message");
            messageTextArea.setBackground(new Color(255, 0, 0));
            validated = false;
        }
        return validated;
    }

    private void initializeListeners() {
        primaryKeywordField.addFocusListener(new TextComponentFocusListener(primaryKeywordField));
        messageTextArea.addFocusListener(new TextComponentFocusListener(messageTextArea));
    }
}
