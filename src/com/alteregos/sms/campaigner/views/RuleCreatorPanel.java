package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;

import com.alteregos.sms.campaigner.data.dto.AutoReplyRule;
import com.alteregos.sms.campaigner.data.validation.RuleValidator;
import com.alteregos.sms.campaigner.exceptions.ExceptionParser;
import com.alteregos.sms.campaigner.exceptions.ITaskResult;
import com.alteregos.sms.campaigner.exceptions.ResultMessage;
import com.alteregos.sms.campaigner.exceptions.SuccessfulTaskResult;
import com.alteregos.sms.campaigner.services.RuleService;
import com.alteregos.sms.campaigner.views.helpers.SizeLimitedTextComponent;

import com.alteregos.sms.campaigner.views.helpers.TextComponentFocusListener;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;

/**
 *
 * @author  John Emmanuel
 */
public class RuleCreatorPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private RuleService ruleService;
    private javax.swing.JButton clearButton;
    private javax.swing.JCheckBox enableRuleCheckbox;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JTextField messageLengthTextField;
    private javax.swing.JScrollPane messageScrollPane;
    private javax.swing.JTextArea messageTextArea;
    private javax.swing.JLabel primaryKeywordLabel;
    private javax.swing.JTextField primaryKeywordTextField;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel secondaryKeywordLabel;
    private javax.swing.JTextField secondaryKeywordTextField;
    private AutoReplyRule rule;
    private RuleValidator validator;

    /** Creates new form RuleCreatorPanel */
    public RuleCreatorPanel() {
        initComponents();
        initializeListeners();
        initialize();
    }

    private void initialize() {
        this.rule = new AutoReplyRule();
        this.validator = new RuleValidator(primaryKeywordTextField, messageTextArea);
    }

    private void initializeListeners() {
        int maxMesssageLength = Integer.parseInt(messageLengthTextField.getText());
        JTextComponent contentArea = messageTextArea;
        contentArea.setDocument(new SizeLimitedTextComponent(maxMesssageLength, messageLengthTextField));
        primaryKeywordTextField.addFocusListener(new TextComponentFocusListener(primaryKeywordTextField));
        messageTextArea.addFocusListener(new TextComponentFocusListener(messageTextArea));
    }

    //<editor-fold defaultstate="collapsed" desc="Actions">
    @Action
    public Task<ITaskResult, Void> saveRule() {
        if (validator.validate()) {
            return new SaveRuleActionTask(Main.getApplication());
        }
        return null;
    }

    @Action
    public void clearRule() {
        primaryKeywordTextField.setText("");
        secondaryKeywordTextField.setText("");
        messageTextArea.setText("");
        enableRuleCheckbox.setSelected(false);
        rule = new AutoReplyRule();
    }

    //</editor-fold>
    /**
     *
     */
    //<editor-fold defaultstate="collapsed" desc="Dependencies">
    private class SaveRuleActionTask extends Task<ITaskResult, Void> {

        public SaveRuleActionTask(Application arg0) {
            super(arg0);
        }

        @Override
        protected ITaskResult doInBackground() throws Exception {
            ITaskResult result = null;
            try {
                Date createdDate = new Date();
                rule.setContent(messageTextArea.getText());
                rule.setCreatedDate(createdDate);
                rule.setModifiedDate(createdDate);
                rule.setPrimaryKeyword(primaryKeywordTextField.getText());
                rule.setSecondaryKeyword(secondaryKeywordTextField.getText());
                rule.setEnabled(enableRuleCheckbox.isSelected());

                ruleService.newRule(rule);

                result = new SuccessfulTaskResult();
            } catch (Exception rollbackException) {
                result = ExceptionParser.getError(rollbackException);
            }
            return result;
        }

        @Override
        protected void succeeded(ITaskResult arg0) {
            super.succeeded(arg0);
            ITaskResult result = arg0;
            if (result.isSuccessful()) {
                clearRule();
            } else {
                if (result.getResultMessage().equals(ResultMessage.DUPLICATE_ENTRY)) {
                    clearRule();
                    JOptionPane.showMessageDialog(Main.getApplication().getMainFrame(), "Rule already exists");
                } else {
                    JOptionPane.showMessageDialog(Main.getApplication().getMainFrame(), result.getResultMessage().getLabel());
                }
            }
        }
    }
    //</editor-fold>

    @SuppressWarnings("unchecked")
    private void initComponents() {
        ruleService = Main.getApplication().getBean("ruleService");
        primaryKeywordLabel = new javax.swing.JLabel();
        primaryKeywordTextField = new javax.swing.JTextField();
        secondaryKeywordLabel = new javax.swing.JLabel();
        secondaryKeywordTextField = new javax.swing.JTextField();
        messageLabel = new javax.swing.JLabel();
        messageScrollPane = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        messageLengthTextField = new javax.swing.JTextField();
        enableRuleCheckbox = new javax.swing.JCheckBox();
        clearButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getResourceMap(RuleCreatorPanel.class);
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("rulesCreatorPanel.border.title"))); // NOI18N
        this.setName("rulesCreatorPanel"); // NOI18N

        primaryKeywordLabel.setText(resourceMap.getString("primaryKeywordLabel.text")); // NOI18N
        primaryKeywordLabel.setName("primaryKeywordLabel"); // NOI18N

        primaryKeywordTextField.setBackground(resourceMap.getColor("primaryKeywordTextField.background")); // NOI18N
        primaryKeywordTextField.setText(resourceMap.getString("primaryKeywordTextField.text")); // NOI18N
        primaryKeywordTextField.setName("primaryKeywordTextField"); // NOI18N

        secondaryKeywordLabel.setText(resourceMap.getString("secondaryKeywordLabel.text")); // NOI18N
        secondaryKeywordLabel.setName("secondaryKeywordLabel"); // NOI18N

        secondaryKeywordTextField.setText(resourceMap.getString("secondaryKeywordTextField.text")); // NOI18N
        secondaryKeywordTextField.setName("secondaryKeywordTextField"); // NOI18N

        messageLabel.setText(resourceMap.getString("messageLabel.text")); // NOI18N
        messageLabel.setName("messageLabel"); // NOI18N

        messageScrollPane.setName("messageScrollPane"); // NOI18N

        messageTextArea.setColumns(20);
        messageTextArea.setFont(resourceMap.getFont("messageTextArea.font")); // NOI18N
        messageTextArea.setLineWrap(true);
        messageTextArea.setRows(5);
        messageTextArea.setTabSize(1);
        messageTextArea.setWrapStyleWord(true);
        messageTextArea.setName("messageTextArea"); // NOI18N
        messageScrollPane.setViewportView(messageTextArea);

        messageLengthTextField.setEditable(false);
        messageLengthTextField.setText(resourceMap.getString("messageLengthTextField.text")); // NOI18N
        messageLengthTextField.setName("messageLengthTextField"); // NOI18N

        enableRuleCheckbox.setText(resourceMap.getString("enableRuleCheckbox.text")); // NOI18N
        enableRuleCheckbox.setName("enableRuleCheckbox"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getActionMap(RuleCreatorPanel.class, this);
        clearButton.setAction(actionMap.get("clearRule")); // NOI18N
        clearButton.setText(resourceMap.getString("clearButton.text")); // NOI18N
        clearButton.setName("clearButton"); // NOI18N

        saveButton.setAction(actionMap.get("saveRule")); // NOI18N
        saveButton.setText(resourceMap.getString("saveButton.text")); // NOI18N
        saveButton.setName("saveButton"); // NOI18N

        this.setLayout(new MigLayout("fill, insets panel", "[min!][grow]", "[min!][min!][min!][grow][min!]"));
        this.add(primaryKeywordLabel);
        this.add(primaryKeywordTextField, "grow, wrap");
        this.add(secondaryKeywordLabel);
        this.add(secondaryKeywordTextField, "grow, wrap");
        this.add(messageLabel, "wrap");
        this.add(messageScrollPane, "spanx 2, grow, wrap");
        this.add(messageLengthTextField, "push");
        this.add(enableRuleCheckbox, "split 3, right");
        this.add(saveButton);
        this.add(clearButton);
    }
}
