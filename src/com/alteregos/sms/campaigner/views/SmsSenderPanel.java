package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.conf.Configuration;
import com.alteregos.sms.campaigner.data.beans.Dnd;
import com.alteregos.sms.campaigner.data.beans.Outbox;
import com.alteregos.sms.campaigner.data.validation.SmsValidator;
import com.alteregos.sms.campaigner.exceptions.ExceptionParser;
import com.alteregos.sms.campaigner.exceptions.ITaskResult;
import com.alteregos.sms.campaigner.exceptions.SuccessfulTaskResult;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import com.alteregos.sms.campaigner.util.StringUtils;
import com.alteregos.sms.campaigner.views.helpers.SizeLimitedTextComponent;
import com.alteregos.sms.campaigner.business.MessagePriority;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;

/**
 *
 * @author  John Emmanuel
 */
public class SmsSenderPanel extends javax.swing.JPanel {

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
            System.out.println("No. recepients after dnd filteration: " + dndFreeList.size());
            List<Outbox> outboxList = new ArrayList<Outbox>();
            String message = previewTextArea.getText();
            String priority = (String) priorityComboBox.getSelectedItem();
            for (String number : dndFreeList) {
                System.out.println("Recepient: " + number);
                Outbox outbox = new Outbox();
                outbox.setContent(message);
                outbox.setPriority(MessagePriority.getPriorityForMessage(priority).toString());
                outbox.setRecepient(number);
                outboxList.add(outbox);
            }

            try {
                entityManager.getTransaction().begin();
                for (Outbox outbox : outboxList) {
                    entityManager.persist(outbox);
                }
                entityManager.getTransaction().commit();
                result = new SuccessfulTaskResult();
            } catch (RollbackException rollbackException) {
                result = ExceptionParser.getError(rollbackException);
                JOptionPane.showMessageDialog(Main.getApplication().getMainFrame(), result.getResultMessage().getLabel());
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
        entityManager = javax.persistence.Persistence.createEntityManagerFactory("absolute-smsPU").createEntityManager();
        dndQuery = entityManager.createQuery("SELECT d FROM Dnd d");
        Configuration configuration = Main.getApplication().getConfiguration();
        this.footer = configuration.getMessageFooter();
        initListeners();
        log.debug("SmsSenderPanel initialized");
    }

    private List<String> filterDndNumbers(List<String> inputList) {
        List<String> cleanNumbers = new ArrayList<String>();
        dndList = dndQuery.getResultList();
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

        smsSenderContainer = new javax.swing.JPanel();
        recepientsLabel = new javax.swing.JLabel();
        recepientsScrollPane = new javax.swing.JScrollPane();
        recepientsTextArea = new javax.swing.JTextArea();
        recepientsNoticeLabel = new javax.swing.JLabel();
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
        smsSenderContainer.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("smsSenderContainer.border.title"))); // NOI18N
        smsSenderContainer.setName("smsSenderContainer"); // NOI18N

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

        recepientsNoticeLabel.setText(resourceMap.getString("recepientsNoticeLabel.text")); // NOI18N
        recepientsNoticeLabel.setName("recepientsNoticeLabel"); // NOI18N

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

        javax.swing.GroupLayout smsSenderContainerLayout = new javax.swing.GroupLayout(smsSenderContainer);
        smsSenderContainer.setLayout(smsSenderContainerLayout);
        smsSenderContainerLayout.setHorizontalGroup(
                smsSenderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(smsSenderContainerLayout.createSequentialGroup().addContainerGap().addGroup(smsSenderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(smsSenderContainerLayout.createSequentialGroup().addComponent(recepientsLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(recepientsNoticeLabel)).addGroup(smsSenderContainerLayout.createSequentialGroup().addGroup(smsSenderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(groupsButton).addComponent(phoneBookButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(recepientsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)).addComponent(previewScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE).addComponent(messageContentLabel).addComponent(messageContentScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE).addGroup(smsSenderContainerLayout.createSequentialGroup().addComponent(messageContentLengthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE).addComponent(longMessageCheckbox).addGap(18, 18, 18).addComponent(enableMessageFooterCheckbox).addGap(18, 18, 18).addComponent(priorityLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(priorityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(previewLabel).addGroup(smsSenderContainerLayout.createSequentialGroup().addComponent(previewLengthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 395, Short.MAX_VALUE).addComponent(sendButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(clearButton))).addContainerGap()));

        smsSenderContainerLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{groupsButton, phoneBookButton});

        smsSenderContainerLayout.setVerticalGroup(
                smsSenderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(smsSenderContainerLayout.createSequentialGroup().addContainerGap().addGroup(smsSenderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(recepientsLabel).addComponent(recepientsNoticeLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(smsSenderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(smsSenderContainerLayout.createSequentialGroup().addComponent(groupsButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(phoneBookButton).addGap(0, 0, Short.MAX_VALUE)).addComponent(recepientsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(messageContentLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(messageContentScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(smsSenderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(messageContentLengthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(priorityLabel).addComponent(priorityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(enableMessageFooterCheckbox).addComponent(longMessageCheckbox)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(previewLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(previewScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(smsSenderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(previewLengthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(clearButton).addComponent(sendButton)).addGap(56, 56, 56)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(smsSenderContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(smsSenderContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
    }
    
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
    private javax.swing.JLabel recepientsNoticeLabel;
    private javax.swing.JScrollPane recepientsScrollPane;
    private javax.swing.JTextArea recepientsTextArea;
    private javax.swing.JButton sendButton;
    private javax.swing.JPanel smsSenderContainer;

    private static Logger log = LoggerHelper.getLogger();
    private EntityManager entityManager;
    private Query dndQuery;
    private List<Dnd> dndList;
    private String footer;
    private SmsValidator validator;
}
