package com.alteregos.sms.campaigner.engine.processors;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.dto.IncomingCall;
import com.alteregos.sms.campaigner.data.dto.OutgoingMessage;
import com.alteregos.sms.campaigner.engine.IProcessor;
import com.alteregos.sms.campaigner.services.IncomingCallService;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author John Emmanuel
 */
public class CallProcessor implements IProcessor {

    private static Logger log = LoggerHelper.getLogger();
    private String notificationMessage;
    private IncomingCallService callService;

    public CallProcessor() {
        callService = Main.getApplication().getBean("incomingCallService");
    }

    @Override
    public void process() {
        log.debug("Processing calls...");
        List<IncomingCall> callsList = filter(callService.findAll());
        List<OutgoingMessage> bulkSms = new ArrayList<OutgoingMessage>();
        for (IncomingCall call : callsList) {
            OutgoingMessage outbox = new OutgoingMessage();
            outbox.setContent(notificationMessage);
            outbox.setRecepientNo(call.getCallerNo());
            bulkSms.add(outbox);
            call.setProcess(true);
        }

        try {
            //TODO Insert or update calls appropriately
        } catch (Exception rollbackException) {
            log.error("Exception when processing Calls");
            log.error(rollbackException);
        }
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    private List<IncomingCall> filter(List<IncomingCall> inputCalls) {
        List<IncomingCall> outputCalls = new ArrayList<IncomingCall>();
        for (IncomingCall call : inputCalls) {
            if (!call.isProcess()) {
                outputCalls.add(call);
            }
        }
        return outputCalls;
    }
}
