package com.alteregos.sms.campaigner.data.validation;

import com.alteregos.sms.campaigner.util.StringUtils;
import com.alteregos.sms.campaigner.views.helpers.TextComponentFocusListener;
import java.awt.Color;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John Emmanuel
 */
public class SmsValidator implements IValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsValidator.class);
    private JTextComponent recepientsTextArea;
    private JTextComponent messageTextArea;

    public SmsValidator(JTextComponent recepientsTextArea, JTextComponent messageTextArea) {
        this.recepientsTextArea = recepientsTextArea;
        this.messageTextArea = messageTextArea;
        initializeListeners();
    }

    @Override
    public boolean validate() {
        boolean validated = true;
        List<String> recepientsList = StringUtils.tokenizeToList(recepientsTextArea.getText(), ",");
        if (recepientsList.size() < 1) {
            LOGGER.debug("No. of recepients is 0");
            recepientsTextArea.setBackground(new Color(255, 0, 0));
            validated = false;
        }
        if (messageTextArea.getText() == null || messageTextArea.getText().length() < 1) {
            LOGGER.debug("Empty or no message");
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
