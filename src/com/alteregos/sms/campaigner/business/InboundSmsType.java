/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.business;

/**
 *
 * @author John Emmanuel
 */
public enum InboundSmsType {

    USER_MESSAGE("I"),
    STATUS_REPORT("S");
    private String label;

    InboundSmsType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static InboundSmsType getType(String label) {
        for (InboundSmsType type : values()) {
            if (type.getLabel().equals(label)) {
                return type;
            }
        }
        return null;
    }
}
