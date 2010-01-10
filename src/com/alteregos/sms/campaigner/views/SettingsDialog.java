package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.conf.Configuration;
import com.alteregos.sms.campaigner.conf.ModemSettings;
import com.alteregos.sms.campaigner.business.FlowControl;
import com.alteregos.sms.campaigner.views.helpers.SizeLimitedTextComponent;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.application.Action;
import org.jdesktop.application.LocalStorage;

/**
 *
 * @author  John Emmanuel
 */
public class SettingsDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;
    private javax.swing.JLabel baudRateLabel;
    private javax.swing.JTextField baudRateTextField;
    private javax.swing.JButton callNotificationClearButton;
    private javax.swing.JPanel callNotificationContainer;
    private javax.swing.JTextField callNotificationLengthTextField;
    private javax.swing.JCheckBox callNotificationLongMessageCheckBox;
    private javax.swing.JPanel callNotificationPanel;
    private javax.swing.JButton callNotificationSaveButton;
    private javax.swing.JScrollPane callNotificationScrollPane;
    private javax.swing.JTextArea callNotificationTextArea;
    private javax.swing.JComboBox comPortComboBox;
    private javax.swing.JLabel comPortLabel;
    private javax.swing.JLabel dataBitsLabel;
    private javax.swing.JTextField dataBitsTextField;
    private javax.swing.JButton defaultMessageClearButton;
    private javax.swing.JPanel defaultMessageContainer;
    private javax.swing.JTextField defaultMessageLengthTextField;
    private javax.swing.JCheckBox defaultMessageLongMessageCheckbox;
    private javax.swing.JPanel defaultMessagePanel;
    private javax.swing.JButton defaultMessageSaveButton;
    private javax.swing.JScrollPane defaultMessageScrollPane;
    private javax.swing.JTextArea defaultMessageTextArea;
    private javax.swing.JCheckBox enableCallNotificationCheckbox;
    private javax.swing.JCheckBox enableDefaultMessageCheckbox;
    private javax.swing.JCheckBox enableMessageFooterForCallNotificationCheckbox;
    private javax.swing.JCheckBox enableMessageFooterForDefaultMessageCheckbox;
    private javax.swing.JComboBox flowControlComboBox;
    private javax.swing.JLabel flowControlLabel;
    private javax.swing.JButton footerClearButton;
    private javax.swing.JButton footerSaveButton;
    private javax.swing.JScrollPane footerScrollPane;
    private javax.swing.JPanel messageFooterContainer;
    private javax.swing.JPanel messageFooterPanel;
    private javax.swing.JTextArea messageFooterTextArea;
    private javax.swing.JTextField messageLengthTextField;
    private javax.swing.JLabel parityBitsLabel;
    private javax.swing.JTextField parityBitsTextField;
    private javax.swing.JPanel portContainer;
    private javax.swing.JPanel portPanel;
    private javax.swing.JLabel portSettingsStatusLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel stopBitsLabel;
    private javax.swing.JTextField stopBitsTextField;
    private javax.swing.JTabbedPane tabbedPane;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    private Configuration configuration;

    /** Creates new form SettingsDialog
     * @param parent
     * @param modal
     */
    public SettingsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        addListeners();
        initState();
    }

    // <editor-fold defaultstate="collapsed" desc="Listeners Initialization">
    private void addListeners() {
        int maxFooterLength = Integer.parseInt(messageLengthTextField.getText());
        int maxNotificationLength = Integer.parseInt(callNotificationLengthTextField.getText());
        int maxDefaultMessageLength = Integer.parseInt(defaultMessageLengthTextField.getText());

        JTextComponent messageFooterTextComponent = messageFooterTextArea;
        messageFooterTextComponent.setDocument(new SizeLimitedTextComponent(maxFooterLength, messageLengthTextField));
        JTextComponent callNotificationTextComponent = callNotificationTextArea;
        callNotificationTextComponent.setDocument(new SizeLimitedTextComponent(maxNotificationLength, callNotificationLengthTextField));
        JTextComponent defaultMessageTextComponent = defaultMessageTextArea;
        defaultMessageTextComponent.setDocument(new SizeLimitedTextComponent(maxDefaultMessageLength, defaultMessageLengthTextField));
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="State Initialization">
    private void initState() {

        //Get required data from Main
        configuration = Main.getApplication().getConfiguration();
        ModemSettings portSettings = Main.getApplication().getProbeResults().getModemSettings();
        //Set static items
        //Flow Control        
        String configuredFlowControlMode = FlowControl.getFlowControl(Integer.parseInt(configuration.getFlowControl())).getLabel();
        String[] flowControlModes = portSettings.getFlowControlModes();
        for (String mode : flowControlModes) {
            flowControlComboBox.addItem(mode);
            if (mode.equals(configuredFlowControlMode)) {
                flowControlComboBox.setSelectedItem(mode);
            }
        }
        //Baud Rate
        baudRateTextField.setText(String.valueOf(portSettings.getBaudRate()));
        //Data Bits
        dataBitsTextField.setText(String.valueOf(portSettings.getDataBits()));
        //Stop Bits
        stopBitsTextField.setText(String.valueOf(portSettings.getStopBits()));
        //Parity Bits
        parityBitsTextField.setText(String.valueOf(portSettings.getParityBits()));
        //Scan COM ports. set values found in combobox
        String[] portNames = portSettings.getPorts();
        if (portNames.length > 0) {
            for (int i = 0; i < portNames.length; i++) {
                String portName = portNames[i];
                comPortComboBox.addItem(portName);
                if (portName.equals(configuration.getComPort())) {
                    comPortComboBox.setSelectedIndex(i);
                }
            }
        } else {
            comPortComboBox.addItem(configuration.getComPort());
            comPortComboBox.setSelectedIndex(0);
        }
        //Message Settings
        messageFooterTextArea.setText(configuration.getMessageFooter());
        enableCallNotificationCheckbox.setSelected(configuration.isCallNotificationEnabled());
        callNotificationLongMessageCheckBox.setSelected(configuration.isLongCallNotificationEnabled());
        enableMessageFooterForCallNotificationCheckbox.setSelected(configuration.isMessageFooterEnabledForCallNotification());
        callNotificationTextArea.setText(configuration.getCallNotification());
        enableDefaultMessageCheckbox.setSelected(configuration.isDefaultMessageEnabled());
        defaultMessageLongMessageCheckbox.setSelected(configuration.isLongDefaultMessageEnabled());
        enableMessageFooterForDefaultMessageCheckbox.setSelected(configuration.isMessageFooterEnabledForDefaultMessage());
        defaultMessageTextArea.setText(configuration.getDefaultMessage());
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Actions">
    @Action
    public void savePortSettings() {
        FlowControl flowControl = FlowControl.getFlowControl((String) flowControlComboBox.getSelectedItem());
        this.configuration.comPort((String) comPortComboBox.getSelectedItem());
        this.configuration.flowControl(String.valueOf(flowControl.getCode()));
        boolean saved = false;
        try {
            saveConfiguration();
            saved = true;
        } catch (Exception e) {
            saved = false;
        } finally {
            if (saved) {
                JOptionPane.showMessageDialog(this, "Port settings saved. Restart application\n for changes to take effect.");
            } else {
                JOptionPane.showMessageDialog(this, "Error! Could not save port settings");
            }
        }
    }

    @Action
    public void saveMessageFooter() {
        configuration.messageFooter(messageFooterTextArea.getText());
        boolean saved = false;
        try {
            saveConfiguration();
            saved = true;
        } catch (Exception e) {
            saved = false;
        } finally {
            if (saved) {
                boolean defaultMessageLonger = false;
                boolean callNotificationLonger = false;
                int messageFooterLength = messageFooterTextArea.getText().length();
                boolean callNotificationEnabled = enableCallNotificationCheckbox.isSelected();
                boolean longCallNotification = callNotificationLongMessageCheckBox.isSelected();
                boolean messageFooterForCallNotificationEnabled = enableMessageFooterForCallNotificationCheckbox.isSelected();

                if (callNotificationEnabled && messageFooterForCallNotificationEnabled) {
                    int callNotificationLength = callNotificationLengthTextField.getText().length();
                    int totalLength = callNotificationLength + messageFooterLength;
                    if ((longCallNotification && (totalLength > 480)) || (!longCallNotification && (totalLength > 160))) {
                        callNotificationLonger = true;
                        enableMessageFooterForCallNotificationCheckbox.setSelected(false);
                        configuration.messageFooterEnabledForCallNotification(false);
                    }
                }

                boolean defaultMessageEnabled = enableDefaultMessageCheckbox.isSelected();
                boolean longDefaultMessage = defaultMessageLongMessageCheckbox.isSelected();
                boolean messageFooterForDefaultMessageEnabled = enableMessageFooterForDefaultMessageCheckbox.isSelected();
                if (defaultMessageEnabled && messageFooterForDefaultMessageEnabled) {
                    int defaultMessageLength = defaultMessageLengthTextField.getText().length();
                    int totalLength = defaultMessageLength + messageFooterLength;
                    if ((longDefaultMessage && (totalLength > 480)) || (!longDefaultMessage && (totalLength > 160))) {
                        defaultMessageLonger = true;
                        enableMessageFooterForDefaultMessageCheckbox.setSelected(false);
                        configuration.messageFooterEnabledForDefaultMessage(false);
                    }
                }

                String message = "";
                if (callNotificationLonger && defaultMessageLonger) {
                    message = "Message footer saved. However since call notification and\n"
                            + "default message lengths are greater than standard lengths, footers"
                            + "have been disabled for these messages. Restart application for\n"
                            + "changes to take effect";
                } else if (callNotificationLonger) {
                    message = "Message footer saved. However since call notification length\n"
                            + "is now greater than standard length, footer has been disabled for "
                            + "this message. Restart application for changes to take effect";
                } else if (defaultMessageLonger) {
                    message = "Message footer saved. However since call default message length\n"
                            + "is now greater than standard length, footer has been disabled for "
                            + "this message. Restart application for changes to take effect.";
                } else if (!callNotificationLonger && !defaultMessageLonger) {
                    message = "Message footer saved. Restart application\n for changes to take effect";
                }

                saveConfiguration();
                JOptionPane.showMessageDialog(this, message);

            } else {
                JOptionPane.showMessageDialog(this, "Error! Could not save message footer");
            }
        }
    }

    @Action
    public void clearMessageFooter() {
        messageFooterTextArea.setText("");
    }

    @Action
    public void saveCallNotification() {
        configuration.callNotificationEnabled(enableCallNotificationCheckbox.isSelected());
        configuration.longCallNotificationEnabled(callNotificationLongMessageCheckBox.isSelected());
        configuration.messageFooterEnabledForCallNotification(enableMessageFooterForCallNotificationCheckbox.isSelected());
        configuration.callNotification(callNotificationTextArea.getText());
        boolean saved = false;
        try {
            saveConfiguration();
            saved = true;
        } catch (Exception e) {
            saved = false;
        } finally {
            if (saved) {
                JOptionPane.showMessageDialog(this, "Call notification saved. Restart application\n for changes to take effect.");
            } else {
                JOptionPane.showMessageDialog(this, "Error! Could not save call notification");
            }
        }
    }

    @Action
    public void clearCallNotification() {
        callNotificationTextArea.setText("");
    }

    @Action
    public void changeCallNotificationLength() {
        modifyCallNotificationLength();
    }

    @Action
    public void enableMessageFooterForCallNotificationAction() {
        modifyCallNotificationLength();
    }

    private void modifyCallNotificationLength() {
        String currentContent = callNotificationTextArea.getText();
        final int longNotificationLength = 480;
        final int defaultNotificationLength = 160;
        int messageFooterLength = configuration.getMessageFooter().length();
        boolean isLongNotification = callNotificationLongMessageCheckBox.isSelected();
        boolean isMessageFooterEnabledForCallNotification = enableMessageFooterForCallNotificationCheckbox.isSelected();
        JTextComponent callNotificationTextComponent = callNotificationTextArea;
        if (isLongNotification) {
            int newCallNotificationLength = 0;
            if (isMessageFooterEnabledForCallNotification) {
                newCallNotificationLength = longNotificationLength - messageFooterLength;
            } else {
                newCallNotificationLength = longNotificationLength;
            }
            callNotificationTextComponent.setDocument(new SizeLimitedTextComponent(newCallNotificationLength, callNotificationLengthTextField));
        } else {
            int newCallNotificationLength = 0;
            if (isMessageFooterEnabledForCallNotification) {
                newCallNotificationLength = defaultNotificationLength - messageFooterLength;
            } else {
                newCallNotificationLength = defaultNotificationLength;
            }
            callNotificationTextComponent.setDocument(new SizeLimitedTextComponent(newCallNotificationLength, callNotificationLengthTextField));
        }
        callNotificationTextArea.setText(currentContent);
    }

    @Action
    public void saveDefaultMessage() {
        configuration.defaultMessageEnabled(enableDefaultMessageCheckbox.isSelected());
        configuration.longDefaultMessageEnabled(defaultMessageLongMessageCheckbox.isSelected());
        configuration.messageFooterEnabledForDefaultMessage(enableMessageFooterForDefaultMessageCheckbox.isSelected());
        configuration.defaultMessage(defaultMessageTextArea.getText());
        boolean saved = false;
        try {
            saveConfiguration();
            saved = true;
        } catch (Exception e) {
            saved = false;
        } finally {
            if (saved) {
                JOptionPane.showMessageDialog(this, "Default message saved. Restart application\n for changes to take effect.");
            } else {
                JOptionPane.showMessageDialog(this, "Error! Could not save default message");
            }
        }
    }

    @Action
    public void changeDefaultMessageLength() {
        String currentContent = defaultMessageTextArea.getText();
        final int longDefaultMessageLength = 480;
        final int defaultMessageLength = 160;
        boolean isLongMessage = defaultMessageLongMessageCheckbox.isSelected();
        JTextComponent defaultMessageTextComponent = defaultMessageTextArea;
        if (isLongMessage) {
            defaultMessageTextComponent.setDocument(new SizeLimitedTextComponent(longDefaultMessageLength, defaultMessageLengthTextField));
        } else {
            defaultMessageTextComponent.setDocument(new SizeLimitedTextComponent(defaultMessageLength, defaultMessageLengthTextField));
        }
        defaultMessageTextArea.setText(currentContent);
    }

    @Action
    public void clearDefaultMessage() {
        defaultMessageTextArea.setText("");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private methods">
    private void saveConfiguration() {
        LocalStorage storage = Main.getApplication().getContext().getLocalStorage();
        OutputStream stream = null;
        try {
            stream = storage.openOutputFile(Configuration.CONFIG_FILE_NAME);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        Configuration.save(stream);
    }
    //</editor-fold>

    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        tabbedPane = new javax.swing.JTabbedPane();
        portPanel = new javax.swing.JPanel();
        portContainer = new javax.swing.JPanel();
        comPortLabel = new javax.swing.JLabel();
        flowControlLabel = new javax.swing.JLabel();
        baudRateLabel = new javax.swing.JLabel();
        dataBitsLabel = new javax.swing.JLabel();
        stopBitsLabel = new javax.swing.JLabel();
        parityBitsLabel = new javax.swing.JLabel();
        comPortComboBox = new javax.swing.JComboBox();
        flowControlComboBox = new javax.swing.JComboBox();
        baudRateTextField = new javax.swing.JTextField();
        dataBitsTextField = new javax.swing.JTextField();
        stopBitsTextField = new javax.swing.JTextField();
        parityBitsTextField = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();
        portSettingsStatusLabel = new javax.swing.JLabel();
        messageFooterPanel = new javax.swing.JPanel();
        messageFooterContainer = new javax.swing.JPanel();
        footerScrollPane = new javax.swing.JScrollPane();
        messageFooterTextArea = new javax.swing.JTextArea();
        messageLengthTextField = new javax.swing.JTextField();
        footerClearButton = new javax.swing.JButton();
        footerSaveButton = new javax.swing.JButton();
        callNotificationPanel = new javax.swing.JPanel();
        callNotificationContainer = new javax.swing.JPanel();
        callNotificationScrollPane = new javax.swing.JScrollPane();
        callNotificationTextArea = new javax.swing.JTextArea();
        callNotificationLengthTextField = new javax.swing.JTextField();
        callNotificationSaveButton = new javax.swing.JButton();
        callNotificationClearButton = new javax.swing.JButton();
        callNotificationLongMessageCheckBox = new javax.swing.JCheckBox();
        enableCallNotificationCheckbox = new javax.swing.JCheckBox();
        enableMessageFooterForCallNotificationCheckbox = new javax.swing.JCheckBox();
        defaultMessagePanel = new javax.swing.JPanel();
        defaultMessageContainer = new javax.swing.JPanel();
        defaultMessageScrollPane = new javax.swing.JScrollPane();
        defaultMessageTextArea = new javax.swing.JTextArea();
        defaultMessageLengthTextField = new javax.swing.JTextField();
        defaultMessageSaveButton = new javax.swing.JButton();
        defaultMessageClearButton = new javax.swing.JButton();
        enableDefaultMessageCheckbox = new javax.swing.JCheckBox();
        defaultMessageLongMessageCheckbox = new javax.swing.JCheckBox();
        enableMessageFooterForDefaultMessageCheckbox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getResourceMap(SettingsDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        tabbedPane.setName("tabbedPane"); // NOI18N

        portPanel.setName("portPanel"); // NOI18N

        portContainer.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        portContainer.setName("portContainer"); // NOI18N

        comPortLabel.setText(resourceMap.getString("comPortLabel.text")); // NOI18N
        comPortLabel.setName("comPortLabel"); // NOI18N

        flowControlLabel.setText(resourceMap.getString("flowControlLabel.text")); // NOI18N
        flowControlLabel.setName("flowControlLabel"); // NOI18N

        baudRateLabel.setText(resourceMap.getString("baudRateLabel.text")); // NOI18N
        baudRateLabel.setName("baudRateLabel"); // NOI18N

        dataBitsLabel.setText(resourceMap.getString("dataBitsLabel.text")); // NOI18N
        dataBitsLabel.setName("dataBitsLabel"); // NOI18N

        stopBitsLabel.setText(resourceMap.getString("stopBitsLabel.text")); // NOI18N
        stopBitsLabel.setName("stopBitsLabel"); // NOI18N

        parityBitsLabel.setText(resourceMap.getString("parityBitsLabel.text")); // NOI18N
        parityBitsLabel.setName("parityBitsLabel"); // NOI18N

        comPortComboBox.setName("comPortComboBox"); // NOI18N

        flowControlComboBox.setName("flowControlComboBox"); // NOI18N

        baudRateTextField.setEditable(false);
        baudRateTextField.setText(resourceMap.getString("baudRateTextField.text")); // NOI18N
        baudRateTextField.setName("baudRateTextField"); // NOI18N

        dataBitsTextField.setEditable(false);
        dataBitsTextField.setText(resourceMap.getString("dataBitsTextField.text")); // NOI18N
        dataBitsTextField.setName("dataBitsTextField"); // NOI18N

        stopBitsTextField.setEditable(false);
        stopBitsTextField.setText(resourceMap.getString("stopBitsTextField.text")); // NOI18N
        stopBitsTextField.setName("stopBitsTextField"); // NOI18N

        parityBitsTextField.setEditable(false);
        parityBitsTextField.setText(resourceMap.getString("parityBitsTextField.text")); // NOI18N
        parityBitsTextField.setName("parityBitsTextField"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getActionMap(SettingsDialog.class, this);
        saveButton.setAction(actionMap.get("savePortSettings")); // NOI18N
        saveButton.setText(resourceMap.getString("saveButton.text")); // NOI18N
        saveButton.setName("saveButton"); // NOI18N

        portSettingsStatusLabel.setText(resourceMap.getString("portSettingsStatusLabel.text")); // NOI18N
        portSettingsStatusLabel.setName("portSettingsStatusLabel"); // NOI18N

        tabbedPane.addTab(resourceMap.getString("portPanel.TabConstraints.tabTitle"), portPanel); // NOI18N

        messageFooterPanel.setName("messageFooterPanel"); // NOI18N

        messageFooterContainer.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        messageFooterContainer.setName("messageFooterContainer"); // NOI18N

        footerScrollPane.setName("footerScrollPane"); // NOI18N

        messageFooterTextArea.setColumns(20);
        messageFooterTextArea.setFont(resourceMap.getFont("messageFooterTextArea.font")); // NOI18N
        messageFooterTextArea.setLineWrap(true);
        messageFooterTextArea.setRows(5);
        messageFooterTextArea.setTabSize(1);
        messageFooterTextArea.setWrapStyleWord(true);
        messageFooterTextArea.setName("messageFooterTextArea"); // NOI18N
        footerScrollPane.setViewportView(messageFooterTextArea);

        messageLengthTextField.setColumns(3);
        messageLengthTextField.setEditable(false);
        messageLengthTextField.setText(resourceMap.getString("messageLengthTextField.text")); // NOI18N
        messageLengthTextField.setName("messageLengthTextField"); // NOI18N

        footerClearButton.setAction(actionMap.get("clearMessageFooter")); // NOI18N
        footerClearButton.setText(resourceMap.getString("footerClearButton.text")); // NOI18N
        footerClearButton.setName("footerClearButton"); // NOI18N

        footerSaveButton.setAction(actionMap.get("saveMessageFooter")); // NOI18N
        footerSaveButton.setText(resourceMap.getString("footerSaveButton.text")); // NOI18N
        footerSaveButton.setName("footerSaveButton"); // NOI18N

        tabbedPane.addTab(resourceMap.getString("messageFooterPanel.TabConstraints.tabTitle"), messageFooterPanel); // NOI18N

        callNotificationPanel.setName("callNotificationPanel"); // NOI18N

        callNotificationContainer.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        callNotificationContainer.setName("callNotificationContainer"); // NOI18N

        callNotificationScrollPane.setName("callNotificationScrollPane"); // NOI18N

        callNotificationTextArea.setColumns(20);
        callNotificationTextArea.setFont(resourceMap.getFont("callNotificationTextArea.font")); // NOI18N
        callNotificationTextArea.setLineWrap(true);
        callNotificationTextArea.setRows(5);
        callNotificationTextArea.setTabSize(1);
        callNotificationTextArea.setWrapStyleWord(true);
        callNotificationTextArea.setName("callNotificationTextArea"); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, enableCallNotificationCheckbox, org.jdesktop.beansbinding.ELProperty.create("${selected}"), callNotificationTextArea, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);

        callNotificationScrollPane.setViewportView(callNotificationTextArea);

        callNotificationLengthTextField.setColumns(3);
        callNotificationLengthTextField.setEditable(false);
        callNotificationLengthTextField.setText(resourceMap.getString("callNotificationLengthTextField.text")); // NOI18N
        callNotificationLengthTextField.setName("callNotificationLengthTextField"); // NOI18N

        callNotificationSaveButton.setAction(actionMap.get("saveCallNotification")); // NOI18N
        callNotificationSaveButton.setText(resourceMap.getString("callNotificationSaveButton.text")); // NOI18N
        callNotificationSaveButton.setName("callNotificationSaveButton"); // NOI18N

        callNotificationClearButton.setAction(actionMap.get("clearCallNotification")); // NOI18N
        callNotificationClearButton.setText(resourceMap.getString("callNotificationClearButton.text")); // NOI18N
        callNotificationClearButton.setName("callNotificationClearButton"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, enableCallNotificationCheckbox, org.jdesktop.beansbinding.ELProperty.create("${selected}"), callNotificationClearButton, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        callNotificationLongMessageCheckBox.setAction(actionMap.get("changeCallNotificationLength")); // NOI18N
        callNotificationLongMessageCheckBox.setText(resourceMap.getString("callNotificationLongMessageCheckBox.text")); // NOI18N
        callNotificationLongMessageCheckBox.setName("callNotificationLongMessageCheckBox"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, enableCallNotificationCheckbox, org.jdesktop.beansbinding.ELProperty.create("${selected}"), callNotificationLongMessageCheckBox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        enableCallNotificationCheckbox.setText(resourceMap.getString("enableCallNotificationCheckbox.text")); // NOI18N
        enableCallNotificationCheckbox.setName("enableCallNotificationCheckbox"); // NOI18N

        enableMessageFooterForCallNotificationCheckbox.setAction(actionMap.get("enableMessageFooterForCallNotificationAction")); // NOI18N
        enableMessageFooterForCallNotificationCheckbox.setText(resourceMap.getString("enableMessageFooterForCallNotificationCheckbox.text")); // NOI18N
        enableMessageFooterForCallNotificationCheckbox.setName("enableMessageFooterForCallNotificationCheckbox"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, enableCallNotificationCheckbox, org.jdesktop.beansbinding.ELProperty.create("${selected}"), enableMessageFooterForCallNotificationCheckbox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        tabbedPane.addTab(resourceMap.getString("callNotificationPanel.TabConstraints.tabTitle"), callNotificationPanel); // NOI18N

        defaultMessagePanel.setName("defaultMessagePanel"); // NOI18N

        defaultMessageContainer.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        defaultMessageContainer.setName("defaultMessageContainer"); // NOI18N

        defaultMessageScrollPane.setName("defaultMessageScrollPane"); // NOI18N

        defaultMessageTextArea.setColumns(20);
        defaultMessageTextArea.setFont(resourceMap.getFont("defaultMessageTextArea.font")); // NOI18N
        defaultMessageTextArea.setLineWrap(true);
        defaultMessageTextArea.setRows(5);
        defaultMessageTextArea.setTabSize(1);
        defaultMessageTextArea.setWrapStyleWord(true);
        defaultMessageTextArea.setName("defaultMessageTextArea"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, enableDefaultMessageCheckbox, org.jdesktop.beansbinding.ELProperty.create("${selected}"), defaultMessageTextArea, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        tabbedPane.addTab(resourceMap.getString("defaultMessagePanel.TabConstraints.tabTitle"), defaultMessagePanel); // NOI18N

        defaultMessageScrollPane.setViewportView(defaultMessageTextArea);

        defaultMessageLengthTextField.setColumns(3);
        defaultMessageLengthTextField.setEditable(false);
        defaultMessageLengthTextField.setText(resourceMap.getString("defaultMessageLengthTextField.text")); // NOI18N
        defaultMessageLengthTextField.setName("defaultMessageLengthTextField"); // NOI18N

        defaultMessageSaveButton.setAction(actionMap.get("saveDefaultMessage")); // NOI18N
        defaultMessageSaveButton.setText(resourceMap.getString("defaultMessageSaveButton.text")); // NOI18N
        defaultMessageSaveButton.setName("defaultMessageSaveButton"); // NOI18N

        defaultMessageClearButton.setAction(actionMap.get("clearDefaultMessage")); // NOI18N
        defaultMessageClearButton.setText(resourceMap.getString("defaultMessageClearButton.text")); // NOI18N
        defaultMessageClearButton.setName("defaultMessageClearButton"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, enableDefaultMessageCheckbox, org.jdesktop.beansbinding.ELProperty.create("${selected}"), defaultMessageClearButton, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        enableDefaultMessageCheckbox.setText(resourceMap.getString("enableDefaultMessageCheckbox.text")); // NOI18N
        enableDefaultMessageCheckbox.setName("enableDefaultMessageCheckbox"); // NOI18N

        defaultMessageLongMessageCheckbox.setText(resourceMap.getString("defaultMessageLongMessageCheckbox.text")); // NOI18N
        defaultMessageLongMessageCheckbox.setName("defaultMessageLongMessageCheckbox"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, enableDefaultMessageCheckbox, org.jdesktop.beansbinding.ELProperty.create("${selected}"), defaultMessageLongMessageCheckbox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        enableMessageFooterForDefaultMessageCheckbox.setText(resourceMap.getString("enableMessageFooterForDefaultMessageCheckbox.text")); // NOI18N
        enableMessageFooterForDefaultMessageCheckbox.setName("enableMessageFooterForDefaultMessageCheckbox"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, enableDefaultMessageCheckbox, org.jdesktop.beansbinding.ELProperty.create("${selected}"), enableMessageFooterForDefaultMessageCheckbox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        portContainer.setLayout(new MigLayout("fill, insets panel", "[70!][]", "[min!][min!][min!][min!][min!][min!][min!]"));
        portContainer.add(comPortLabel);
        portContainer.add(comPortComboBox, "growx, wrap");
        portContainer.add(flowControlLabel);
        portContainer.add(flowControlComboBox, "growx, wrap");
        portContainer.add(baudRateLabel);
        portContainer.add(baudRateTextField, "growx, wrap");
        portContainer.add(dataBitsLabel);
        portContainer.add(dataBitsTextField, "growx, wrap");
        portContainer.add(stopBitsLabel);
        portContainer.add(stopBitsTextField, "growx, wrap");
        portContainer.add(parityBitsLabel);
        portContainer.add(parityBitsTextField, "growx, wrap");
        portContainer.add(saveButton, "skip 1, push");

        this.portPanel.setLayout(new MigLayout("fill, insets panel"));
        this.portPanel.add(portContainer, "grow, push");

        messageFooterContainer.setLayout(new MigLayout("fill, insets panel", "[]push[][]"));
        messageFooterContainer.add(footerScrollPane, "span, grow, push, wrap");
        messageFooterContainer.add(messageLengthTextField, "");
        messageFooterContainer.add(footerSaveButton);
        messageFooterContainer.add(footerClearButton);

        this.messageFooterPanel.setLayout(new MigLayout("fill, insets panel"));
        this.messageFooterPanel.add(messageFooterContainer, "grow, push");

        callNotificationContainer.setLayout(new MigLayout("fill, insets panel", "[]push[][]"));
        callNotificationContainer.add(enableCallNotificationCheckbox, "wrap");
        callNotificationContainer.add(callNotificationLongMessageCheckBox, "wrap");
        callNotificationContainer.add(enableMessageFooterForCallNotificationCheckbox, "wrap");
        callNotificationContainer.add(callNotificationScrollPane, "span, grow, push, wrap");
        callNotificationContainer.add(callNotificationLengthTextField);
        callNotificationContainer.add(callNotificationSaveButton);
        callNotificationContainer.add(callNotificationClearButton);

        this.callNotificationPanel.setLayout(new MigLayout("fill, insets panel"));
        this.callNotificationPanel.add(callNotificationContainer, "grow, push");

        defaultMessageContainer.setLayout(new MigLayout("fill, insets panel", "[]push[][]"));
        defaultMessageContainer.add(enableDefaultMessageCheckbox, "wrap");
        defaultMessageContainer.add(defaultMessageLongMessageCheckbox, "wrap");
        defaultMessageContainer.add(enableMessageFooterForDefaultMessageCheckbox, "wrap");
        defaultMessageContainer.add(defaultMessageScrollPane, "span, grow, push, wrap");
        defaultMessageContainer.add(defaultMessageLengthTextField);
        defaultMessageContainer.add(defaultMessageSaveButton);
        defaultMessageContainer.add(defaultMessageClearButton);

        this.defaultMessagePanel.setLayout(new MigLayout("fill, insets panel"));
        this.defaultMessagePanel.add(defaultMessageContainer, "grow, push");

        this.getContentPane().add(tabbedPane);

        bindingGroup.bind();

        pack();
    }
}
