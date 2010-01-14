package com.alteregos.sms.campaigner.engine;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.business.FailureCause;
import com.alteregos.sms.campaigner.business.MessageStatus;
import com.alteregos.sms.campaigner.data.dto.OutgoingMessage;
import com.alteregos.sms.campaigner.services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.IOutboundMessageNotification;
import org.smslib.MessageStatuses;
import org.smslib.OutboundMessage;

/**
 *
 * @author John Emmanuel
 */
public class OutboundMessageNotification implements IOutboundMessageNotification {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutboundMessageNotification.class);
    private MessageService messageService;
    private final int MAX_TRIES = 2;

    public OutboundMessageNotification() {
        LOGGER.debug("** OutboundMessageNotification()");
        messageService = Main.getApplication().getBean("messageService");
    }

    @Override
    public void process(String gtwId, OutboundMessage outboundMessage) {
        LOGGER.debug(">> process()");
        String messageId = outboundMessage.getId();
        OutgoingMessage outboxMessage = getMessage(messageId);
        if (outboundMessage == null) {
            LOGGER.warn("-- Outbox message with id: {} not found. Most probably deleted", messageId);
        }

        if (outboxMessage != null && outboundMessage.getMessageStatus().equals(MessageStatuses.SENT)) {
            outboxMessage.setSentDate(outboundMessage.getDispatchDate());
            outboxMessage.setStatus(MessageStatus.SUCCESSFULLY_SENT);
            outboxMessage.setReferenceNo(outboundMessage.getRefNo());
            outboxMessage.setGatewayId(outboundMessage.getGatewayId());
            LOGGER.debug("-- Message sent successfully");
        } else if (outboxMessage != null && outboundMessage.getMessageStatus().equals(MessageStatuses.FAILED)) {
            FailureCause cause = FailureCause.getFailureCause(outboundMessage.getFailureCause());
            LOGGER.error("-- Sending message to recepient {} failed. Reason - {}", outboundMessage.getRecipient(),
                    cause.getLabel());
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
            LOGGER.error("-- Exception when processing outbound message: {}", rollbackException);
        }
    }

    private OutgoingMessage getMessage(String messageId) {
        Integer id = Integer.parseInt(messageId);
        OutgoingMessage message = messageService.getOutgoingMessage(id);
        return message;
    }
}
