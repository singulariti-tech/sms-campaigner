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
