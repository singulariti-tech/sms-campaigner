package com.alteregos.sms.campaigner.services.probe;

import com.alteregos.sms.campaigner.exceptions.CommPortTestException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.helper.CommPortIdentifier;
import org.smslib.helper.SerialPort;

/**
 *
 * @author Authors of SMSLIB
 * @author John Emmanuel
 */
public class SerialPortDiscoverer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerialPortDiscoverer.class);
    private CommPortIdentifier portId;
    private Enumeration<CommPortIdentifier> portEnumeration;
    private int baudRate = 9600;
    private int[] baudRates = new int[]{
        9600,
        19200,
        38400,
        57600,
        115200,
        230400,
        460800,
        921600
    };

    private ArrayList<CommPortIdentifier> discoverCommPortIdentifiers() {
        LOGGER.debug(">> discoverCommPortIdentifiers()");
        ArrayList<CommPortIdentifier> portList = new ArrayList<CommPortIdentifier>();
        portEnumeration = CommPortIdentifier.getPortIdentifiers();
        while (portEnumeration.hasMoreElements()) {
            portId = portEnumeration.nextElement();
            LOGGER.debug("-- detected port {}", portId.getName());
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                LOGGER.debug("-- is serial port, yay!!");
                portList.add(portId);
            }
        }
        LOGGER.debug("<< discoverCommPortIdentifiers()");
        return portList;
    }

    public String[] retrieveNamesOfAvailableCommPorts() {
        LOGGER.debug(">> retrieveNamesOfAvailableCommPorts()");
        List<CommPortIdentifier> portList = discoverCommPortIdentifiers();
        List<String> portNames = new ArrayList<String>();
        for (CommPortIdentifier id : portList) {
            portNames.add(id.getName());
        }
        String[] portArray = new String[portNames.size()];
        LOGGER.debug("<< retrieveNamesOfAvailableCommPorts()");
        return portNames.toArray(portArray);
    }

    public boolean test(String portName) throws CommPortTestException {
        LOGGER.debug(">> test()");
        LOGGER.debug("-- port under test: {}", portName);
        boolean isTestSuccessful = false;
        String response = null;
        SerialPort serialPort = null;
        CommPortIdentifier portUnderTest = null;

        for (CommPortIdentifier id : discoverCommPortIdentifiers()) {
            if (id.getName().equals(portName)) {
                portUnderTest = id;
                break;
            }
        }

        if (portUnderTest == null) {
            throw new CommPortTestException("Comm port not found");
        }

        for (int i = 0; i < baudRates.length; i++) {
            try {
                serialPort = portUnderTest.open("SmsCampaigner-" + portUnderTest.getName(), 1971);
                LOGGER.debug("-- testing port {} at baud rate {}", portUnderTest.getName(), baudRates[i]);

                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
                serialPort.setSerialPortParams(baudRates[i], SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
                InputStream inStream = serialPort.getInputStream();
                OutputStream outStream = serialPort.getOutputStream();
                serialPort.enableReceiveTimeout(1000);
                int c = inStream.read();
                while (c != -1) {
                    c = inStream.read();
                }
                outStream.write('A');
                outStream.write('T');
                outStream.write('\r');

                Thread.sleep(1000);

                response = "";
                c = inStream.read();
                while (c != -1) {
                    response += (char) c;
                    c = inStream.read();
                }
                if (response.indexOf("OK") >= 0) {
                    try {
                        LOGGER.debug("-- Getting Info...");
                        outStream.write('A');
                        outStream.write('T');
                        outStream.write('+');
                        outStream.write('C');
                        outStream.write('G');
                        outStream.write('M');
                        outStream.write('M');
                        outStream.write('\r');
                        response = "";
                        c = inStream.read();
                        while (c != -1) {
                            response += (char) c;
                            c = inStream.read();
                        }
                        LOGGER.debug("-- Found: {}",
                                response.replaceAll("\\s+OK\\s+", "").replaceAll("\n", "").replaceAll("\r", ""));
                        isTestSuccessful = true;
                        baudRate = baudRates[i];
                    } catch (Exception e) {
                        isTestSuccessful = false;
                    }
                } else {
                    isTestSuccessful = false;
                }
            } catch (Exception ex) {
                isTestSuccessful = false;
                throw new CommPortTestException(ex);
            } finally {
                serialPort.close();
            }
        }

        LOGGER.debug("-- selected baud rate: {}", baudRate);
        LOGGER.debug("<< test()");
        return isTestSuccessful;
    }

    public int getBestBaudRate() {
        return baudRate;
    }

    public static void main(String[] args) {
        LOGGER.debug(">> main()");
        SerialPortDiscoverer spd = new SerialPortDiscoverer();
        LOGGER.debug("-- listing available comm ports...");
        for (String port : spd.retrieveNamesOfAvailableCommPorts()) {
            LOGGER.debug("-- {}", port);
        }

        LOGGER.debug("-- listing discovered comm ports...");
        for (CommPortIdentifier port : spd.discoverCommPortIdentifiers()) {
            LOGGER.debug("-- {}", port.getName());
        }
    }
}
