package com.alteregos.sms.campaigner.conf;

import com.alteregos.sms.campaigner.business.FlowControl;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import com.alteregos.sms.campaigner.util.LookAndFeel;
import org.apache.log4j.Logger;

/**
 *
 * @author John Emmanuel
 */
public enum ConfigurationKeys {

    COM_PORT("COM-PORT", ""),
    FLOW_CONTROL("FLOW-CONTROL", String.valueOf(FlowControl.NONE.getCode())),
    PIN("PIN", "0000"),
    MESSAGE_FOOTER("MESSAGE-FOOTER", "Enter custom message"),
    ENABLE_CALL_NOTIFICATION("ENABLE-CALL-NOTIFICATION", "false"),
    ENABLE_LONG_CALL_NOTIFICATION("CALL-NOTIFICATION-LONG-MESSAGE", "false"),
    ENABLE_FOOTER_FOR_CALL_NOTIFICATION("ENABLE-FOOTER-FOR-CALL-NOTIFICATION", "false"),
    CALL_NOTIFICATION("CALL-NOTIFICATION", "Enter custom message"),
    ENABLE_DEFAULT_MESSAGE("ENABLE-DEFAULT-MESSAGE", "false"),
    ENABLE_LONG_DEFAULT_MESSAGE("ENABLE-LONG-DEFAULT-MESSAGE", "false"),
    ENABLE_FOOTER_FOR_DEFAULT_MESSAGE("ENABLE-FOOTER-FOR-DEFAULT-MESSAGE", "false"),
    DEFAULT_MESSAGE("DEFAULT-MESSAGE", "Enter custom message"),
    GATEWAY_PORT_NUMBER("GATEWAY-PORT-NUMBER", "49152"),
    GATEWAY_AUTO_START("GATEWAY-AUTO-START", "true"),
    LOOK_AND_FEEL("LOOK-AND-FEEL", LookAndFeel.CREME.getClassName());
    /**
     * 
     */
    private static Logger log = LoggerHelper.getLogger();
    private String label;
    private String defaultValue;

    ConfigurationKeys(String label, String defaultValue) {
        this.label = label;
        this.defaultValue = defaultValue;
    }

    public String label() {
        return this.label;
    }

    public String defaultValue() {
        return this.defaultValue;
    }

    public static ConfigurationKeys getKey(String label) {
        for (ConfigurationKeys key : values()) {
            if (key.label().equals(label)) {
                return key;
            }
        }
        log.debug("Configuration key for label '" + label + "' not found");
        return null;
    }
}
