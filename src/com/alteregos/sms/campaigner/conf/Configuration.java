package com.alteregos.sms.campaigner.conf;

import com.alteregos.sms.campaigner.util.LoggerHelper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author John Emmanuel
 */
public class Configuration {

    private static Logger log = LoggerHelper.getLogger();
    public static final String CONFIG_FILE_NAME = "SmsCampaigner.conf";
    private static Configuration instance = new Configuration();
    private Properties configProperties = null;
    //Properties
    private String comPort = ConfigurationKeys.COM_PORT.defaultValue();
    private String flowControl = ConfigurationKeys.FLOW_CONTROL.defaultValue();
    private String pin = ConfigurationKeys.PIN.defaultValue();
    private String messageFooter = ConfigurationKeys.MESSAGE_FOOTER.defaultValue();
    private boolean callNotificationEnabled = Boolean.parseBoolean(ConfigurationKeys.ENABLE_CALL_NOTIFICATION.defaultValue());
    private boolean longCallNotificationEnabled = Boolean.parseBoolean(ConfigurationKeys.ENABLE_LONG_CALL_NOTIFICATION.defaultValue());
    private boolean messageFooterEnabledForCallNotification = Boolean.parseBoolean(ConfigurationKeys.ENABLE_FOOTER_FOR_CALL_NOTIFICATION.defaultValue());
    private String callNotification = ConfigurationKeys.CALL_NOTIFICATION.defaultValue();
    private boolean defaultMessageEnabled = Boolean.parseBoolean(ConfigurationKeys.ENABLE_DEFAULT_MESSAGE.defaultValue());
    private boolean longDefaultMessageEnabled = Boolean.parseBoolean(ConfigurationKeys.ENABLE_LONG_DEFAULT_MESSAGE.defaultValue());
    private boolean messageFooterEnabledForDefaultMessage = Boolean.parseBoolean(ConfigurationKeys.ENABLE_FOOTER_FOR_DEFAULT_MESSAGE.defaultValue());
    private String defaultMessage = ConfigurationKeys.DEFAULT_MESSAGE.defaultValue();
    private int gatewayPortNumber = Integer.parseInt(ConfigurationKeys.GATEWAY_PORT_NUMBER.defaultValue());
    private boolean gatewayAutoStart = Boolean.parseBoolean(ConfigurationKeys.GATEWAY_AUTO_START.defaultValue());
    private String lookAndFeel = ConfigurationKeys.LOOK_AND_FEEL.defaultValue();

    private Configuration() {
    }

    public Configuration comPort(String comPort) {
        this.comPort = comPort;
        return this;
    }

    public Configuration flowControl(String flowControl) {
        this.flowControl = flowControl;
        return this;
    }

    public Configuration pin(String pin) {
        this.pin = pin;
        return this;
    }

    public Configuration messageFooter(String footer) {
        this.messageFooter = footer;
        return this;
    }

    public Configuration callNotificationEnabled(boolean enabled) {
        this.callNotificationEnabled = enabled;
        return this;
    }

    public Configuration longCallNotificationEnabled(boolean enabled) {
        this.longCallNotificationEnabled = enabled;
        return this;
    }

    public Configuration messageFooterEnabledForCallNotification(boolean enabled) {
        this.messageFooterEnabledForCallNotification = enabled;
        return this;
    }

    public Configuration callNotification(String notification) {
        this.callNotification = notification;
        return this;
    }

    public Configuration defaultMessageEnabled(boolean enabled) {
        this.defaultMessageEnabled = enabled;
        return this;
    }

    public Configuration longDefaultMessageEnabled(boolean enabled) {
        this.longDefaultMessageEnabled = enabled;
        return this;
    }

    public Configuration messageFooterEnabledForDefaultMessage(boolean enabled) {
        this.messageFooterEnabledForDefaultMessage = enabled;
        return this;
    }

    public Configuration defaultMessage(String message) {
        this.defaultMessage = message;
        return this;
    }

