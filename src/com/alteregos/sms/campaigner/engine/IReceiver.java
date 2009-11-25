/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.engine;

import com.alteregos.sms.campaigner.engine.processors.CallProcessor;
import com.alteregos.sms.campaigner.engine.processors.SmsProcessor;

/**
 *
 * @author John Emmanuel
 */
public interface IReceiver extends ISmsService {

    void setCallProcessor(CallProcessor callProcessor);

    void setSmsProcessor(SmsProcessor smsProcessor);

    void receive();
}
