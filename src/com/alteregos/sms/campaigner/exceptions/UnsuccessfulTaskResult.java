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

    @Override
    public ResultMessage getResultMessage() {
        return errorCode;
    }

    @Override
    public boolean isSuccessful() {
        return SUCCESS;
    }
}
