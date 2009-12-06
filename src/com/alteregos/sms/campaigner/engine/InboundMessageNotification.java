package com.alteregos.sms.campaigner.engine;

import com.alteregos.sms.campaigner.engine.receivers.InboundMessageReceiver;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
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

    private static Logger log = LoggerHelper.getLogger();
    private Service service;

    public InboundMessageNotification() {
    }

    public InboundMessageNotification(Service service) {
        this.service = service;
    }

    @Override
    public void process(String gtwId, MessageTypes msgType, String memLoc, int memIndex) {
        log.debug("Processing incoming SMS");
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
                log.error("Exception when processing incoming SMS");
                log.error(e);
            }
        }

        //Process inbound messages        
        InboundMessageReceiver receiver = new InboundMessageReceiver();
        receiver.receive(inboundMessages, service);
    }

    public void setService(Service service) {
        this.service = service;
    }
}
