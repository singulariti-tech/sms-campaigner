package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.conf.Configuration;
import com.alteregos.sms.campaigner.conf.ModemSettings;
import com.alteregos.sms.campaigner.helpers.ProbeListener;
import com.alteregos.sms.campaigner.services.probe.ProbeResults;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import java.io.IOException;
import javax.swing.BorderFactory;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.jdesktop.application.ResourceMap;
import org.smslib.GatewayException;
import org.smslib.TimeoutException;

/**
 *
 * @author  John Emmanuel
 */
public class HomePanel extends javax.swing.JPanel implements ProbeListener {

    private static final long serialVersionUID = 1L;

    /** Creates new form HomePanel */
    public HomePanel() {
        initComponents();
        Main.getApplication().addProbeListener(this);
        initState();
    }

    @Override
    public void probeEnded(ProbeResults results) {
        this.probeResults = results;
        initState();
    }

    @Override
    public void probeStarted() {
        //Set status
    }

    private void initState() {
        try {
            log.debug("Initializing home state");
            Configuration configuration = Main.getApplication().getConfiguration();
            ModemSettings portSettings = (this.probeResults != null) ? this.probeResults.getModemSettings() : null;
            //SIM & Gateway settings
            int signalLevel = 0;
            String manufacturer = "-NA-";
            String model = "-NA-";
            String imsiNo = "-NA-";
            String swVersion = "-NA-";
            String serialNo = "-NA-";
            String simPin = configuration.getPin();

            if (portSettings != null && portSettings.isGatewayInitialized() && configuration.isGatewayAutoStart()) {
                signalLevel = portSettings.getSignalLevel();
                manufacturer = portSettings.getManufacturer();
                model = portSettings.getModel();
                serialNo = portSettings.getSerialNo();
                swVersion = portSettings.getSwVersion();
                imsiNo = portSettings.getImsiNo();
            }

            signalLevelProgressBar.setValue(signalLevel);
            signalLevelTextField.setText(signalLevel + "%");
            manufacturerTextField.setText(manufacturer);
            modelTextField.setText(model);
            softwareVersionTextField.setText(swVersion);
            imsiNoTextField.setText(imsiNo);
            serialNoTextField.setText(serialNo);
            simPinTextField.setText(simPin);
            String dbTestMessage = "Trying to connect to database. Please wait...";
            String portTestMessage = "Trying to start gateway. Please wait...";
            if (probeResults != null) {
                dbTestMessage = probeResults.isDbTestSuccessful() ? "Connected successfully." : "Could not connect to Database";
                portTestMessage = probeResults.isPortTestSuccessful() ? "Started successfully." : "Configured port not found. Reconfigure port";
            }
            gatewayStatusTextField.setText(portTestMessage);
            databaseConnectivityTextField.setText(dbTestMessage);

        } catch (TimeoutException ex) {
            log.error("Timeout when reading SIM settings");
        } catch (GatewayException ex) {
            log.error("Gateway exception when reading SIM settings");
        } catch (IOException ex) {
            log.error("IO exception when reading SIM settings");
        } catch (InterruptedException ex) {
            log.error("Interrupted exception when reading SIM settings");
        }


    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        servicesSummaryPanel = new javax.swing.JPanel();
        gatewayStatusLabel = new javax.swing.JLabel();
        databaseConnectivityLabel = new javax.swing.JLabel();
        gatewayStatusTextField = new javax.swing.JTextField();
        databaseConnectivityTextField = new javax.swing.JTextField();
        autoReplyStatusCheckButton = new javax.swing.JButton();
        databaseConnectivityCheckButton = new javax.swing.JButton();
        modemSummaryPanel = new javax.swing.JPanel();
        imsiNoLabel = new javax.swing.JLabel();
        imsiNoTextField = new javax.swing.JTextField();
        softwareVersionTextField = new javax.swing.JTextField();
        softwareVersionLabel = new javax.swing.JLabel();
        signalLevelLabel = new javax.swing.JLabel();
        manufacturerLabel = new javax.swing.JLabel();
        modelLabel = new javax.swing.JLabel();
        manufacturerTextField = new javax.swing.JTextField();
        modelTextField = new javax.swing.JTextField();
        signalLevelProgressBar = new javax.swing.JProgressBar();
        signalLevelTextField = new javax.swing.JTextField();
        serialNoLabel = new javax.swing.JLabel();
        serialNoTextField = new javax.swing.JTextField();
        simPinLabel = new javax.swing.JLabel();
        simPinTextField = new javax.swing.JTextField();

        setName("Form"); // NOI18N

        Main application = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class);
        ResourceMap resourceMap = application.getContext().getResourceMap(HomePanel.class);
        servicesSummaryPanel.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("homePanel.border.title"))); // NOI18N
        servicesSummaryPanel.setName("homePanel"); // NOI18N

        gatewayStatusLabel.setText(resourceMap.getString("autoReplyServiceStatusLabel.text")); // NOI18N
        gatewayStatusLabel.setName("autoReplyServiceStatusLabel"); // NOI18N

        databaseConnectivityLabel.setText(resourceMap.getString("databaseConnectivityLabel.text")); // NOI18N
        databaseConnectivityLabel.setName("databaseConnectivityLabel"); // NOI18N

        gatewayStatusTextField.setEditable(false);
        gatewayStatusTextField.setText(resourceMap.getString("gatewayStatusTextField.text")); // NOI18N
        gatewayStatusTextField.setBorder(BorderFactory.createLineBorder(resourceMap.getColor("gatewayStatusTextField.border.lineColor"))); // NOI18N
        gatewayStatusTextField.setName("gatewayStatusTextField"); // NOI18N

        databaseConnectivityTextField.setEditable(false);
        databaseConnectivityTextField.setText(resourceMap.getString("databaseConnectivityTextField.text")); // NOI18N
        databaseConnectivityTextField.setBorder(BorderFactory.createLineBorder(resourceMap.getColor("databaseConnectivityTextField.border.lineColor"))); // NOI18N
        databaseConnectivityTextField.setName("databaseConnectivityTextField"); // NOI18N

        autoReplyStatusCheckButton.setText(resourceMap.getString("autoReplyStatusCheckButton.text")); // NOI18N
        autoReplyStatusCheckButton.setName("autoReplyStatusCheckButton"); // NOI18N

        databaseConnectivityCheckButton.setText(resourceMap.getString("databaseConnectivityCheckButton.text")); // NOI18N
        databaseConnectivityCheckButton.setName("databaseConnectivityCheckButton"); // NOI18N

        modemSummaryPanel.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("simCardSummaryPanel.border.title"))); // NOI18N
        modemSummaryPanel.setName("simCardSummaryPanel"); // NOI18N

        imsiNoLabel.setText(resourceMap.getString("imsiNoLabel.text")); // NOI18N
        imsiNoLabel.setName("imsiNoLabel"); // NOI18N

        imsiNoTextField.setEditable(false);
        imsiNoTextField.setText(resourceMap.getString("imsiNoTextField.text")); // NOI18N
        imsiNoTextField.setBorder(BorderFactory.createLineBorder(resourceMap.getColor("manufacturerTextField.border.lineColor"))); // NOI18N
        imsiNoTextField.setName("imsiNoTextField"); // NOI18N

        softwareVersionTextField.setEditable(false);
        softwareVersionTextField.setText(resourceMap.getString("softwareVersionTextField.text")); // NOI18N
        softwareVersionTextField.setBorder(BorderFactory.createLineBorder(resourceMap.getColor("manufacturerTextField.border.lineColor"))); // NOI18N
        softwareVersionTextField.setName("softwareVersionTextField"); // NOI18N

        softwareVersionLabel.setText(resourceMap.getString("softwareVersionLabel.text")); // NOI18N
        softwareVersionLabel.setName("softwareVersionLabel"); // NOI18N

        signalLevelLabel.setText(resourceMap.getString("signalLevelLabel.text")); // NOI18N
        signalLevelLabel.setName("signalLevelLabel"); // NOI18N

        manufacturerLabel.setText(resourceMap.getString("manufacturerLabel.text")); // NOI18N
        manufacturerLabel.setName("manufacturerLabel"); // NOI18N

        modelLabel.setText(resourceMap.getString("modelLabel.text")); // NOI18N
        modelLabel.setName("modelLabel"); // NOI18N

        manufacturerTextField.setEditable(false);
        manufacturerTextField.setText(resourceMap.getString("manufacturerTextField.text")); // NOI18N
        manufacturerTextField.setBorder(BorderFactory.createLineBorder(resourceMap.getColor("manufacturerTextField.border.lineColor"))); // NOI18N
        manufacturerTextField.setName("manufacturerTextField"); // NOI18N

        modelTextField.setEditable(false);
        modelTextField.setText(resourceMap.getString("modelTextField.text")); // NOI18N
        modelTextField.setBorder(BorderFactory.createLineBorder(resourceMap.getColor("manufacturerTextField.border.lineColor"))); // NOI18N
        modelTextField.setName("modelTextField"); // NOI18N

        signalLevelProgressBar.setBorder(BorderFactory.createLineBorder(resourceMap.getColor("signalLevelProgressBar.border.lineColor"))); // NOI18N
        signalLevelProgressBar.setName("signalLevelProgressBar"); // NOI18N

        signalLevelTextField.setColumns(4);
        signalLevelTextField.setEditable(false);
        signalLevelTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        signalLevelTextField.setText(resourceMap.getString("signalLevelTextField.text")); // NOI18N
        signalLevelTextField.setBorder(BorderFactory.createLineBorder(resourceMap.getColor("signalLevelTextField.border.lineColor"))); // NOI18N
        signalLevelTextField.setName("signalLevelTextField"); // NOI18N

        serialNoLabel.setText(resourceMap.getString("serialNoLabel.text")); // NOI18N
        serialNoLabel.setName("serialNoLabel"); // NOI18N

        serialNoTextField.setEditable(false);
        serialNoTextField.setText(resourceMap.getString("serialNoTextField.text")); // NOI18N
        serialNoTextField.setBorder(BorderFactory.createLineBorder(resourceMap.getColor("manufacturerTextField.border.lineColor"))); // NOI18N
        serialNoTextField.setName("serialNoTextField"); // NOI18N

        simPinLabel.setText(resourceMap.getString("simPinLabel.text")); // NOI18N
        simPinLabel.setName("simPinLabel"); // NOI18N

        simPinTextField.setText(resourceMap.getString("simPinTextField.text")); // NOI18N
        simPinTextField.setBorder(BorderFactory.createLineBorder(resourceMap.getColor("simPinTextField.border.lineColor"))); // NOI18N
        simPinTextField.setEnabled(false);
        simPinTextField.setName("simPinTextField"); // NOI18N

        servicesSummaryPanel.setLayout(new MigLayout("fill, insets panel", "[150][]", "min!"));//servicesSummaryPanel
        modemSummaryPanel.setLayout(new MigLayout("fill, insets panel", "[150][]", "min!"));//modemSimSummaryPanel

        servicesSummaryPanel.setBorder(BorderFactory.createTitledBorder("Connectivity Status"));
        modemSummaryPanel.setBorder(BorderFactory.createTitledBorder("Modem & SIM Card Summary"));

        this.setLayout(new MigLayout("insets panel, wrap 1"));

        this.add(servicesSummaryPanel, "top, left, grow");
        this.add(modemSummaryPanel, "gaptop 20, top, left, grow, push");

        servicesSummaryPanel.add(gatewayStatusLabel);
        servicesSummaryPanel.add(gatewayStatusTextField, "grow, push, wrap");
        servicesSummaryPanel.add(databaseConnectivityLabel);
        servicesSummaryPanel.add(databaseConnectivityTextField, "grow, push");

        modemSummaryPanel.add(manufacturerLabel);
        modemSummaryPanel.add(manufacturerTextField, "grow,  wrap");
        modemSummaryPanel.add(modelLabel);
        modemSummaryPanel.add(modelTextField, "grow,  wrap");
        modemSummaryPanel.add(serialNoLabel);
        modemSummaryPanel.add(serialNoTextField, "grow, wrap");
        modemSummaryPanel.add(signalLevelLabel);
        modemSummaryPanel.add(signalLevelProgressBar, "split 2, grow, push");
        modemSummaryPanel.add(signalLevelTextField, "right, wrap");
        modemSummaryPanel.add(imsiNoLabel);
        modemSummaryPanel.add(imsiNoTextField, "grow, wrap");
        modemSummaryPanel.add(softwareVersionLabel);
        modemSummaryPanel.add(softwareVersionTextField, "grow, wrap");
        modemSummaryPanel.add(simPinLabel);
        modemSummaryPanel.add(simPinTextField, "grow, wrap");
    }
    private javax.swing.JLabel gatewayStatusLabel;
    private javax.swing.JButton autoReplyStatusCheckButton;
    private javax.swing.JButton databaseConnectivityCheckButton;
    private javax.swing.JLabel databaseConnectivityLabel;
    private javax.swing.JTextField databaseConnectivityTextField;
    private javax.swing.JTextField gatewayStatusTextField;
    private javax.swing.JPanel servicesSummaryPanel;
    private javax.swing.JLabel imsiNoLabel;
    private javax.swing.JTextField imsiNoTextField;
    private javax.swing.JLabel manufacturerLabel;
    private javax.swing.JTextField manufacturerTextField;
    private javax.swing.JLabel modelLabel;
    private javax.swing.JTextField modelTextField;
    private javax.swing.JLabel serialNoLabel;
    private javax.swing.JTextField serialNoTextField;
    private javax.swing.JLabel signalLevelLabel;
    private javax.swing.JProgressBar signalLevelProgressBar;
    private javax.swing.JTextField signalLevelTextField;
    private javax.swing.JPanel modemSummaryPanel;
    private javax.swing.JLabel simPinLabel;
    private javax.swing.JTextField simPinTextField;
    private javax.swing.JLabel softwareVersionLabel;
    private javax.swing.JTextField softwareVersionTextField;
    private static Logger log = LoggerHelper.getLogger();
    private ProbeResults probeResults;
}
