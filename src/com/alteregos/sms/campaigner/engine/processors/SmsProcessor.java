/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.engine.processors;

import com.alteregos.sms.campaigner.data.beans.Dnd;
import com.alteregos.sms.campaigner.business.InboundSmsType;
import com.alteregos.sms.campaigner.data.beans.Inbox;
import com.alteregos.sms.campaigner.business.OutboundSmsType;
import com.alteregos.sms.campaigner.data.beans.Outbox;
import com.alteregos.sms.campaigner.engine.processors.helpers.RuleProcessResult;
import com.alteregos.sms.campaigner.engine.IProcessor;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import org.apache.log4j.Logger;

/**
 *
 * @author John Emmanuel
 */
public class SmsProcessor implements IProcessor {

    private static Logger log = LoggerHelper.getLogger();
    private EntityManager entityManager;
    private Query inboxQuery;
    private String defaultMessage;

    public SmsProcessor() {
        entityManager = javax.persistence.Persistence.createEntityManagerFactory("absolute-smsPU").createEntityManager();
        inboxQuery = entityManager.createQuery("SELECT i FROM Inbox i");
    }

    public void process() {
        log.debug("Processing messages received");
        RuleProcessor ruleProcessor = new RuleProcessor();
        List<Inbox> inboxList = filter(inboxQuery.getResultList());
        List<Outbox> outboxList = new ArrayList<Outbox>();
        for (Inbox sms : inboxList) {
            RuleProcessResult result = ruleProcessor.process(sms.getContent());
            String content = result.getContent();
            String reference = null;
            if (result.isRule()) {
                if (result.isDndRule()) {
                    reference = registerDnd(sms.getSender());
                    content = (reference != null) ? content + reference : content;
                }

                if (!content.equals("") && (result.isDndRule() && reference != null)) {
                    log.debug("Queueing message in outbox");
                    Outbox reply = new Outbox();
                    reply.setRecepient(sms.getSender());
                    reply.setContent(content);
                    reply.setType(OutboundSmsType.AUTO_REPLY.getCode());
                    outboxList.add(reply);
                    sms.setProcess(true);
                } else if (result.isDndRule() && reference == null) {
                    log.debug("Requester already registered");
                    Outbox reply = new Outbox();
                    reply.setRecepient(sms.getSender());
                    reply.setContent("You are already registered on our DND list.");
                    reply.setType(OutboundSmsType.AUTO_REPLY.getCode());
                    outboxList.add(reply);
                    sms.setProcess(true);
                } else {
                    if (defaultMessage != null) {
                        log.debug("Sending default message");
                        Outbox reply = new Outbox();
                        reply.setRecepient(sms.getSender());
                        reply.setContent(getDefaultMessage());
                        outboxList.add(reply);
                        sms.setProcess(true);
                    } else {
                        log.info("Default message disabled...so will not send sms");
                        log.info("Details of the message: " + sms.getSender() + " - " + sms.getContent());
                    }
                }
                entityManager.merge(sms);
            } else {
                log.debug("This is most probably a message received from vendors or the like. Will not process it");
            }
        }

        try {
            entityManager.getTransaction().begin();
            for (Outbox sms : outboxList) {
                entityManager.persist(sms);
            }
            entityManager.getTransaction().commit();
        } catch (RollbackException rollbackException) {
            log.error("Error when processing messages received");
            log.error(rollbackException);
        }
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    private List<Inbox> filter(List<Inbox> inputList) {
        List<Inbox> outputList = new ArrayList<Inbox>();
        for (Inbox sms : inputList) {
            if (!sms.getType().equals(InboundSmsType.STATUS_REPORT.getLabel()) && !sms.getProcess()) {
                outputList.add(sms);
            }
        }
        return outputList;
    }

    private String registerDnd(String mobileNo) {
        boolean success = false;
        Dnd dnd = new Dnd();
        dnd.setMobileNo(mobileNo);
        dnd.setRegisteredDate(new Date());
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(dnd);
            entityManager.getTransaction().commit();
            success = true;
        } catch (RollbackException rollbackException) {
            log.error("Error when registering DND");
            log.error("Mobile no. of registrant: " + mobileNo);
            log.error("Error details: " + rollbackException);
            success = false;
        }

        String reference = success ? String.valueOf(dnd.getDndId()) : null;
        return reference;
    }
}
