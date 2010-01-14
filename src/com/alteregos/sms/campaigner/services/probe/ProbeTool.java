package com.alteregos.sms.campaigner.services.probe;

import com.alteregos.sms.campaigner.exceptions.CommPortTestException;
import com.alteregos.sms.campaigner.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John Emmanuel
 */
public class ProbeTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProbeTool.class);
    private Configuration configuration;
    private SerialPortDiscoverer probe;
    int baudRate = 9600;

    public ProbeTool(Configuration configuration) {
        LOGGER.debug("** ProbeTool()");
        this.configuration = configuration;
        this.probe = new SerialPortDiscoverer();
    }

    public ProbeResults start() {
        LOGGER.debug(">> start()");
        String[] portNames = getPorts();
        boolean isPortTestSuccessful = testConfiguredPort();
        boolean isDatabaseTestSuccessful = testDatabaseConnectivity();
        ProbeResults results = new ProbeResults();
        results.setDbTestSuccessful(isDatabaseTestSuccessful);
        results.setPortTestSuccessful(isPortTestSuccessful);
        results.setPortNames(portNames);
        results.setBaudRate(baudRate);
        LOGGER.debug("<< start()");
        return results;
    }

    private String[] getPorts() {
        LOGGER.debug(">> getPorts()");
        //Probe Port Settings        
        String[] portNames = probe.retrieveNamesOfAvailableCommPorts();
        LOGGER.debug("<< getPorts()");
        return portNames;
    }

    private boolean testConfiguredPort() {
        LOGGER.debug(">> testConfiguredPort()");
        boolean testSuccessful = false;
        if (configuration.getComPort() != null && !configuration.getComPort().equals("")) {
            try {
                LOGGER.debug("-- Testing port: {}", configuration.getComPort());
                testSuccessful = probe.test(configuration.getComPort());
                if (testSuccessful) {
                    LOGGER.debug("-- Setting best baud rate - {}", probe.getBestBaudRate());
                    baudRate = probe.getBestBaudRate();
                }
                LOGGER.debug("--Port test successful: {}", testSuccessful);
            } catch (CommPortTestException ex) {
                testSuccessful = false;
                LOGGER.debug("-- Post test unsuccessful: {}", ex.getMessage());
            }
        }
        LOGGER.debug("<< testConfiguredPort()");
        return testSuccessful;
    }

    private boolean testDatabaseConnectivity() {
        LOGGER.debug(">> testDatabaseConnectivity");
        boolean isDbTestSuccessful = false;
        try {
            //For SQLite this will always be true
            isDbTestSuccessful = true;
            LOGGER.debug("-- Database connectivity test successful");
        } catch (Exception de) {
            //if (de. == 4002) {
            isDbTestSuccessful = false;
            //}
            LOGGER.debug("-- Database connectivity test unsuccessful: {}", de.getMessage());
        } //catch (Exception e) {
        //log.debug("Database connectivity test unsuccessful: " + e.getMessage());
        //isDbTestSuccessful = false;
        //}
        LOGGER.debug("<< testDatabaseConnectivity");
        return isDbTestSuccessful;
    }
}
