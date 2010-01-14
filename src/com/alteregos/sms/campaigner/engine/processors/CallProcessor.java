package com.alteregos.sms.campaigner.engine.processors;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.dto.IncomingCall;
import com.alteregos.sms.campaigner.data.dto.OutgoingMessage;
import com.alteregos.sms.campaigner.engine.IProcessor;
import com.alteregos.sms.campaigner.services.IncomingCallService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John Emmanuel
 */
public class CallProcessor implements IProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallProcessor.class);
    private String notificationMessage;
    private IncomingCallService callService;

    public CallProcessor() {
        LOGGER.debug("** CallProcessor()");
        callService = Main.getApplication().getBean("incomingCallService");
    }

    @Override
    public void process() {
        LOGGER.debug(">> process()");
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
            LOGGER.error("-- Exception when processing Calls: {}", rollbackException);
        }
        LOGGER.debug("<< process()");
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
