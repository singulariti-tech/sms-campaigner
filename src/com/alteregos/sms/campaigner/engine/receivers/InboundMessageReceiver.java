package com.alteregos.sms.campaigner.engine.receivers;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.business.Encoding;
import com.alteregos.sms.campaigner.business.IncomingMessageType;
import com.alteregos.sms.campaigner.data.dto.IncomingMessage;
import com.alteregos.sms.campaigner.services.MessageService;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private MessageService messageService;

    public InboundMessageReceiver() {
        messageService = Main.getApplication().getBean("messageService");
    }

    public void receive(List<InboundMessage> inboundMessages, Service service) {
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
        }

        boolean commited = false;
        try {
            int[] counts = messageService.newIncomingMessages(inboxList);
            commited = true;
            //TODO Check insert counts
        } catch (Exception rollbackException) {
            log.error("Error when receiving inbound message");
            log.error(rollbackException);
            commited = false;
        } finally {
            //TODO Delete only those messages that were successfully commited
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
