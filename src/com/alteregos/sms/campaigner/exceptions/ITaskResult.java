/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.exceptions;

/**
 *
 * @author John Emmanuel
 */
public interface ITaskResult {

    ResultMessage getResultMessage();

    boolean isSuccessful();
}
