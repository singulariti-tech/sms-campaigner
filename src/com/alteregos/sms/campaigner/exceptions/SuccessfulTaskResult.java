/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.exceptions;

/**
 *
 * @author John Emmanuel
 */
public class SuccessfulTaskResult implements ITaskResult {

    private static final boolean SUCCESS = true;
    private ResultMessage resultMessage = ResultMessage.SUCCESS;

    public SuccessfulTaskResult() {
    }

    public SuccessfulTaskResult(ResultMessage message) {
    }

    public ResultMessage getResultMessage() {
        return resultMessage;
    }

    public boolean isSuccessful() {
        return SUCCESS;
    }
}
