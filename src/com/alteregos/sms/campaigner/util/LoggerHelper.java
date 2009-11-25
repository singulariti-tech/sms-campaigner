/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.util;

import org.apache.log4j.Logger;

/**
 * (Taken from Spiffy Framework)
 * @author Kasper B. Graversen
 */
public class LoggerHelper {

    public static Logger getLogger() {
        final Throwable t = new Throwable();
        t.fillInStackTrace();
        return Logger.getLogger(t.getStackTrace()[1].getClassName());
    }
}
