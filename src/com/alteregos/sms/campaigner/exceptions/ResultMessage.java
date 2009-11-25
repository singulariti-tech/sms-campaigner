/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.exceptions;

/**
 *
 * @author John Emmanuel
 */
public enum ResultMessage {

    SUCCESS("Operation successful ", 0),
    DUPLICATE_ENTRY("A duplicate entry already exists", 1062),
    GENERIC_MESSAGE("Error occured", 1),
    VALIDATION_FAILED("Validation failed", 2);
    private String label;
    private int code;

    ResultMessage(String label, int code) {
        this.label = label;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static ResultMessage getCode(int code) {
        for (ResultMessage errorCode : values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return ResultMessage.SUCCESS;
    }

    public static ResultMessage getCode(String label) {
        for (ResultMessage errorCode : values()) {
            if (errorCode.getLabel().equals(label)) {
                return errorCode;
            }
        }
        return ResultMessage.SUCCESS;
    }
}
