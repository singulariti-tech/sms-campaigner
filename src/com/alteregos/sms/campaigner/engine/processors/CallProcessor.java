/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.engine.processors;

import com.alteregos.sms.campaigner.data.beans.Calls;
import com.alteregos.sms.campaigner.data.beans.Outbox;
import com.alteregos.sms.campaigner.engine.IProcessor;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import org.apache.log4j.Logger;

/**
 *
 * @author John Emmanuel
 */
public class CallProcessor implements IProcessor {

    private static Logger log = LoggerHelper.getLogger();
    private EntityManager entityManager;
    private Query callsQuery;
    private String notificationMessage;

    public CallProcessor() {
        entityManager = javax.persistence.Persistence.createEntityManagerFactory("absolute-smsPU").createEntityManager();
        callsQuery = entityManager.createQuery("SELECT c FROM Calls c");
    }

    @Override
    public void process() {
        log.debug("Processing calls...");
        List<Calls> callsList = filter(callsQuery.getResultList());
        List<Outbox> bulkSms = new ArrayList<Outbox>();
        for (Calls call : callsList) {
            Outbox outbox = new Outbox();
            outbox.setContent(notificationMessage);
            outbox.setRecepient(call.getCaller());
            bulkSms.add(outbox);
            call.setProcess(true);
        }

        try {
            entityManager.getTransaction().begin();
            for (Outbox sms : bulkSms) {
                entityManager.persist(sms);
            }
            entityManager.getTransaction().commit();
        } catch (RollbackException rollbackException) {
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

    private List<Calls> filter(List<Calls> inputCalls) {
        List<Calls> outputCalls = new ArrayList<Calls>();
        for (Calls call : inputCalls) {
            if (!call.getProcess()) {
                outputCalls.add(call);
            }
        }
        return outputCalls;
    }
}
