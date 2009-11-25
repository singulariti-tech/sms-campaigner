/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.business;

/**
 *
 * @author John Emmanuel
 */
public enum OutboundSmsType {

    AUTO_REPLY("A", "Auto Reply"),
    BULK("B", "Bulk");
    private String code;
    private String message;

    OutboundSmsType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static OutboundSmsType getType(String code) {
        for (OutboundSmsType smsType : values()) {
            if (smsType.getCode().equals(code)) {
                return smsType;
            }
        }
        return null;
    }
}
