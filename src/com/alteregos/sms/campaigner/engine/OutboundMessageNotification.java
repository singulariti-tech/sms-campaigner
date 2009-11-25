package com.alteregos.sms.campaigner.engine;

import com.alteregos.sms.campaigner.business.FailureCause;
import com.alteregos.sms.campaigner.data.beans.Outbox;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import com.alteregos.sms.campaigner.business.MessageStatus;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import org.apache.log4j.Logger;
import org.smslib.IOutboundMessageNotification;
import org.smslib.MessageStatuses;
import org.smslib.OutboundMessage;

/**
 *
 * @author John Emmanuel
 */
public class OutboundMessageNotification implements IOutboundMessageNotification {

    private static Logger log = LoggerHelper.getLogger();
    private EntityManager entityManager;
    private Query messageQuery;
    private final int MAX_TRIES = 2;

    public OutboundMessageNotification() {
        entityManager = javax.persistence.Persistence.createEntityManagerFactory("absolute-smsPU").createEntityManager();
        messageQuery = entityManager.createQuery("SELECT o FROM Outbox o WHERE o.outboxId = ?1");
    }

    public void process(String gtwId, OutboundMessage outboundMessage) {
        String messageId = outboundMessage.getId();
        Outbox outboxMessage = getMessage(messageId);
        if (outboundMessage == null) {
            log.warn("Outbox message with id: " + messageId + " not found. Most probably deleted");
        }

        if (outboxMessage != null && outboundMessage.getMessageStatus().equals(MessageStatuses.SENT)) {
            outboxMessage.setSentDate(outboundMessage.getDispatchDate());
            outboxMessage.setStatus(MessageStatus.SUCCESSFULLY_SENT.toString());
            outboxMessage.setRefNo(outboundMessage.getRefNo());
            outboxMessage.setGatewayId(outboundMessage.getGatewayId());
            log.debug("Message sent successfully");
        } else if (outboxMessage != null && outboundMessage.getMessageStatus().equals(MessageStatuses.FAILED)) {
            FailureCause cause = FailureCause.getFailureCause(outboundMessage.getFailureCause());
            log.error("Sending message to recepient " + outboundMessage.getRecipient() + " failed. Reason - " + cause.getLabel());
            int noTries = outboxMessage.getErrors() + 1;
            if (noTries > MAX_TRIES) {
                outboxMessage.setErrors((short) noTries);
                outboxMessage.setStatus(MessageStatus.FAILED.toString());
            } else {
                outboxMessage.setErrors((short) noTries);
                outboxMessage.setStatus(MessageStatus.UNSENT.toString());
            }
        }

        try {
            entityManager.getTransaction().begin();
            entityManager.getTransaction().commit();
        } catch (RollbackException rollbackException) {
            log.error("Exception when processing outbound message");
            log.error(rollbackException);
        }
    }

    private Outbox getMessage(String messageId) {
        Integer id = Integer.parseInt(messageId);
        messageQuery.setParameter(1, id);
        Outbox message = null;
        List<Outbox> outboxList = messageQuery.getResultList();
        if (outboxList.size() == 1) {
            message = outboxList.get(0);
        }
        return message;
    }
}