    public Configuration gatewayPortNumber(int portNo) {
        this.gatewayPortNumber = portNo;
        return this;
    }

    public Configuration gatewayAutoStart(boolean autoStart) {
        this.gatewayAutoStart = autoStart;
        return this;
    }

    public Configuration lookAndFeel(String lookAndFeel) {
        this.lookAndFeel = lookAndFeel;
        return this;
    }

    public String getCallNotification() {
        return callNotification;
    }

    public boolean isCallNotificationEnabled() {
        return callNotificationEnabled;
    }

    public String getComPort() {
        return comPort;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public boolean isDefaultMessageEnabled() {
        return defaultMessageEnabled;
    }

    public String getFlowControl() {
        return flowControl;
    }

    public boolean isGatewayAutoStart() {
        return gatewayAutoStart;
    }

    public int getGatewayPortNumber() {
        return gatewayPortNumber;
    }

    public boolean isLongCallNotificationEnabled() {
        return longCallNotificationEnabled;
    }

    public boolean isLongDefaultMessageEnabled() {
        return longDefaultMessageEnabled;
    }

    public String getLookAndFeel() {
        return lookAndFeel;
    }

    public String getMessageFooter() {
        return messageFooter;
    }

    public boolean isMessageFooterEnabledForCallNotification() {
        return messageFooterEnabledForCallNotification;
    }

    public boolean isMessageFooterEnabledForDefaultMessage() {
        return messageFooterEnabledForDefaultMessage;
    }

    public String getPin() {
        return pin;
    }

    public static Configuration load(InputStream stream) {
        InputStream inputStream = stream;
        if (inputStream != null) {
            log.debug("Config file found. Reading properties.");
            //Config found load properties into configuration bean
            instance.configProperties = new Properties();
            try {
                instance.configProperties.loadFromXML(inputStream);
                instance.comPort = instance.configProperties.getProperty(ConfigurationKeys.COM_PORT.label(), instance.comPort);
                instance.flowControl = instance.configProperties.getProperty(ConfigurationKeys.FLOW_CONTROL.label(), instance.flowControl);
                instance.pin = instance.configProperties.getProperty(ConfigurationKeys.PIN.label(), instance.pin);
                instance.messageFooter = instance.configProperties.getProperty(ConfigurationKeys.MESSAGE_FOOTER.label(), instance.messageFooter);
                instance.callNotificationEnabled = Boolean.parseBoolean(instance.configProperties.getProperty(ConfigurationKeys.ENABLE_CALL_NOTIFICATION.label()));
                instance.longCallNotificationEnabled = Boolean.parseBoolean(instance.configProperties.getProperty(ConfigurationKeys.ENABLE_LONG_CALL_NOTIFICATION.label()));
                instance.callNotification = instance.configProperties.getProperty(ConfigurationKeys.CALL_NOTIFICATION.label(), instance.callNotification);
                instance.messageFooterEnabledForCallNotification = Boolean.parseBoolean(instance.configProperties.getProperty(ConfigurationKeys.ENABLE_FOOTER_FOR_CALL_NOTIFICATION.label()));
                instance.defaultMessageEnabled = Boolean.parseBoolean(instance.configProperties.getProperty(ConfigurationKeys.ENABLE_DEFAULT_MESSAGE.label()));
                instance.longDefaultMessageEnabled = Boolean.parseBoolean(instance.configProperties.getProperty(ConfigurationKeys.ENABLE_LONG_DEFAULT_MESSAGE.label()));
                instance.messageFooterEnabledForDefaultMessage = Boolean.parseBoolean(instance.configProperties.getProperty(ConfigurationKeys.ENABLE_FOOTER_FOR_DEFAULT_MESSAGE.label()));
                instance.defaultMessage = instance.configProperties.getProperty(ConfigurationKeys.DEFAULT_MESSAGE.label(), instance.defaultMessage);
                instance.gatewayPortNumber(Integer.parseInt(instance.configProperties.getProperty(ConfigurationKeys.GATEWAY_PORT_NUMBER.label(), String.valueOf(instance.getGatewayPortNumber()))));
                instance.gatewayAutoStart(Boolean.parseBoolean(instance.configProperties.getProperty(ConfigurationKeys.GATEWAY_AUTO_START.label())));
                instance.lookAndFeel(instance.configProperties.getProperty(ConfigurationKeys.LOOK_AND_FEEL.label(), instance.getLookAndFeel()));
            } catch (InvalidPropertiesFormatException invalidPropertiesFormatException) {
                log.error("Invalid properties format in config file");
                log.error(invalidPropertiesFormatException);
            } catch (IOException ioException) {
                log.error("IOException when reading config properties");
                log.error(ioException);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable ignore) {
                    }
                }
            }
        } else {
            log.debug("Config file not found");
        }
        return instance;
    }

    public static void save(OutputStream stream) {
        log.debug("Saving config properties");
        if (stream == null) {
            log.error("Output stream to config file is null");
            throw new RuntimeException("Outputstream to Config file is null");
        }
        OutputStream outputStream = stream;
        try {
            instance.configProperties = new Properties();
            instance.configProperties.setProperty(ConfigurationKeys.COM_PORT.label(), instance.getComPort());
            instance.configProperties.setProperty(ConfigurationKeys.FLOW_CONTROL.label(), instance.getFlowControl().equals("") ? ConfigurationKeys.FLOW_CONTROL.defaultValue() : instance.getFlowControl());
            instance.configProperties.setProperty(ConfigurationKeys.PIN.label(), String.valueOf(instance.getPin()));
            instance.configProperties.setProperty(ConfigurationKeys.MESSAGE_FOOTER.label(), instance.getMessageFooter());
            instance.configProperties.setProperty(ConfigurationKeys.ENABLE_CALL_NOTIFICATION.label(), String.valueOf(instance.isCallNotificationEnabled()));
            instance.configProperties.setProperty(ConfigurationKeys.ENABLE_LONG_CALL_NOTIFICATION.label(), String.valueOf(instance.isLongCallNotificationEnabled()));
            instance.configProperties.setProperty(ConfigurationKeys.ENABLE_FOOTER_FOR_CALL_NOTIFICATION.label(), String.valueOf(instance.isMessageFooterEnabledForCallNotification()));
            instance.configProperties.setProperty(ConfigurationKeys.CALL_NOTIFICATION.label(), instance.getCallNotification());
            instance.configProperties.setProperty(ConfigurationKeys.ENABLE_DEFAULT_MESSAGE.label(), String.valueOf(instance.isDefaultMessageEnabled()));
            instance.configProperties.setProperty(ConfigurationKeys.ENABLE_LONG_DEFAULT_MESSAGE.label(), String.valueOf(instance.isLongDefaultMessageEnabled()));
            instance.configProperties.setProperty(ConfigurationKeys.ENABLE_FOOTER_FOR_DEFAULT_MESSAGE.label(), String.valueOf(instance.isMessageFooterEnabledForDefaultMessage()));
            instance.configProperties.setProperty(ConfigurationKeys.DEFAULT_MESSAGE.label(), instance.getDefaultMessage());
            instance.configProperties.setProperty(ConfigurationKeys.GATEWAY_PORT_NUMBER.label(), String.valueOf(instance.getGatewayPortNumber()));
            instance.configProperties.setProperty(ConfigurationKeys.GATEWAY_AUTO_START.label(), String.valueOf(instance.isGatewayAutoStart()));
            instance.configProperties.setProperty(ConfigurationKeys.LOOK_AND_FEEL.label(), String.valueOf(instance.getLookAndFeel()));
            instance.configProperties.storeToXML(outputStream, CONFIG_FILE_NAME);
        } catch (FileNotFoundException ex) {
            log.debug("File not found when trying to save config");
        } catch (IOException ioException) {
            log.error("IO Exception when trying to save config");
            log.error(ioException);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Throwable ignore) {
                }
            }
        }
    }
}
