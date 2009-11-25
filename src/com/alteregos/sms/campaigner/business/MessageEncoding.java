/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.business;

/**
 *
 * @author John Emmanuel
 */
public enum MessageEncoding {

    SEVEN_BIT("7"),
    EIGHT_BIT("8"),
    UNICODE_UCS2("U");
    private String label;

    MessageEncoding(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static MessageEncoding getEncoding(String label) {
        for (MessageEncoding encoding : values()) {
            if (encoding.getLabel().equals(label)) {
                return encoding;
            }
        }
        return null;
    }
}
