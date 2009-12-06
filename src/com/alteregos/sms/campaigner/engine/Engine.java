package com.alteregos.sms.campaigner.engine;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.conf.Configuration;
import com.alteregos.sms.campaigner.conf.ModemSettings;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.jdesktop.application.TaskService;
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

    private static Logger log = LoggerHelper.getLogger();
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
        this.modemSettings = modemSettings;
        this.main = Main.getApplication();
        this.configuration = main.getConfiguration();
        this.taskService = main.getContext().getTaskService();
        initialize();
    }

    private void initialize() {
        String comPort = configuration.getComPort();
        int baudRate = modemSettings.getBaudRate();
        log.debug("Initializing SMS engine: " + baudRate);
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
                log.debug("Gateway initialized");
            } catch (Exception e) {
                gatewayInitialized = false;
                log.error("Exception when initializing gateway");
                log.error(e);
            } finally {
                if (gatewayInitialized) {
                    log.debug("Initializing service");
                    service = new Service();
                    service.setLoadBalancer(new RoundRobinLoadBalancer(service));
                    inboundMessageNotification.setService(service);
                    service.addGateway(gateway);
                    gateway.setService(service);
                    if (configuration.isGatewayAutoStart()) {
                        log.debug("Gateway has been set to autostart...so will start gateway");
                        boolean gatewayStarted = running = start();
                        if (gatewayStarted) {
                            try {
                                String serialNo = gateway.getSerialNo();
                                String imsiNo = gateway.getImsi();
                                String swVersion = gateway.getSwVersion();
                                this.modemSettings.serialNo(serialNo).swVersion(swVersion).imsiNo(imsiNo);
                            } catch (TimeoutException ex) {
                                log.debug("Took too long to read SIM properties");
                                log.error(ex);
                            } catch (GatewayException ex) {
                                log.error("Exception when reading SIM properties");
                                log.error(ex);
                            } catch (IOException ex) {
                                log.error("IOException when reading SIM properties");
                                log.error(ex);
                            } catch (InterruptedException ex) {
                                log.error("Interrupted exception when reading SIM properties");
                                log.error(ex);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean startGateway() {
        boolean started = false;
        try {
            log.debug("Trying to start gateway");
            gateway.startGateway();
            started = true;
        } catch (TimeoutException ex) {
            started = false;
            log.debug("Took too long to start gateway");
            log.error(ex);
        } catch (GatewayException ex) {
            log.error("Exception when starting gateway");
            log.error(ex);
        } catch (SMSLibException ex) {
            started = false;
            log.error("SMSLib exception when starting gateway");
            log.error(ex);
        } catch (IOException ex) {
            log.error("IOException when starting gateway");
            log.error(ex);
        } catch (InterruptedException ex) {
            started = false;
            log.error("Interrupted exception when starting gateway");
            log.error(ex);
        } finally {
            log.debug("Gateway started: " + started);
        }
        return started;
    }

    private boolean stopGateway() {
        boolean stopped = false;
        try {
            log.debug("Trying to stop gateway");
            gateway.stopGateway();
            stopped = true;
        } catch (TimeoutException ex) {
            stopped = false;
            log.error("Timeout when stopping gateway");
            log.error(ex);
        } catch (GatewayException ex) {
            stopped = false;
            log.error("Gateway exception when stopping gateway");
            log.error(ex);
        } catch (SMSLibException ex) {
            stopped = false;
            log.error("SMSLib exception when stopping gateway");
            log.error(ex);
        } catch (IOException ex) {
            stopped = false;
            log.error("IOException when stopping gateway");
            log.error(ex);
        } catch (InterruptedException ex) {
            stopped = false;
            log.error("Interrupted exception when stopping gateway");
            log.error(ex);
        } finally {
            log.debug("Gateway stopped: " + stopped);
        }
        return stopped;
    }

    public boolean start() {
        log.info("Trying to start SMS engine");
        if (gatewayInitialized && !running) {
            log.debug("Going to start gateway");
            running = startGateway();
            engineServices = new EngineServicesTask(main, service);
            taskService.execute(engineServices);
            return running;
        } else {
            log.info("Gateway probably running already");
        }

        return false;
    }

    public boolean stop() {
        if (running) {
            log.info("Trying to stop SMS Engine");
            engineServices.cancel(true);
            stopGateway();
            running = false;
            log.info("Successfully stopped SMS Engine");
        }
        return false;
    }

    public ModemSettings getModemSettings() {
        return this.modemSettings;
    }

    public boolean isRunning() {
        return running;
    }
}
