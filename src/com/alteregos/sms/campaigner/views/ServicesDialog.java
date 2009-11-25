package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.conf.Configuration;
import com.alteregos.sms.campaigner.engine.Engine;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import org.jdesktop.application.Action;

/**
 *
 * @author  John Emmanuel
 */
public class ServicesDialog extends javax.swing.JDialog {

    /** Creates new form ServicesDialog */
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
        autoReplyAutoStartRbg = new javax.swing.ButtonGroup();
        tabbedPane = new javax.swing.JTabbedPane();
        gatewayServiceTab = new javax.swing.JPanel();
        gatewayGenericPanel = new javax.swing.JPanel();
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

        tabbedPane.setName("tabbedPane"); // NOI18N

        gatewayServiceTab.setName("gatewayServiceTab"); // NOI18N

        gatewayGenericPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("gatewayGenericPanel.border.title"))); // NOI18N
        gatewayGenericPanel.setName("gatewayGenericPanel"); // NOI18N

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

        javax.swing.GroupLayout gatewayGenericPanelLayout = new javax.swing.GroupLayout(gatewayGenericPanel);
        gatewayGenericPanel.setLayout(gatewayGenericPanelLayout);
        gatewayGenericPanelLayout.setHorizontalGroup(
                gatewayGenericPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gatewayGenericPanelLayout.createSequentialGroup().addContainerGap().addGroup(gatewayGenericPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gatewayGenericPanelLayout.createSequentialGroup().addComponent(gatewayServiceAutoStartLabel).addGap(23, 23, 23).addComponent(gatewayServiceAutoStartEnable).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(gatewayServiceAutoStartDisable)).addGroup(gatewayGenericPanelLayout.createSequentialGroup().addComponent(gatewayPortNoLabel).addGap(33, 33, 33).addGroup(gatewayGenericPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(gatewayPortNoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(gatewayServiceDetailsSaveButton)))).addContainerGap(32, Short.MAX_VALUE)));
        gatewayGenericPanelLayout.setVerticalGroup(
                gatewayGenericPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gatewayGenericPanelLayout.createSequentialGroup().addGroup(gatewayGenericPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(gatewayServiceAutoStartLabel).addComponent(gatewayServiceAutoStartEnable).addComponent(gatewayServiceAutoStartDisable)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(gatewayGenericPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(gatewayPortNoLabel).addComponent(gatewayPortNoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(gatewayServiceDetailsSaveButton).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

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

        javax.swing.GroupLayout gatewayServiceStatusPanelLayout = new javax.swing.GroupLayout(gatewayServiceStatusPanel);
        gatewayServiceStatusPanel.setLayout(gatewayServiceStatusPanelLayout);
        gatewayServiceStatusPanelLayout.setHorizontalGroup(
                gatewayServiceStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gatewayServiceStatusPanelLayout.createSequentialGroup().addContainerGap().addComponent(gatewayStatusLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(gatewayServiceStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gatewayServiceStatusPanelLayout.createSequentialGroup().addComponent(startGatewayServiceButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(stopGatewayServiceButton)).addComponent(gatewayStatusTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(32, Short.MAX_VALUE)));
        gatewayServiceStatusPanelLayout.setVerticalGroup(
                gatewayServiceStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gatewayServiceStatusPanelLayout.createSequentialGroup().addGroup(gatewayServiceStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(gatewayStatusLabel).addComponent(gatewayStatusTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(gatewayServiceStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(stopGatewayServiceButton).addComponent(startGatewayServiceButton)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        javax.swing.GroupLayout gatewayServiceTabLayout = new javax.swing.GroupLayout(gatewayServiceTab);
        gatewayServiceTab.setLayout(gatewayServiceTabLayout);
        gatewayServiceTabLayout.setHorizontalGroup(
                gatewayServiceTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gatewayServiceTabLayout.createSequentialGroup().addContainerGap().addGroup(gatewayServiceTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(gatewayServiceStatusPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(gatewayGenericPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        gatewayServiceTabLayout.setVerticalGroup(
                gatewayServiceTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gatewayServiceTabLayout.createSequentialGroup().addContainerGap().addComponent(gatewayGenericPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(gatewayServiceStatusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        tabbedPane.addTab(resourceMap.getString("gatewayServiceTab.TabConstraints.tabTitle"), gatewayServiceTab); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 328, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE).addContainerGap())));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 280, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE).addContainerGap())));

        bindingGroup.bind();

        pack();
    }

    private javax.swing.ButtonGroup autoReplyAutoStartRbg;
    private javax.swing.JPanel gatewayGenericPanel;
    private javax.swing.JLabel gatewayPortNoLabel;
    private javax.swing.JTextField gatewayPortNoTextField;
    private javax.swing.JRadioButton gatewayServiceAutoStartDisable;
    private javax.swing.JRadioButton gatewayServiceAutoStartEnable;
    private javax.swing.JLabel gatewayServiceAutoStartLabel;
    private javax.swing.ButtonGroup gatewayServiceAutoStartRbg;
    private javax.swing.JButton gatewayServiceDetailsSaveButton;
    private javax.swing.JPanel gatewayServiceStatusPanel;
    private javax.swing.JPanel gatewayServiceTab;
    private javax.swing.JLabel gatewayStatusLabel;
    private javax.swing.JTextField gatewayStatusTextField;
    private javax.swing.JButton startGatewayServiceButton;
    private javax.swing.JButton stopGatewayServiceButton;
    private javax.swing.JTabbedPane tabbedPane;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
}
