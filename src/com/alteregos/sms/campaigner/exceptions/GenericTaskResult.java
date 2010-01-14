package com.alteregos.sms.campaigner.exceptions;

/**
 *
 * @author john.emmanuel
 */
public class GenericTaskResult implements ITaskResult {

    private ResultMessage resultMessage;
    private boolean success;

    public GenericTaskResult() {
    }

    public GenericTaskResult(ResultMessage resultMessage, boolean isSuccessful) {
        this.resultMessage = resultMessage;
        this.success = isSuccessful;
    }

    public GenericTaskResult(ResultMessage resultMessage, boolean success, String message) {
        this.resultMessage = resultMessage;
        this.success = success;
        setMessage(message);
    }

    @Override
    public ResultMessage getResultMessage() {
        return resultMessage;
    }

    @Override
    public boolean isSuccessful() {
        return success;
    }

    public void setMessage(String message) {
        this.resultMessage.setLabel(message);
    }
}
