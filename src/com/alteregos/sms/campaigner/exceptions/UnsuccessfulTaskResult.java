/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.exceptions;

/**
 *
 * @author John Emmanuel
 */
public class UnsuccessfulTaskResult implements ITaskResult {

    private final boolean SUCCESS = false;
    private ResultMessage errorCode;

    public UnsuccessfulTaskResult() {
    }

    public UnsuccessfulTaskResult(ResultMessage errorCode) {
        this.errorCode = errorCode;
    }

    public ResultMessage getResultMessage() {
        return errorCode;
    }

    public boolean isSuccessful() {
        return SUCCESS;
    }
}
