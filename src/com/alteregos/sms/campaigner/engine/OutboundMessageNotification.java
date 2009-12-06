package com.alteregos.sms.campaigner.engine;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.business.FailureCause;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import com.alteregos.sms.campaigner.business.MessageStatus;
import com.alteregos.sms.campaigner.data.dto.OutgoingMessage;
import com.alteregos.sms.campaigner.services.MessageService;
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
    private MessageService messageService;
    private final int MAX_TRIES = 2;

    public OutboundMessageNotification() {
        messageService = Main.getApplication().getBean("messageService");
    }

    @Override
    public void process(String gtwId, OutboundMessage outboundMessage) {
        String messageId = outboundMessage.getId();
        OutgoingMessage outboxMessage = getMessage(messageId);
        if (outboundMessage == null) {
            log.warn("Outbox message with id: " + messageId + " not found. Most probably deleted");
        }

        if (outboxMessage != null && outboundMessage.getMessageStatus().equals(MessageStatuses.SENT)) {
            outboxMessage.setSentDate(outboundMessage.getDispatchDate());
            outboxMessage.setStatus(MessageStatus.SUCCESSFULLY_SENT);
            outboxMessage.setReferenceNo(outboundMessage.getRefNo());
            outboxMessage.setGatewayId(outboundMessage.getGatewayId());
            log.debug("Message sent successfully");
        } else if (outboxMessage != null && outboundMessage.getMessageStatus().equals(MessageStatuses.FAILED)) {
            FailureCause cause = FailureCause.getFailureCause(outboundMessage.getFailureCause());
            log.error("Sending message to recepient " + outboundMessage.getRecipient() + " failed. Reason - " + cause.getLabel());
            int noTries = outboxMessage.getErrors() + 1;
            if (noTries > MAX_TRIES) {
                outboxMessage.setErrors((short) noTries);
                outboxMessage.setStatus(MessageStatus.FAILED);
            } else {
                outboxMessage.setErrors((short) noTries);
                outboxMessage.setStatus(MessageStatus.UNSENT);
            }
        }

        try {            
            messageService.updateOutgoingMessage(outboxMessage);
        } catch (Exception rollbackException) {
            log.error("Exception when processing outbound message");
            log.error(rollbackException);
        }
    }

    private OutgoingMessage getMessage(String messageId) {
        Integer id = Integer.parseInt(messageId);
        OutgoingMessage message = messageService.getOutgoingMessage(id);
        return message;
    }
}
