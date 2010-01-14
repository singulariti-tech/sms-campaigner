package com.alteregos.sms.campaigner.engine.receivers;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.business.Encoding;
import com.alteregos.sms.campaigner.business.IncomingMessageType;
import com.alteregos.sms.campaigner.data.dto.IncomingMessage;
import com.alteregos.sms.campaigner.services.MessageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(InboundMessageReceiver.class);
    private MessageService messageService;

    public InboundMessageReceiver() {
        LOGGER.debug("** InboundMessageReceiver()");
        messageService = Main.getApplication().getBean("messageService");
    }

    public void receive(List<InboundMessage> inboundMessages, Service service) {
        LOGGER.debug(">> receive()");
        List<IncomingMessage> inboxList = new ArrayList<IncomingMessage>();
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
                encoding = Encoding.SEVEN_BIT.toString();
            } else if (msg.getEncoding().equals(MessageEncodings.ENC8BIT)) {
                encoding = Encoding.EIGHT_BIT.toString();
            } else if (msg.getEncoding().equals(MessageEncodings.ENCUCS2)) {
                encoding = Encoding.UNICODE_UCS2.toString();
            }
            IncomingMessageType type = null;
            if (msg.getType().equals(MessageTypes.INBOUND)) {
                type = IncomingMessageType.USER_MESSAGE;
            } else if (msg.getType().equals(MessageTypes.STATUSREPORT)) {
                type = IncomingMessageType.STATUS_REPORT;
            }
            IncomingMessage sms = new IncomingMessage();
            sms.setContent(content);
            sms.setSenderNo(sender);
            sms.setEncoding(Encoding.getEncoding(encoding));
            sms.setMessageDate(messageDate);
            sms.setReceiptDate(receiptDate);
            sms.setType(type);
            sms.setGatewayId(gatewayId);

            inboxList.add(sms);
            LOGGER.debug("<< receive()");
        }

        boolean commited = false;
        try {
            int[] counts = messageService.newIncomingMessages(inboxList);
            commited = true;
            //TODO Check insert counts
        } catch (Exception rollbackException) {
            LOGGER.error("-- Error when receiving inbound message: {}", rollbackException);
            commited = false;
        } finally {
            if (commited) {
                for (InboundMessage message : inboundMessages) {
                    try {
                        service.deleteMessage(message);
                    } catch (TimeoutException ex) {
                        LOGGER.error("-- Timeout when trying to delete messages on SIM: {}", ex);
                    } catch (GatewayException ex) {
                        LOGGER.error("-- Gateway exception when trying to delete messages on SIM: {}", ex);
                    } catch (IOException ex) {
                        LOGGER.error("-- IO Exception when trying to delete messages on SIM: {}", ex);
                    } catch (InterruptedException ex) {
                        LOGGER.error("-- Interrupted exception when trying to delete messages on SIM: {}", ex);
                    }
                }
            }
        }
    }
}
