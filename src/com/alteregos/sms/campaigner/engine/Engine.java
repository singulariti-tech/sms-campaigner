package com.alteregos.sms.campaigner.engine;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.conf.Configuration;
import com.alteregos.sms.campaigner.conf.ModemSettings;
import java.io.IOException;
import org.jdesktop.application.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.GatewayException;
import org.smslib.RoundRobinLoadBalancer;
import org.smslib.SMSLibException;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;

/**
 *
 * @author John Emmanuel
 */
public class Engine {

    private static final Logger LOGGER = LoggerFactory.getLogger(Engine.class);
    private Main main;
    private TaskService taskService;
    private Configuration configuration;
    private ModemSettings modemSettings;
    private SerialModemGateway gateway;
    private Service service;
    private EngineServicesTask engineServices;
    private boolean gatewayInitialized = false;
    private boolean running = false;

    public Engine(ModemSettings modemSettings) {
        LOGGER.debug("** Engine()");
        this.modemSettings = modemSettings;
        this.main = Main.getApplication();
        this.configuration = main.getConfiguration();
        this.taskService = main.getContext().getTaskService();
        initialize();
    }

    private void initialize() {
        LOGGER.debug(">> initialize()");
        String comPort = configuration.getComPort();
        int baudRate = modemSettings.getBaudRate();
        LOGGER.info("-- initializing SMS engine with baudrate {}" + baudRate);
        CallNotification callNotification = null;
        OutboundMessageNotification outboundMessageNotification = null;
        InboundMessageNotification inboundMessageNotification = null;
        if (comPort != null && !comPort.equals("")) {
            try {
                callNotification = new CallNotification();
                outboundMessageNotification = new OutboundMessageNotification();
                inboundMessageNotification = new InboundMessageNotification();
                gateway = new SerialModemGateway("SmsCampaigner-Gateway", comPort, baudRate, "", "");
                modemSettings.gateway(gateway);
                gateway.setSimPin(configuration.getPin());
                gateway.setInbound(true);
                gateway.setOutbound(true);
                gatewayInitialized = true;
                gateway.setCallNotification(callNotification);
                gateway.setInboundNotification(inboundMessageNotification);
                gateway.setOutboundNotification(outboundMessageNotification);
                LOGGER.info("-- Gateway initialized");
            } catch (Exception e) {
                gatewayInitialized = false;
                LOGGER.error("-- Exception when initializing gateway: {}", e);
            } finally {
                if (gatewayInitialized) {
                    LOGGER.debug("-- Initializing service");
                    service = new Service();
                    service.setLoadBalancer(new RoundRobinLoadBalancer(service));
                    inboundMessageNotification.setService(service);
                    service.addGateway(gateway);
                    gateway.setService(service);
                    if (configuration.isGatewayAutoStart()) {
                        LOGGER.debug("-- Gateway configured to be autostarted...so will start gateway");
                        boolean gatewayStarted = running = start();
                        if (gatewayStarted) {
                            try {
                                String serialNo = gateway.getSerialNo();
                                String imsiNo = gateway.getImsi();
                                String swVersion = gateway.getSwVersion();
                                this.modemSettings.serialNo(serialNo).swVersion(swVersion).imsiNo(imsiNo);
                            } catch (TimeoutException ex) {
                                LOGGER.debug("-- Took too long to read SIM properties: {}", ex);
                            } catch (GatewayException ex) {
                                LOGGER.error("Exception when reading SIM properties: {}", ex);
                            } catch (IOException ex) {
                                LOGGER.error("IOException when reading SIM properties: {}", ex);
                            } catch (InterruptedException ex) {
                                LOGGER.error("Interrupted exception when reading SIM properties: {}", ex);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean startGateway() {
        LOGGER.debug(">> startGateway()");
        boolean started = false;
        try {
            LOGGER.info("-- Trying to start gateway");
            gateway.startGateway();
            started = true;
        } catch (TimeoutException ex) {
            started = false;
            LOGGER.info("-- Took too long...quitting");
            LOGGER.debug("-- TimeoutException: {}", ex);
        } catch (GatewayException ex) {
            LOGGER.info("-- Error starting gateway");
            LOGGER.error("-- Error: {}", ex);
        } catch (SMSLibException ex) {
            started = false;
            LOGGER.info("-- Application component error");
            LOGGER.error("-- SMSLib exception when starting gateway: {}", ex);
        } catch (IOException ex) {
            LOGGER.info("-- Application component error");
            LOGGER.error("-- IOException when starting gateway: {}", ex);
        } catch (InterruptedException ex) {
            started = false;
            LOGGER.info("-- Application component error");
            LOGGER.error("-- Interrupted exception when starting gateway: {}", ex);
        } finally {
            LOGGER.info("-- Gateway status - started? " + started);
        }
        LOGGER.debug("<< startGateway()");
        return started;
    }

    private boolean stopGateway() {
        LOGGER.debug(">> stopGateway()");
        boolean stopped = false;
        try {
            LOGGER.debug("-- Trying to stop gateway");
            gateway.stopGateway();
            stopped = true;
        } catch (TimeoutException ex) {
            stopped = false;
            LOGGER.info("-- Took too long...quitting");
            LOGGER.error("-- Timeout when stopping gateway: {}", ex);
        } catch (GatewayException ex) {
            stopped = false;
            LOGGER.info("-- Error starting gateway");
            LOGGER.error("-- Error: {}", ex);
        } catch (SMSLibException ex) {
            stopped = false;
            LOGGER.info("-- Application component error");
            LOGGER.error("-- SMSLib exception when stopping gateway: {}", ex);
        } catch (IOException ex) {
            stopped = false;
            LOGGER.info("-- Application component error");
            LOGGER.error("-- IOException when stopping gateway: {}", ex);
        } catch (InterruptedException ex) {
            stopped = false;
            LOGGER.info("-- Application component error");
            LOGGER.error("-- Interrupted exception when stopping gateway: {}", ex);
        } finally {
            LOGGER.info("-- Gateway status - stopped? " + stopped);
        }
        LOGGER.debug("<< stopGateway()");
        return stopped;
    }

    public boolean start() {
        LOGGER.debug(">> start()");
        if (gatewayInitialized && !running) {
            LOGGER.debug("-- calling startGateway()");
            running = startGateway();
            engineServices = new EngineServicesTask(main, service);
            taskService.execute(engineServices);
            return running;
        } else {
            LOGGER.info("-- Gateway probably running already");
        }
        LOGGER.debug("<< start()");
        return false;
    }

    public boolean stop() {
        LOGGER.debug(">> stop()");
        if (running) {
            LOGGER.info("-- Trying to stop SMS Engine");
            engineServices.cancel(true);
            stopGateway();
            running = false;
            LOGGER.info("-- Successfully stopped SMS Engine");
        }
        LOGGER.debug("<< stop()");
        return false;
    }

    public ModemSettings getModemSettings() {
        return this.modemSettings;
    }

    public boolean isRunning() {
        return running;
    }
}
