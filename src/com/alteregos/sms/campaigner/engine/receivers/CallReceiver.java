package com.alteregos.sms.campaigner.engine.receivers;

import com.alteregos.sms.campaigner.data.dto.IncomingCall;
import com.alteregos.sms.campaigner.services.IncomingCallService;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John Emmanuel
 */
public class CallReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallReceiver.class);
    private IncomingCallService callService;

    public CallReceiver() {
    }

    public void receive(String gatewayId, String callerId) {
        LOGGER.debug(">> receive()");
        LOGGER.info("-- received call from {}", callerId);
        IncomingCall call = new IncomingCall();
        call.setCallerNo(callerId);
        call.setGatewayId(gatewayId);
        call.setProcess(false);
        call.setReceiptDate(new Date());
        try {
            callService.newIncomingCall(call);
        } catch (Exception rollbackException) {
            LOGGER.error("-- Error when processing Call from {}", callerId);
            LOGGER.error("-- Error: {}" + rollbackException);
        }
        LOGGER.debug("<< receive()");
    }
}
