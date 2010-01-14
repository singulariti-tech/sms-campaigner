package com.alteregos.sms.campaigner.engine;

import com.alteregos.sms.campaigner.engine.receivers.InboundMessageReceiver;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.IInboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.MessageClasses;
import org.smslib.MessageTypes;
import org.smslib.Service;

/**
 *
 * @author John Emmanuel
 */
public class InboundMessageNotification implements IInboundMessageNotification {

    private static final Logger LOGGER = LoggerFactory.getLogger(InboundMessageNotification.class);
    private Service service;

    public InboundMessageNotification() {
        LOGGER.debug("** InboundMessageNotification()");
    }

    public InboundMessageNotification(Service service) {
        LOGGER.debug("** InboundMessageNotification(service)");
        this.service = service;
    }

    @Override
    public void process(String gtwId, MessageTypes msgType, String memLoc, int memIndex) {
        LOGGER.debug(">> process()");
        List<InboundMessage> inboundMessages = new ArrayList<InboundMessage>();
        List<InboundMessage> messageList = new ArrayList<InboundMessage>();
        if (msgType.equals(MessageTypes.INBOUND) || msgType.equals(MessageTypes.STATUSREPORT)) {
            try {
                this.service.readMessages(messageList, MessageClasses.UNREAD, gtwId);
                for (int i = 0; i < messageList.size(); i++) {
                    InboundMessage inboundMessage = messageList.get(i);
                    inboundMessages.add(inboundMessage);
                }
            } catch (Exception e) {
                LOGGER.error("-- Exception when processing incoming SMS: {}", e);
            }
        }

        //Process inbound messages        
        InboundMessageReceiver receiver = new InboundMessageReceiver();
        receiver.receive(inboundMessages, service);
        LOGGER.debug("<< process()");
    }

    public void setService(Service service) {
        this.service = service;
    }
}
