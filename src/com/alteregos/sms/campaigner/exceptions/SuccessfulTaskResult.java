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

    @Override
    public ResultMessage getResultMessage() {
        return resultMessage;
    }

    @Override
    public boolean isSuccessful() {
        return SUCCESS;
    }
}
