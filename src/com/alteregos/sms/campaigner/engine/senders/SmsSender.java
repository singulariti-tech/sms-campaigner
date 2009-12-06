package com.alteregos.sms.campaigner.engine.senders;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.engine.ISender;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import com.alteregos.sms.campaigner.business.MessagePriority;
import com.alteregos.sms.campaigner.business.MessageStatus;
import com.alteregos.sms.campaigner.data.dto.OutgoingMessage;
import com.alteregos.sms.campaigner.services.ContactService;
import com.alteregos.sms.campaigner.services.MessageService;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.smslib.MessagePriorities;
import org.smslib.OutboundMessage;
import org.smslib.Service;

/**
 *
 * @author John Emmanuel
 */
public class SmsSender implements ISender {

    private static Logger log = LoggerHelper.getLogger();
    private Service service;
    private MessageService messageService;

    public SmsSender() {
        messageService = Main.getApplication().getBean("messageService");
        //outboxQuery = entityManager.createQuery("SELECT o FROM OutgoingMessage o WHERE o.status =  ?1");
        //outboxQuery.setParameter(1, MessageStatus.UNSENT.toString());
    }

    public SmsSender(Service service) {
        this();
        this.service = service;
    }

    @Override
    public void send() {
        log.debug("Starting to queue messages");
        List<OutgoingMessage> messagesToBeSent = getMessagesToBeSent();
        List<OutgoingMessage> highPriorityList = getMessagesOfPriority(messagesToBeSent, MessagePriority.HIGH);
        messagesToBeSent.removeAll(highPriorityList);
        List<OutgoingMessage> normalPriorityList = getMessagesOfPriority(messagesToBeSent, MessagePriority.NORMAL);
        messagesToBeSent.removeAll(normalPriorityList);
        List<OutgoingMessage> lowPriorityList = getMessagesOfPriority(messagesToBeSent, MessagePriority.LOW);
        messagesToBeSent.removeAll(lowPriorityList);
        queuePriorityList(highPriorityList, MessagePriorities.HIGH);
        queuePriorityList(normalPriorityList, MessagePriorities.NORMAL);
        queuePriorityList(lowPriorityList, MessagePriorities.LOW);
    }

    @Override
    public void setService(Service service) {
        this.service = service;
    }

    private List<OutgoingMessage> getMessagesOfPriority(List<OutgoingMessage> inputList, MessagePriority priority) {
        List<OutgoingMessage> outputList = new ArrayList<OutgoingMessage>();
        for (OutgoingMessage message : inputList) {
            if (message.getPriority().equals(priority)) {
                outputList.add(message);
            }
        }
        return outputList;
    }

    private void queuePriorityList(List<OutgoingMessage> outboxList, MessagePriorities priority) {
        for (int i = 0; i < outboxList.size(); i++) {
            OutgoingMessage message = outboxList.get(i);
            message.setStatus(MessageStatus.QUEUED);
            OutboundMessage outboundMessage = new OutboundMessage(message.getRecepientNo(), message.getContent());
            outboundMessage.setId(String.valueOf(message.getOutgoingMessageId()));
            outboundMessage.setPriority(priority);

            //UNCOMMENT THE FOLLOWING FOR MORE FEATURES
            /*            
            if(message.getSender() != null && !message.getSender().equals("")) {
            outboundMessage.setFrom(message.getSender());
            }            
            outboundMessage.setStatusReport(message.getStatusReport());
            outboundMessage.setFlashSms(message.getFlashSms());
            if(message.getDstPort() != -1) {
            outboundMessage.setDstPort(message.getDstPort());
            outboundMessage.setSrcPort(message.getSrcPort());
            }*/
            this.service.queueMessage(outboundMessage);

            try {
                messageService.updateOutgoingMessage(message);
            } catch (Exception rollbackException) {
                log.debug("Error when queueing messages");
                log.debug(rollbackException);
            }
        }
    }

    private List<OutgoingMessage> getMessagesToBeSent() {
        return messageService.getOutgoingMessages(MessageStatus.UNSENT);
    }
}
