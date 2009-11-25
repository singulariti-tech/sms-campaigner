/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class RuleValidator implements IValidator {

    private static Logger log = LoggerHelper.getLogger();
    private JTextComponent primaryKeywordField;
    private JTextComponent messageTextArea;

    public RuleValidator(JTextComponent primaryKeywordField, JTextComponent messageTextArea) {
        this.primaryKeywordField = primaryKeywordField;
        this.messageTextArea = messageTextArea;
        initializeListeners();
    }

    public boolean validate() {
        boolean validated = true;
        String primary = primaryKeywordField.getText();
        String message = messageTextArea.getText();
        if (primary == null || primary.length() < 1 || primary.length() > 45) {
            log.debug("Primary keyword is invalid");
            primaryKeywordField.setBackground(new Color(255, 0, 0));
            validated = false;
        }
        if (message == null || message.length() < 1) {
            log.debug("Message is invalid");
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
