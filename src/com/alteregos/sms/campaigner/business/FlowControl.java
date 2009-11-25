package com.alteregos.sms.campaigner.business;

import javax.comm.SerialPort;

/**
 *
 * @author John Emmanuel
 */
public enum FlowControl {

    NONE("None", SerialPort.FLOWCONTROL_NONE),
    RTSCTS_IN("RTSCTS IN", SerialPort.FLOWCONTROL_RTSCTS_IN),
    RTSCTS_OUT("RTSCTS OUT", SerialPort.FLOWCONTROL_RTSCTS_OUT),
    XONXOFF_IN("XONXOFF IN", SerialPort.FLOWCONTROL_XONXOFF_IN),
    XON_XOFF_OUT("XONXOFF OUT", SerialPort.FLOWCONTROL_XONXOFF_OUT);
    private String label;
    private int code;

    FlowControl(String label, int code) {
        this.label = label;
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public String getLabel() {
        return this.label;
    }

    public static FlowControl getFlowControl(String label) {
        for (FlowControl fc : values()) {
            if (fc.getLabel().equals(label)) {
                return fc;
            }
        }
        return null;
    }

    public static FlowControl getFlowControl(int code) {
        for (FlowControl fc : values()) {
            if (fc.getCode() == code) {
                return fc;
            }
        }
        return null;
    }
}
