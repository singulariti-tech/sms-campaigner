package com.alteregos.sms.campaigner.engine.receivers;

import com.alteregos.sms.campaigner.business.InboundSmsType;
import com.alteregos.sms.campaigner.data.beans.Inbox;
import com.alteregos.sms.campaigner.business.MessageEncoding;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import org.apache.log4j.Logger;
import org.smslib.GatewayException;
import org.smslib.InboundMessage;
import org.smslib.MessageEncodings;
import org.smslib.MessageTypes;
import org.smslib.Service;
import org.smslib.TimeoutException;

/**
 *
 * @author John Emmanuel
 */
public class InboundMessageReceiver {

    private static Logger log = LoggerHelper.getLogger();
    private EntityManager entityManager;

    public InboundMessageReceiver() {
        entityManager = javax.persistence.Persistence.createEntityManagerFactory("absolute-smsPU").createEntityManager();
    }

    public void receive(List<InboundMessage> inboundMessages, Service service) {
        List<Inbox> inboxList = new ArrayList<Inbox>();
        for (InboundMessage msg : inboundMessages) {
            String sender = msg.getOriginator();
            String content = msg.getText().replaceAll("'", "''");
            String gatewayId = msg.getGatewayId();
            Date receiptDate = new Date();
            Date messageDate = null;
            if (msg.getDate() != null) {
                messageDate = msg.getDate();
            }
            String encoding = null;
            if (msg.getEncoding().equals(MessageEncodings.ENC7BIT)) {
                encoding = MessageEncoding.SEVEN_BIT.getLabel();
            } else if (msg.getEncoding().equals(MessageEncodings.ENC8BIT)) {
                encoding = MessageEncoding.EIGHT_BIT.getLabel();
            } else if (msg.getEncoding().equals(MessageEncodings.ENCUCS2)) {
                encoding = MessageEncoding.UNICODE_UCS2.getLabel();
            }
            String type = null;
            if (msg.getType().equals(MessageTypes.INBOUND)) {
                type = InboundSmsType.USER_MESSAGE.getLabel();
            } else if (msg.getType().equals(MessageTypes.STATUSREPORT)) {
                type = InboundSmsType.STATUS_REPORT.getLabel();
            }
            Inbox sms = new Inbox();
            sms.setContent(content);
            sms.setSender(sender);
            sms.setEncoding(encoding);
            sms.setMessageDate(messageDate);
            sms.setReceiptDate(receiptDate);
            sms.setType(type);
            sms.setGatewayId(gatewayId);

            inboxList.add(sms);
        }

        boolean commited = false;
        try {
            entityManager.getTransaction().begin();
            for (Inbox sms : inboxList) {
                entityManager.persist(sms);
            }
            entityManager.getTransaction().commit();
            commited = true;
        } catch (RollbackException rollbackException) {
            log.error("Error when receiving inbound message");
            log.error(rollbackException);
            commited = false;
        } finally {
            if (commited) {
                for (InboundMessage message : inboundMessages) {
                    try {
                        service.deleteMessage(message);
                    } catch (TimeoutException ex) {
                        log.error("Timeout when trying to delete messages on SIM");
                        log.error(ex);
                    } catch (GatewayException ex) {
                        log.error("Gateway exception when trying to delete messages on SIM");
                        log.error(ex);
                    } catch (IOException ex) {
                        log.error("IO Exception when trying to delete messages on SIM");
                        log.error(ex);
                    } catch (InterruptedException ex) {
                        log.error("Interrupted exception when trying to delete messages on SIM");
                        log.error(ex);
                    }
                }
            }
        }


    }
}
