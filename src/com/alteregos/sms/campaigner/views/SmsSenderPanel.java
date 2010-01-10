package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.conf.Configuration;
import com.alteregos.sms.campaigner.data.validation.SmsValidator;
import com.alteregos.sms.campaigner.exceptions.ITaskResult;
import com.alteregos.sms.campaigner.exceptions.SuccessfulTaskResult;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import com.alteregos.sms.campaigner.util.StringUtils;
import com.alteregos.sms.campaigner.views.helpers.SizeLimitedTextComponent;
import com.alteregos.sms.campaigner.business.MessagePriority;
import com.alteregos.sms.campaigner.data.dto.Dnd;
import com.alteregos.sms.campaigner.data.dto.OutgoingMessage;
import com.alteregos.sms.campaigner.services.DndService;
import com.alteregos.sms.campaigner.services.MessageService;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.JTextComponent;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;

/**
 *
 * @author  John Emmanuel
 */
public class SmsSenderPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private MessageService messageService;
    private DndService dndService;
    private javax.swing.JButton clearButton;
    private javax.swing.JCheckBox enableMessageFooterCheckbox;
    private javax.swing.JButton groupsButton;
    private javax.swing.JCheckBox longMessageCheckbox;
    private javax.swing.JLabel messageContentLabel;
    private javax.swing.JTextField messageContentLengthTextField;
    private javax.swing.JScrollPane messageContentScrollPane;
    private javax.swing.JTextArea messageContentTextArea;
    private javax.swing.JButton phoneBookButton;
    private javax.swing.JLabel previewLabel;
    private javax.swing.JTextField previewLengthTextField;
    private javax.swing.JScrollPane previewScrollPane;
    private javax.swing.JTextArea previewTextArea;
    private javax.swing.JComboBox priorityComboBox;
    private javax.swing.JLabel priorityLabel;
    private javax.swing.JLabel recepientsLabel;
    private javax.swing.JScrollPane recepientsScrollPane;
    private javax.swing.JTextArea recepientsTextArea;
    private javax.swing.JButton sendButton;
    private List<Dnd> dndList;
    private String footer;
    private SmsValidator validator;
    private static Logger log = LoggerHelper.getLogger();

    /** Creates new form SmsSenderPanel */
    public SmsSenderPanel() {
        initComponents();
        initState();
        validator = new SmsValidator(recepientsTextArea, messageContentTextArea);
    }

    //<editor-fold defaultstate="collapsed" desc="Public methods">
    public void setRecepients(List<String> recepients) {
        recepientsTextArea.setText(StringUtils.toDelimitedString(recepients, ", "));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Actions">
    @Action
    public void sendAction() {
        if (validator.validate()) {
            ITaskResult result = null;
            List<String> recepientsList = StringUtils.tokenizeToList(recepientsTextArea.getText(), ",");
            List<String> filteredRecepientsList = new ArrayList<String>();
            for (String number : recepientsList) {
                //Validate numbers
                boolean valid = true;//PhoneNumberUtils.isMobileNumber(number);
                if (!valid) {
                    filteredRecepientsList.add(number);
                }
            }
            recepientsList.removeAll(filteredRecepientsList);
            List<String> dndFreeList = filterDndNumbers(recepientsList);
            List<OutgoingMessage> outboxList = new ArrayList<OutgoingMessage>();
            String message = previewTextArea.getText();
            String priority = (String) priorityComboBox.getSelectedItem();
            for (String number : dndFreeList) {
                OutgoingMessage outbox = new OutgoingMessage();
                outbox.setContent(message);
                outbox.setPriority(MessagePriority.getPriorityForMessage(priority));
                outbox.setRecepientNo(number);
                outboxList.add(outbox);
            }

            try {
                //DO BULK INSERT
                messageService.newOutgoingMessages(outboxList);
                result = new SuccessfulTaskResult();
            } catch (Exception rollbackException) {
                rollbackException.printStackTrace();
                //result = ExceptionParser.getError(rollbackException);
                //JOptionPane.showMessageDialog(Main.getApplication().getMainFrame(), result.getResultMessage().getLabel());
            }

            if (result.isSuccessful()) {
                clearAction();
            }
        }
    }

    @Action
    public void clearAction() {
        recepientsTextArea.setText("");
        messageContentTextArea.setText("");
        enableMessageFooterCheckbox.setSelected(false);
        longMessageCheckbox.setSelected(false);
    }

    @Action
    public void addGroupsAction() {
        List<String> recepientMobileNos = StringUtils.tokenizeToList(recepientsTextArea.getText(), ",");
        Main main = Main.getApplication();
        SmsCreatorGroupsDialog groupsDialog = new SmsCreatorGroupsDialog(main.getMainFrame(), true, this, recepientMobileNos);
        main.show(groupsDialog);
    }

    @Action
    public void addContactsAction() {
        List<String> recepientMobileNos = StringUtils.tokenizeToList(recepientsTextArea.getText(), ",");
        Main main = Main.getApplication();
        SmsCreatorContactsDialog contactsDialog = new SmsCreatorContactsDialog(main.getMainFrame(), true, this, recepientMobileNos);
        main.show(contactsDialog);
    }

    @Action
    public void toggleLongMessage() {
        resetMessageLengths();
    }

    @Action
    public void toggleMessageFooter() {
        resetMessageLengths();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="State Initialization">
    private void initState() {
        dndService = Main.getApplication().getBean("dndService");
        Configuration configuration = Main.getApplication().getConfiguration();
        this.footer = configuration.getMessageFooter();
        initListeners();
        log.debug("SmsSenderPanel initialized");
    }

    private List<String> filterDndNumbers(List<String> inputList) {
        List<String> cleanNumbers = new ArrayList<String>();
        dndList = dndService.findAll();
        List<String> dndNumbers = new ArrayList<String>();
        for (Dnd dnd : dndList) {
            dndNumbers.add(dnd.getMobileNo());
        }

        for (String number : inputList) {
            if (!dndNumbers.contains(number)) {
                cleanNumbers.add(number);
            }
        }
        return cleanNumbers;
    }

    private void resetMessageLengths() {
        String currentMessageContent = messageContentTextArea.getText();
        boolean footerEnabled = enableMessageFooterCheckbox.isSelected();
        boolean enableLongMessage = longMessageCheckbox.isSelected();
        final int longMessageLength = 480;
        final int defaultMessageLength = 160;
        int footerLength = this.footer.length();
        JTextComponent messageContentTextComponent = messageContentTextArea;
        JTextComponent previewContentTextComponent = previewTextArea;
        if (enableLongMessage && footerEnabled) {
            int messageLength = longMessageLength - footerLength;
            String fText = this.footer;
            messageContentTextComponent.setDocument(new SizeLimitedTextComponent(messageLength, messageContentLengthTextField, previewTextArea, fText));
            previewContentTextComponent.setDocument(new SizeLimitedTextComponent(longMessageLength, previewLengthTextField));
        } else if (enableLongMessage && !footerEnabled) {
            int messageLength = longMessageLength;
            String fText = null;
            messageContentTextComponent.setDocument(new SizeLimitedTextComponent(messageLength, messageContentLengthTextField, previewTextArea, fText));
            previewContentTextComponent.setDocument(new SizeLimitedTextComponent(longMessageLength, previewLengthTextField));
        } else if (!enableLongMessage && footerEnabled) {
            int messageLength = defaultMessageLength - footerLength;
            String fText = this.footer;
            messageContentTextComponent.setDocument(new SizeLimitedTextComponent(messageLength, messageContentLengthTextField, previewTextArea, fText));
            previewContentTextComponent.setDocument(new SizeLimitedTextComponent(defaultMessageLength, previewLengthTextField));
        } else {
            int messageLength = defaultMessageLength;
            String fText = null;
            messageContentTextComponent.setDocument(new SizeLimitedTextComponent(messageLength, messageContentLengthTextField, previewTextArea, fText));
            previewContentTextComponent.setDocument(new SizeLimitedTextComponent(defaultMessageLength, previewLengthTextField));
        }
        if (!currentMessageContent.equals("")) {
            messageContentTextArea.setText(currentMessageContent);
        } else {
            messageContentTextArea.setText(" ");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Listeners Initialization">
    private void initListeners() {
        final int actualContentLength = Integer.parseInt(messageContentLengthTextField.getText());
        final int previewLength = Integer.parseInt(previewLengthTextField.getText());
        JTextComponent messageContentTextComponent = messageContentTextArea;
        messageContentTextComponent.setDocument(new SizeLimitedTextComponent(actualContentLength, messageContentLengthTextField, previewTextArea, null));
        JTextComponent previewContentTextComponent = previewTextArea;
        previewContentTextComponent.setDocument(new SizeLimitedTextComponent(previewLength, previewLengthTextField));
    }
    //</editor-fold>

    @SuppressWarnings("unchecked")
    private void initComponents() {

        messageService = Main.getApplication().getBean("messageService");

        recepientsLabel = new javax.swing.JLabel();
        recepientsScrollPane = new javax.swing.JScrollPane();
        recepientsTextArea = new javax.swing.JTextArea();
        groupsButton = new javax.swing.JButton();
        messageContentLabel = new javax.swing.JLabel();
        messageContentScrollPane = new javax.swing.JScrollPane();
        messageContentTextArea = new javax.swing.JTextArea();
        messageContentLengthTextField = new javax.swing.JTextField();
        previewScrollPane = new javax.swing.JScrollPane();
        previewTextArea = new javax.swing.JTextArea();
        previewLabel = new javax.swing.JLabel();
        clearButton = new javax.swing.JButton();
        sendButton = new javax.swing.JButton();
        priorityLabel = new javax.swing.JLabel();
        priorityComboBox = new javax.swing.JComboBox();
        phoneBookButton = new javax.swing.JButton();
        previewLengthTextField = new javax.swing.JTextField();
        longMessageCheckbox = new javax.swing.JCheckBox();
        enableMessageFooterCheckbox = new javax.swing.JCheckBox();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getResourceMap(SmsSenderPanel.class);
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("smsSenderContainer.border.title"))); // NOI18N

        recepientsLabel.setText(resourceMap.getString("recepientsLabel.text")); // NOI18N
        recepientsLabel.setName("recepientsLabel"); // NOI18N

        recepientsScrollPane.setName("recepientsScrollPane"); // NOI18N

        recepientsTextArea.setColumns(20);
        recepientsTextArea.setFont(resourceMap.getFont("recepientsTextArea.font")); // NOI18N
        recepientsTextArea.setLineWrap(true);
        recepientsTextArea.setRows(5);
        recepientsTextArea.setTabSize(1);
        recepientsTextArea.setWrapStyleWord(true);
        recepientsTextArea.setName("recepientsTextArea"); // NOI18N
        recepientsScrollPane.setViewportView(recepientsTextArea);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getActionMap(SmsSenderPanel.class, this);
        groupsButton.setAction(actionMap.get("addGroupsAction")); // NOI18N
        groupsButton.setText(resourceMap.getString("groupsButton.text")); // NOI18N
        groupsButton.setName("groupsButton"); // NOI18N

        messageContentLabel.setText(resourceMap.getString("messageContentLabel.text")); // NOI18N
        messageContentLabel.setName("messageContentLabel"); // NOI18N

        messageContentScrollPane.setName("messageContentScrollPane"); // NOI18N

        messageContentTextArea.setColumns(20);
        messageContentTextArea.setFont(resourceMap.getFont("messageContentTextArea.font")); // NOI18N
        messageContentTextArea.setLineWrap(true);
        messageContentTextArea.setRows(5);
        messageContentTextArea.setTabSize(1);
        messageContentTextArea.setWrapStyleWord(true);
        messageContentTextArea.setName("messageContentTextArea"); // NOI18N
        messageContentScrollPane.setViewportView(messageContentTextArea);

        messageContentLengthTextField.setColumns(3);
        messageContentLengthTextField.setEditable(false);
        messageContentLengthTextField.setText(resourceMap.getString("messageContentLengthTextField.text")); // NOI18N
        messageContentLengthTextField.setName("messageContentLengthTextField"); // NOI18N

        previewScrollPane.setName("previewScrollPane"); // NOI18N

        previewTextArea.setColumns(20);
        previewTextArea.setEditable(false);
        previewTextArea.setFont(resourceMap.getFont("previewTextArea.font")); // NOI18N
        previewTextArea.setLineWrap(true);
        previewTextArea.setRows(5);
        previewTextArea.setTabSize(1);
        previewTextArea.setWrapStyleWord(true);
        previewTextArea.setName("previewTextArea"); // NOI18N
        previewScrollPane.setViewportView(previewTextArea);

        previewLabel.setText(resourceMap.getString("previewLabel.text")); // NOI18N
        previewLabel.setName("previewLabel"); // NOI18N

        clearButton.setAction(actionMap.get("clearAction")); // NOI18N
        clearButton.setText(resourceMap.getString("clearButton.text")); // NOI18N
        clearButton.setName("clearButton"); // NOI18N

        sendButton.setAction(actionMap.get("sendAction")); // NOI18N
        sendButton.setText(resourceMap.getString("sendButton.text")); // NOI18N
        sendButton.setName("sendButton"); // NOI18N

        priorityLabel.setText(resourceMap.getString("priorityLabel.text")); // NOI18N
        priorityLabel.setName("priorityLabel"); // NOI18N

        priorityComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Low", "Normal", "High"}));
        priorityComboBox.setSelectedIndex(1);
        priorityComboBox.setName("priorityComboBox"); // NOI18N

        phoneBookButton.setAction(actionMap.get("addContactsAction")); // NOI18N
        phoneBookButton.setText(resourceMap.getString("phoneBookButton.text")); // NOI18N
        phoneBookButton.setName("phoneBookButton"); // NOI18N

        previewLengthTextField.setColumns(3);
        previewLengthTextField.setEditable(false);
        previewLengthTextField.setText(resourceMap.getString("previewLengthTextField.text")); // NOI18N
        previewLengthTextField.setName("previewLengthTextField"); // NOI18N

        longMessageCheckbox.setAction(actionMap.get("toggleLongMessage")); // NOI18N
        longMessageCheckbox.setText(resourceMap.getString("longMessageCheckbox.text")); // NOI18N
        longMessageCheckbox.setToolTipText(resourceMap.getString("longMessageCheckbox.toolTipText")); // NOI18N
        longMessageCheckbox.setName("longMessageCheckbox"); // NOI18N

        enableMessageFooterCheckbox.setAction(actionMap.get("toggleMessageFooter")); // NOI18N
        enableMessageFooterCheckbox.setText(resourceMap.getString("enableMessageFooterCheckbox.text")); // NOI18N
        enableMessageFooterCheckbox.setToolTipText(resourceMap.getString("enableMessageFooterCheckbox.toolTipText")); // NOI18N
        enableMessageFooterCheckbox.setName("enableMessageFooterCheckbox"); // NOI18N      

        this.setLayout(new MigLayout("fill, insets panel", "[min!][push]",
                "[min!][min!][min!]15[min!][push][min!]15[min!][push][min!]"));

        this.add(recepientsLabel, "span, wrap");
        this.add(groupsButton, "w 100!");
        this.add(recepientsScrollPane, "spany 2, grow, push, wrap");
        this.add(phoneBookButton, "w 100!, wrap");
        this.add(messageContentLabel, "span, wrap");
        this.add(messageContentScrollPane, "span, grow, push, wrap");
        this.add(messageContentLengthTextField);
        this.add(longMessageCheckbox, "span, split 5, right, push");
        this.add(enableMessageFooterCheckbox);
        this.add(priorityLabel);
        this.add(priorityComboBox, "wrap");
        this.add(previewLabel, "span, wrap");
        this.add(previewScrollPane, "span, grow, push, wrap");
        this.add(previewLengthTextField);
        this.add(sendButton, "span, split 3, right, push");
        this.add(clearButton);
    }
}
