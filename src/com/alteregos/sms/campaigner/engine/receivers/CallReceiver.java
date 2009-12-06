package com.alteregos.sms.campaigner.engine.receivers;

import com.alteregos.sms.campaigner.data.dto.IncomingCall;
import com.alteregos.sms.campaigner.services.IncomingCallService;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author John Emmanuel
 */
public class CallReceiver {

    private static Logger log = LoggerHelper.getLogger();
    private IncomingCallService callService;

    public CallReceiver() {
    }

    public void receive(String gatewayId, String callerId) {
        log.error("Call received from " + callerId);
        IncomingCall call = new IncomingCall();
        call.setCallerNo(callerId);
        call.setGatewayId(gatewayId);
        call.setProcess(false);
        call.setReceiptDate(new Date());
        try {
            callService.newIncomingCall(call);
            System.out.println("Call from " + callerId + " logged.");
        } catch (Exception rollbackException) {
            log.error("Error when processing Call from " + callerId);
            log.error(rollbackException);
        }
    }
}
