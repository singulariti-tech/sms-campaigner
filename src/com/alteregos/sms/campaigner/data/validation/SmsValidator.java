/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.data.validation;

import com.alteregos.sms.campaigner.util.LoggerHelper;
import com.alteregos.sms.campaigner.util.StringUtils;
import com.alteregos.sms.campaigner.views.helpers.TextComponentFocusListener;
import java.awt.Color;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;

/**
 *
 * @author John Emmanuel
 */
public class SmsValidator implements IValidator {

    private static Logger log = LoggerHelper.getLogger();
    private JTextComponent recepientsTextArea;
    private JTextComponent messageTextArea;

    public SmsValidator(JTextComponent recepientsTextArea, JTextComponent messageTextArea) {
        this.recepientsTextArea = recepientsTextArea;
        this.messageTextArea = messageTextArea;
        initializeListeners();
    }

    public boolean validate() {
        boolean validated = true;
        List<String> recepientsList = StringUtils.tokenizeToList(recepientsTextArea.getText(), ",");
        if (recepientsList.size() < 1) {
            log.debug("No. of SMS recepients is 0");
            recepientsTextArea.setBackground(new Color(255, 0, 0));
            validated = false;
        }
        if (messageTextArea.getText() == null || messageTextArea.getText().length() < 1) {
            log.debug("Message is invalid");
            messageTextArea.setBackground(new Color(255, 0, 0));
            validated = false;
        }
        return validated;
    }

    private void initializeListeners() {
        recepientsTextArea.addFocusListener(new TextComponentFocusListener(recepientsTextArea));
        messageTextArea.addFocusListener(new TextComponentFocusListener(messageTextArea));
    }
}
