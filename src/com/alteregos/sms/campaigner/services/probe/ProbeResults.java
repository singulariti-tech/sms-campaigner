/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.services.probe;

import com.alteregos.sms.campaigner.conf.ModemSettings;
import org.smslib.modem.SerialModemGateway;

/**
 *
 * @author John Emmanuel
 */
public class ProbeResults {

    private boolean portTestSuccessful = false;
    private boolean dbTestSuccessful = false;
    private ModemSettings modemSettings = new ModemSettings();
    private String[] portNames = null;

    public ProbeResults() {
    }

    public boolean isDbTestSuccessful() {
        return dbTestSuccessful;
    }

    public void setDbTestSuccessful(boolean dbTestSuccessful) {
        this.dbTestSuccessful = dbTestSuccessful;
    }

    public boolean isPortTestSuccessful() {
        return portTestSuccessful;
    }

    public void setPortTestSuccessful(boolean portTestSuccessful) {
        this.portTestSuccessful = portTestSuccessful;
    }

    public String[] getPortNames() {
        return portNames;
    }

    public void setPortNames(String[] portNames) {
        this.portNames = portNames;
    }

    public ModemSettings getModemSettings() {
        return modemSettings;
    }

    public void setModemSettings(ModemSettings modemSettings) {
        this.modemSettings = modemSettings;
    }

    public void setGateway(SerialModemGateway gateway) {
        this.modemSettings.gateway(gateway);
    }

    public void setBaudRate(int baudRate) {
        this.modemSettings.baudRate(baudRate);
    }

    public void addPort(String portName) {
        this.modemSettings.addPort(portName);
    }
}
