package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.conf.Configuration;
import com.alteregos.sms.campaigner.engine.Engine;
import java.awt.Container;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.application.Action;

/**
 *
 * @author  John Emmanuel
 */
public class ServicesDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;
    private javax.swing.JPanel gatewayDetailsPanel;
    private javax.swing.JPanel gatewayServiceStatusPanel;
    //
    private javax.swing.JLabel gatewayServiceAutoStartLabel;
    private javax.swing.ButtonGroup gatewayServiceAutoStartRbg;
    private javax.swing.JRadioButton gatewayServiceAutoStartDisable;
    private javax.swing.JRadioButton gatewayServiceAutoStartEnable;
    private javax.swing.JLabel gatewayPortNoLabel;
    private javax.swing.JTextField gatewayPortNoTextField;
    private javax.swing.JButton gatewayServiceDetailsSaveButton;
    //
    private javax.swing.JLabel gatewayStatusLabel;
    private javax.swing.JTextField gatewayStatusTextField;
    private javax.swing.JButton startGatewayServiceButton;
    private javax.swing.JButton stopGatewayServiceButton;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;

    /** Creates new form ServicesDialog
     * @param parent
     * @param modal
     */
    public ServicesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initState();
    }

    public void initState() {
        Configuration configuration = Main.getApplication().getConfiguration();
        boolean gatewayAutoStartEnabled = configuration.isGatewayAutoStart();
        int portNo = configuration.getGatewayPortNumber();
        setAutoStartServiceEnabled(gatewayServiceAutoStartRbg, gatewayAutoStartEnabled);
        gatewayPortNoTextField.setText(String.valueOf(portNo));
        //Verify if services have been started and set state
        Engine engine = Main.getApplication().getEngine();
        if (engine != null) {
            boolean running = engine.isRunning();
            setServiceState(running);
        }
    }

    private void setServiceState(boolean serviceRunning) {
        startGatewayServiceButton.setEnabled(!serviceRunning);
        stopGatewayServiceButton.setEnabled(serviceRunning);
        if (serviceRunning) {
            gatewayStatusTextField.setText("RUNNING");
        } else {
            gatewayStatusTextField.setText("STOPPED");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Actions">
    @Action
    public void startGatewayService() {
        Engine engine = Main.getApplication().getEngine();
        if (engine != null) {
            boolean running = engine.start();
            setServiceState(running);
        } else {
            //ERROR
        }
    }

    @Action
    public void stopGatewayService() {
        Engine engine = Main.getApplication().getEngine();
        if (engine != null) {
            boolean running = engine.stop();
            setServiceState(running);
        } else {
            //ERROR
        }
    }

    @Action
    public void saveGatewayServiceSettings() {
        Configuration configuration = Main.getApplication().getConfiguration();
        boolean enabled = isAutoStartServiceEnabled(gatewayServiceAutoStartRbg);
        int portNo = configuration.getGatewayPortNumber();
        try {
            portNo = Integer.parseInt(gatewayPortNoTextField.getText());
            configuration.gatewayPortNumber(portNo);
            configuration.gatewayAutoStart(enabled);
            JOptionPane.showMessageDialog(this, "Settings have been saved. Restart\n application for changes to affect");
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(this, "Port number should be a positive integer");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods">
    private boolean isAutoStartServiceEnabled(ButtonGroup group) {
        Enumeration<AbstractButton> buttons = group.getElements();
        while (buttons.hasMoreElements()) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                String buttonLabel = button.getText();
                //TODO This can be made very generic by reading from resouce bundle
                if (buttonLabel.equals("Enable")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void setAutoStartServiceEnabled(ButtonGroup group, boolean enabled) {
        Enumeration<AbstractButton> buttons = group.getElements();
        while (buttons.hasMoreElements()) {
            AbstractButton button = buttons.nextElement();
            if (enabled && button.getText().equals("Enable")) {
                button.setSelected(enabled);
            }
        }
    }
    //</editor-fold>

    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        gatewayServiceAutoStartRbg = new javax.swing.ButtonGroup();
        gatewayDetailsPanel = new javax.swing.JPanel();
        gatewayPortNoLabel = new javax.swing.JLabel();
        gatewayPortNoTextField = new javax.swing.JTextField();
        gatewayServiceAutoStartEnable = new javax.swing.JRadioButton();
        gatewayServiceAutoStartDisable = new javax.swing.JRadioButton();
        gatewayServiceAutoStartLabel = new javax.swing.JLabel();
        gatewayServiceDetailsSaveButton = new javax.swing.JButton();
        gatewayServiceStatusPanel = new javax.swing.JPanel();
        gatewayStatusLabel = new javax.swing.JLabel();
        gatewayStatusTextField = new javax.swing.JTextField();
        stopGatewayServiceButton = new javax.swing.JButton();
        startGatewayServiceButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getResourceMap(ServicesDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        gatewayDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("gatewayGenericPanel.border.title"))); // NOI18N
        gatewayDetailsPanel.setName("gatewayGenericPanel"); // NOI18N

        gatewayPortNoLabel.setText(resourceMap.getString("gatewayPortNoLabel.text")); // NOI18N
        gatewayPortNoLabel.setName("gatewayPortNoLabel"); // NOI18N

        gatewayPortNoTextField.setText(resourceMap.getString("gatewayPortNoTextField.text")); // NOI18N
        gatewayPortNoTextField.setName("gatewayPortNoTextField"); // NOI18N

        gatewayServiceAutoStartRbg.add(gatewayServiceAutoStartEnable);
        gatewayServiceAutoStartEnable.setText(resourceMap.getString("gatewayServiceAutoStartEnable.text")); // NOI18N
        gatewayServiceAutoStartEnable.setName("gatewayServiceAutoStartEnable"); // NOI18N

        gatewayServiceAutoStartRbg.add(gatewayServiceAutoStartDisable);
        gatewayServiceAutoStartDisable.setText(resourceMap.getString("gatewayServiceAutoStartDisable.text")); // NOI18N
        gatewayServiceAutoStartDisable.setName("gatewayServiceAutoStartDisable"); // NOI18N

        gatewayServiceAutoStartLabel.setText(resourceMap.getString("gatewayServiceAutoStartLabel.text")); // NOI18N
        gatewayServiceAutoStartLabel.setName("gatewayServiceAutoStartLabel"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getActionMap(ServicesDialog.class, this);
        gatewayServiceDetailsSaveButton.setAction(actionMap.get("saveGatewayServiceSettings")); // NOI18N
        gatewayServiceDetailsSaveButton.setText(resourceMap.getString("gatewayServiceDetailsSaveButton.text")); // NOI18N
        gatewayServiceDetailsSaveButton.setName("gatewayServiceDetailsSaveButton"); // NOI18N

        gatewayServiceStatusPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Current Status"));
        gatewayServiceStatusPanel.setName("gatewayServiceStatusPanel"); // NOI18N

        gatewayStatusLabel.setText(resourceMap.getString("gatewayStatusLabel.text")); // NOI18N
        gatewayStatusLabel.setName("gatewayStatusLabel"); // NOI18N

        gatewayStatusTextField.setEditable(false);
        gatewayStatusTextField.setText(resourceMap.getString("gatewayStatusTextField.text")); // NOI18N
        gatewayStatusTextField.setName("gatewayStatusTextField"); // NOI18N

        stopGatewayServiceButton.setAction(actionMap.get("stopGatewayService")); // NOI18N
        stopGatewayServiceButton.setText(resourceMap.getString("stopGatewayServiceButton.text")); // NOI18N
        stopGatewayServiceButton.setName("stopGatewayServiceButton"); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, gatewayStatusTextField, org.jdesktop.beansbinding.ELProperty.create("${text != STOPPED}"), stopGatewayServiceButton, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        startGatewayServiceButton.setAction(actionMap.get("startGatewayService")); // NOI18N
        startGatewayServiceButton.setText(resourceMap.getString("startGatewayServiceButton.text")); // NOI18N
        startGatewayServiceButton.setName("startGatewayServiceButton"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, gatewayStatusTextField, org.jdesktop.beansbinding.ELProperty.create("${text != STARTED}"), startGatewayServiceButton, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        Container contentPane = this.getContentPane();
        contentPane.setLayout(new MigLayout("fill, insets dialog, wrap 1"));
        contentPane.add(gatewayDetailsPanel, "top, left, growx");
        contentPane.add(gatewayServiceStatusPanel, "top, left, grow, push");

        gatewayDetailsPanel.setLayout(new MigLayout("fill, insets panel", "[70!][min!][min!]", "min!"));
        gatewayServiceStatusPanel.setLayout(new MigLayout("fill, insets panel", "[70!][min!]", "min!"));

        gatewayDetailsPanel.add(gatewayServiceAutoStartLabel);
        gatewayDetailsPanel.add(gatewayServiceAutoStartEnable);
        gatewayDetailsPanel.add(gatewayServiceAutoStartDisable, "push, wrap");
        gatewayDetailsPanel.add(gatewayPortNoLabel);
        gatewayDetailsPanel.add(gatewayPortNoTextField, "width 70, span, wrap");
        gatewayDetailsPanel.add(gatewayServiceDetailsSaveButton, "skip 1");

        gatewayServiceStatusPanel.add(gatewayStatusLabel);
        gatewayServiceStatusPanel.add(gatewayStatusTextField, "width 70, span, wrap");
        gatewayServiceStatusPanel.add(startGatewayServiceButton, "skip 1, split 2, top, left");
        gatewayServiceStatusPanel.add(stopGatewayServiceButton, "push");

        bindingGroup.bind();

        pack();
    }
}
