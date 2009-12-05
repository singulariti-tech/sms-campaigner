package com.alteregos.sms.campaigner.services.probe;

import com.alteregos.sms.campaigner.exceptions.CommPortTestException;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.log4j.Logger;
import org.smslib.helper.CommPortIdentifier;
import org.smslib.helper.SerialPort;

/**
 *
 * @author Authors of SMSLIB
 * @author John Emmanuel
 */
public class SerialPortDiscoverer {

    private static Logger log = LoggerHelper.getLogger();
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
        ArrayList<CommPortIdentifier> portList = new ArrayList<CommPortIdentifier>();
        portEnumeration = CommPortIdentifier.getPortIdentifiers();
        while (portEnumeration.hasMoreElements()) {
            portId = portEnumeration.nextElement();
            System.out.println("Detected port " + portId.getName());
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                portList.add(portId);
            }
        }
        return portList;
    }

    public String[] retrieveNamesOfAvailableCommPorts() {
        List<CommPortIdentifier> portList = discoverCommPortIdentifiers();
        List<String> portNames = new ArrayList<String>();
        for (CommPortIdentifier id : portList) {
            portNames.add(id.getName());
        }
        String[] portArray = new String[portNames.size()];
        return portNames.toArray(portArray);
    }

    public boolean test(String portName) throws CommPortTestException {
        log.debug("Testing COM port " + portName);
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
                serialPort = portUnderTest.open("Absolute-SMS-" + portUnderTest.getName(), 1971);
                System.out.println("Testing port " + portUnderTest.getName() + " at baud rate " + baudRates[i]);

                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
                serialPort.setSerialPortParams(baudRates[i], SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
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
                        System.out.print("  Getting Info...");
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
                        System.out.println(" Found: " + response.replaceAll("\\s+OK\\s+", "").replaceAll("\n", "").replaceAll("\r", ""));
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

        System.out.println("Selected baud rate: " + baudRate);
        return isTestSuccessful;
    }

    public int getBestBaudRate() {
        return baudRate;
    }

    public static void main(String[] args) {
        SerialPortDiscoverer spd = new SerialPortDiscoverer();
        for (String port : spd.retrieveNamesOfAvailableCommPorts()) {
            System.out.println("Found port: " + port);
        }

        for (CommPortIdentifier port : spd.discoverCommPortIdentifiers()) {
            System.out.println("Found port: " + port.getName());
        }
    }
}
