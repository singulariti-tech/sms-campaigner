package com.alteregos.sms.campaigner.conf;

import com.alteregos.sms.campaigner.business.FlowControl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.comm.SerialPort;
import org.smslib.GatewayException;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;

/**
 *
 * @author John Emmanuel
 */
public class ModemSettings {

    private List<String> ports;
    private List<String> flowControlModes;
    private int baudRate = 9600; //Default baud rate for sms modems
    private int dataBits = SerialPort.DATABITS_8; //Default for all electronic applications
    private int stopBits = SerialPort.STOPBITS_1; //Single bit stop identification
    private int parityBits = SerialPort.PARITY_NONE; //No parity 
    private SerialModemGateway serialModemGateway;
    private int signalLevel = 0;
    private String manufacturer;
    private String model;
    private String serialNo;
    private String imsiNo;
    private String swVersion;

    public ModemSettings() {
        ports = new ArrayList<String>();
        flowControlModes = new ArrayList<String>();
        flowControlModes.add(FlowControl.NONE.getLabel());
        flowControlModes.add(FlowControl.RTSCTS_IN.getLabel());
        flowControlModes.add(FlowControl.RTSCTS_OUT.getLabel());
        flowControlModes.add(FlowControl.XONXOFF_IN.getLabel());
        flowControlModes.add(FlowControl.XON_XOFF_OUT.getLabel());
    }

    public int getBaudRate() {
        return baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public String[] getFlowControlModes() {
        String[] flowControlModesArray = new String[flowControlModes.size()];
        flowControlModes.toArray(flowControlModesArray);
        return flowControlModesArray;
    }

    public int getParityBits() {
        return parityBits;
    }

    public String[] getPorts() {
        String[] portArray = new String[ports.size()];
        ports.toArray(portArray);
        return portArray;
    }

    public int getStopBits() {
        return stopBits;
    }

    public int getSignalLevel() throws TimeoutException, GatewayException, IOException, InterruptedException {
        signalLevel = serialModemGateway.getSignalLevel();
        return signalLevel;
    }

    public String getManufacturer() throws TimeoutException, GatewayException, IOException, InterruptedException {
        this.manufacturer = serialModemGateway.getManufacturer();
        return manufacturer;
    }

    public String getModel() throws TimeoutException, GatewayException, IOException, InterruptedException {
        this.model = serialModemGateway.getModel();
        return model;
    }

    public ModemSettings addPort(String portName) {
        if (!ports.contains(portName)) {
            ports.add(portName);
        }
        return this;
    }

    public ModemSettings addFlowControlMode(String flowControlMode) {
        if (!flowControlModes.contains(flowControlMode)) {
            flowControlModes.add(flowControlMode);
        }
        return this;
    }

    public ModemSettings gateway(SerialModemGateway gateway) {
        this.serialModemGateway = gateway;
        return this;
    }

    public ModemSettings imsiNo(String imsi) {
        this.imsiNo = imsi;
        return this;
    }

    public ModemSettings serialNo(String no) {
        this.serialNo = no;
        return this;
    }

    public ModemSettings swVersion(String version) {
        this.swVersion = version;
        return this;
    }

    public ModemSettings baudRate(int baudRate) {
        this.baudRate = baudRate;
        return this;
    }

    public String getImsiNo() {
        return imsiNo;
    }

    public String getSwVersion() {
        return swVersion;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public boolean isGatewayInitialized() {
        if (this.serialModemGateway == null) {
            // || !this.serialModemGateway.getGatewayStatus().equals(GatewayStatuses.OK)
            return false;
        }
        return true;
    }
}
