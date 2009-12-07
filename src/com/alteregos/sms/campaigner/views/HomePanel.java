package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.conf.Configuration;
import com.alteregos.sms.campaigner.conf.ModemSettings;
import com.alteregos.sms.campaigner.helpers.ProbeListener;
import com.alteregos.sms.campaigner.services.probe.ProbeResults;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import java.awt.Dimension;
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
            String manufacturer = "- NA -";
            String model = "- NA -";
            String imsiNo = "- NA -";
            String swVersion = "- NA -";
            String serialNo = "- NA -";
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
        gatewayStatusTextField = new javax.swing.JLabel();
        databaseConnectivityTextField = new javax.swing.JLabel();
        autoReplyStatusCheckButton = new javax.swing.JButton();
        databaseConnectivityCheckButton = new javax.swing.JButton();
        modemSummaryPanel = new javax.swing.JPanel();
        imsiNoLabel = new javax.swing.JLabel();
        imsiNoTextField = new javax.swing.JLabel();
        softwareVersionTextField = new javax.swing.JLabel();
        softwareVersionLabel = new javax.swing.JLabel();
        signalLevelLabel = new javax.swing.JLabel();
        manufacturerLabel = new javax.swing.JLabel();
        modelLabel = new javax.swing.JLabel();
        manufacturerTextField = new javax.swing.JLabel();
        modelTextField = new javax.swing.JLabel();
        signalLevelProgressBar = new javax.swing.JProgressBar();
        signalLevelTextField = new javax.swing.JLabel();
        serialNoLabel = new javax.swing.JLabel();
        serialNoTextField = new javax.swing.JLabel();
        simPinLabel = new javax.swing.JLabel();
        simPinTextField = new javax.swing.JLabel();

        setName("Form"); // NOI18N

        Main application = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class);
        ResourceMap resourceMap = application.getContext().getResourceMap(HomePanel.class);
        servicesSummaryPanel.setName("homePanel"); // NOI18N

        gatewayStatusLabel.setText(resourceMap.getString("autoReplyServiceStatusLabel.text")); // NOI18N
        gatewayStatusLabel.setName("autoReplyServiceStatusLabel"); // NOI18N

        databaseConnectivityLabel.setText(resourceMap.getString("databaseConnectivityLabel.text")); // NOI18N
        databaseConnectivityLabel.setName("databaseConnectivityLabel"); // NOI18N

        gatewayStatusTextField.setText(resourceMap.getString("gatewayStatusTextField.text")); // NOI18N        
        gatewayStatusTextField.setName("gatewayStatusTextField"); // NOI18N

        databaseConnectivityTextField.setText(resourceMap.getString("databaseConnectivityTextField.text")); // NOI18N        
        databaseConnectivityTextField.setName("databaseConnectivityTextField"); // NOI18N

        autoReplyStatusCheckButton.setText(resourceMap.getString("autoReplyStatusCheckButton.text")); // NOI18N
        autoReplyStatusCheckButton.setName("autoReplyStatusCheckButton"); // NOI18N

        databaseConnectivityCheckButton.setText(resourceMap.getString("databaseConnectivityCheckButton.text")); // NOI18N
        databaseConnectivityCheckButton.setName("databaseConnectivityCheckButton"); // NOI18N

        modemSummaryPanel.setName("simCardSummaryPanel"); // NOI18N

        imsiNoLabel.setText(resourceMap.getString("imsiNoLabel.text")); // NOI18N
        imsiNoLabel.setName("imsiNoLabel"); // NOI18N

        imsiNoTextField.setText(resourceMap.getString("imsiNoTextField.text")); // NOI18N        
        imsiNoTextField.setName("imsiNoTextField"); // NOI18N

        softwareVersionTextField.setText(resourceMap.getString("softwareVersionTextField.text")); // NOI18N        
        softwareVersionTextField.setName("softwareVersionTextField"); // NOI18N

        softwareVersionLabel.setText(resourceMap.getString("softwareVersionLabel.text")); // NOI18N
        softwareVersionLabel.setName("softwareVersionLabel"); // NOI18N

        signalLevelLabel.setText(resourceMap.getString("signalLevelLabel.text")); // NOI18N
        signalLevelLabel.setName("signalLevelLabel"); // NOI18N

        manufacturerLabel.setText(resourceMap.getString("manufacturerLabel.text")); // NOI18N
        manufacturerLabel.setName("manufacturerLabel"); // NOI18N

        modelLabel.setText(resourceMap.getString("modelLabel.text")); // NOI18N
        modelLabel.setName("modelLabel"); // NOI18N

        manufacturerTextField.setText(resourceMap.getString("manufacturerTextField.text")); // NOI18N        
        manufacturerTextField.setName("manufacturerTextField"); // NOI18N

        modelTextField.setText(resourceMap.getString("modelTextField.text")); // NOI18N        
        modelTextField.setName("modelTextField"); // NOI18N

        signalLevelProgressBar.setBorder(BorderFactory.createLineBorder(resourceMap.getColor("signalLevelProgressBar.border.lineColor"))); // NOI18N
        signalLevelProgressBar.setName("signalLevelProgressBar"); // NOI18N

        signalLevelTextField.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
        signalLevelTextField.setText(resourceMap.getString("signalLevelTextField.text")); // NOI18N
        signalLevelTextField.setName("signalLevelTextField"); // NOI18N

        serialNoLabel.setText(resourceMap.getString("serialNoLabel.text")); // NOI18N
        serialNoLabel.setName("serialNoLabel"); // NOI18N

        serialNoTextField.setText(resourceMap.getString("serialNoTextField.text")); // NOI18N        
        serialNoTextField.setName("serialNoTextField"); // NOI18N

        simPinLabel.setText(resourceMap.getString("simPinLabel.text")); // NOI18N
        simPinLabel.setName("simPinLabel"); // NOI18N

        simPinTextField.setText(resourceMap.getString("simPinTextField.text")); // NOI18N        
        simPinTextField.setName("simPinTextField"); // NOI18N

        servicesSummaryPanel.setLayout(new MigLayout("fill, insets panel", "[150][]", "min!"));//servicesSummaryPanel
        modemSummaryPanel.setLayout(new MigLayout("fill, insets panel", "[150][]", "min!"));//modemSimSummaryPanel

        servicesSummaryPanel.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("homePanel.border.title")));
        modemSummaryPanel.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("simCardSummaryPanel.border.title")));

        this.setPreferredSize(new Dimension(resourceMap.getInteger("width.text"), resourceMap.getInteger("height.text")));
        this.setLayout(new MigLayout("insets panel, wrap 1"));

        this.add(servicesSummaryPanel, "top, left, grow");
        this.add(modemSummaryPanel, "top, left, grow, push");

        servicesSummaryPanel.add(gatewayStatusLabel);
        servicesSummaryPanel.add(gatewayStatusTextField, "gap 5, wrap");
        servicesSummaryPanel.add(databaseConnectivityLabel);
        servicesSummaryPanel.add(databaseConnectivityTextField, "gap 5, push");

        modemSummaryPanel.add(manufacturerLabel);
        modemSummaryPanel.add(manufacturerTextField, "gap 5, wrap");
        modemSummaryPanel.add(modelLabel);
        modemSummaryPanel.add(modelTextField, "gap 5, wrap");
        modemSummaryPanel.add(serialNoLabel);
        modemSummaryPanel.add(serialNoTextField, "gap 5, wrap");
        modemSummaryPanel.add(signalLevelLabel);
        modemSummaryPanel.add(signalLevelProgressBar, "split 2, gap 5, push");
        modemSummaryPanel.add(signalLevelTextField, "gap 5, right, wrap");
        modemSummaryPanel.add(imsiNoLabel);
        modemSummaryPanel.add(imsiNoTextField, "gap 5, wrap");
        modemSummaryPanel.add(softwareVersionLabel);
        modemSummaryPanel.add(softwareVersionTextField, "gap 5,wrap");
        modemSummaryPanel.add(simPinLabel);
        modemSummaryPanel.add(simPinTextField, "gap 5, push, wrap");
    }
    private javax.swing.JLabel gatewayStatusLabel;
    private javax.swing.JButton autoReplyStatusCheckButton;
    private javax.swing.JButton databaseConnectivityCheckButton;
    private javax.swing.JLabel databaseConnectivityLabel;
    private javax.swing.JLabel databaseConnectivityTextField;
    private javax.swing.JLabel gatewayStatusTextField;
    private javax.swing.JPanel servicesSummaryPanel;
    private javax.swing.JLabel imsiNoLabel;
    private javax.swing.JLabel imsiNoTextField;
    private javax.swing.JLabel manufacturerLabel;
    private javax.swing.JLabel manufacturerTextField;
    private javax.swing.JLabel modelLabel;
    private javax.swing.JLabel modelTextField;
    private javax.swing.JLabel serialNoLabel;
    private javax.swing.JLabel serialNoTextField;
    private javax.swing.JLabel signalLevelLabel;
    private javax.swing.JProgressBar signalLevelProgressBar;
    private javax.swing.JLabel signalLevelTextField;
    private javax.swing.JPanel modemSummaryPanel;
    private javax.swing.JLabel simPinLabel;
    private javax.swing.JLabel simPinTextField;
    private javax.swing.JLabel softwareVersionLabel;
    private javax.swing.JLabel softwareVersionTextField;
    private static Logger log = LoggerHelper.getLogger();
    private ProbeResults probeResults;
}
