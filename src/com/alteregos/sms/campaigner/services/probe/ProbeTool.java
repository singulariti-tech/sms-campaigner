/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.services.probe;

import com.alteregos.sms.campaigner.exceptions.CommPortTestException;
import com.alteregos.sms.campaigner.conf.Configuration;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;

/**
 *
 * @author John Emmanuel
 */
public class ProbeTool {

    private static Logger log = LoggerHelper.getLogger();
    private Configuration configuration;
    private SerialPortDiscoverer probe;
    int baudRate = 9600;

    public ProbeTool(Configuration configuration) {
        this.configuration = configuration;
        this.probe = new SerialPortDiscoverer();
    }

    public ProbeResults start() {
        String[] portNames = getPorts();
        boolean isPortTestSuccessful = testConfiguredPort();
        boolean isDatabaseTestSuccessful = testDatabaseConnectivity();
        ProbeResults results = new ProbeResults();
        results.setDbTestSuccessful(isDatabaseTestSuccessful);
        results.setPortTestSuccessful(isPortTestSuccessful);
        results.setPortNames(portNames);
        results.setBaudRate(baudRate);
        return results;
    }

    private String[] getPorts() {
        //Probe Port Settings        
        String[] portNames = probe.retrieveNamesOfAvailableCommPorts();
        return portNames;
    }

    private boolean testConfiguredPort() {
        boolean testSuccessful = false;
        if (configuration.getComPort() != null && !configuration.getComPort().equals("")) {
            try {
                log.debug("Testing port: " + configuration.getComPort());
                testSuccessful = probe.test(configuration.getComPort());
                if (testSuccessful) {
                    log.debug("Setting best baud rate - " + probe.getBestBaudRate());
                    baudRate = probe.getBestBaudRate();
                }
                log.debug("Port test successful: " + testSuccessful);
            } catch (CommPortTestException ex) {
                testSuccessful = false;
                log.debug("Post test unsuccessful: " + ex.getMessage());
            }
        }
        return testSuccessful;
    }

    private boolean testDatabaseConnectivity() {
        boolean isDbTestSuccessful = false;
        EntityManager entityManager = null;
        try {
            entityManager = Persistence.createEntityManagerFactory("absolute-smsPU").createEntityManager();
            isDbTestSuccessful = true;
            log.debug("Database connectivity test successful");
        } catch (oracle.toplink.essentials.exceptions.DatabaseException de) {
            if (de.getErrorCode() == 4002) {
                isDbTestSuccessful = false;
            }
            log.debug("Database connectivity test unsuccessful: " + de.getMessage());
        } catch (Exception e) {
            log.debug("Database connectivity test unsuccessful: " + e.getMessage());
            isDbTestSuccessful = false;
        }
        return isDbTestSuccessful;
    }
}
