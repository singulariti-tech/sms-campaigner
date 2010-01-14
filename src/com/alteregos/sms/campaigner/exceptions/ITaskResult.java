package com.alteregos.sms.campaigner.exceptions;

/**
 *
 * @author John Emmanuel
 */
public interface ITaskResult {
    ResultMessage getResultMessage();
    boolean isSuccessful();
}
