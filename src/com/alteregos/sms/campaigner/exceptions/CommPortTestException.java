/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.exceptions;

/**
 *
 * @author John Emmanuel
 */
public class CommPortTestException extends Exception {

    public CommPortTestException(Throwable cause) {
        super(cause);
    }

    public CommPortTestException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommPortTestException(String message) {
        super(message);
    }

    public CommPortTestException() {
    }
}
