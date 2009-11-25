/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.engine.senders;

import com.alteregos.sms.campaigner.data.beans.Outbox;
import com.alteregos.sms.campaigner.engine.ISender;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import com.alteregos.sms.campaigner.business.MessagePriority;
import com.alteregos.sms.campaigner.business.MessageStatus;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;
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
    private EntityManager entityManager;
    private Query outboxQuery;

    public SmsSender() {
        entityManager = javax.persistence.Persistence.createEntityManagerFactory("absolute-smsPU").createEntityManager();
        outboxQuery = entityManager.createQuery("SELECT o FROM Outbox o WHERE o.status =  ?1");
        outboxQuery.setParameter(1, MessageStatus.UNSENT.toString());
    }

    public SmsSender(Service service) {
        this();
        this.service = service;
    }

    public void send() {
        log.debug("Starting to queue messages");
        List<Outbox> messagesToBeSent = getMessagesToBeSent();
        List<Outbox> highPriorityList = getMessagesOfPriority(messagesToBeSent, MessagePriority.HIGH);
        messagesToBeSent.removeAll(highPriorityList);
        List<Outbox> normalPriorityList = getMessagesOfPriority(messagesToBeSent, MessagePriority.NORMAL);
        messagesToBeSent.removeAll(normalPriorityList);
        List<Outbox> lowPriorityList = getMessagesOfPriority(messagesToBeSent, MessagePriority.LOW);
        messagesToBeSent.removeAll(lowPriorityList);
        queuePriorityList(highPriorityList, MessagePriorities.HIGH);
        queuePriorityList(normalPriorityList, MessagePriorities.NORMAL);
        queuePriorityList(lowPriorityList, MessagePriorities.LOW);
    }

    public void setService(Service service) {
        this.service = service;
    }

    private List<Outbox> getMessagesOfPriority(List<Outbox> inputList, MessagePriority priority) {
        List<Outbox> outputList = new ArrayList<Outbox>();
        String priorityString = priority.toString();
        for (Outbox message : inputList) {
            if (message.getPriority().equals(priorityString)) {
                outputList.add(message);
            }
        }
        return outputList;
    }

    private void queuePriorityList(List<Outbox> outboxList, MessagePriorities priority) {
        for (int i = 0; i < outboxList.size(); i++) {
            Outbox message = outboxList.get(i);
            message.setStatus(MessageStatus.QUEUED.toString());
            OutboundMessage outboundMessage = new OutboundMessage(message.getRecepient(), message.getContent());
            outboundMessage.setId(String.valueOf(message.getOutboxId()));
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
                entityManager.getTransaction().begin();
                entityManager.getTransaction().commit();
            } catch (RollbackException rollbackException) {
                log.debug("Error when queueing messages");
                log.debug(rollbackException);
            }
        }
    }

    private List<Outbox> getMessagesToBeSent() {
        List<Outbox> input = outboxQuery.getResultList();
        for (Outbox sms : input) {
            entityManager.refresh(sms);
        }
        return filter(input);
    }

    private List<Outbox> filter(List<Outbox> inputList) {
        List<Outbox> outputList = new ArrayList<Outbox>();
        for (Outbox sms : inputList) {
            if (sms.getStatus().equals(MessageStatus.UNSENT.toString())) {
                outputList.add(sms);
            }
        }
        return outputList;
    }
}
