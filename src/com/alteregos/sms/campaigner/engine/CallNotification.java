package com.alteregos.sms.campaigner.engine;

import com.alteregos.sms.campaigner.engine.receivers.CallReceiver;
import org.smslib.ICallNotification;

/**
 *
 * @author John Emmanuel
 */
public class CallNotification implements ICallNotification {

    public CallNotification() {
    }

    @Override
    public void process(String gtwId, String callerId) {
        CallReceiver receiver = new CallReceiver();
        receiver.receive(gtwId, callerId);
    }
}
