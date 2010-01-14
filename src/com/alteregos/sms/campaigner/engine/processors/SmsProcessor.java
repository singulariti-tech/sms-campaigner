package com.alteregos.sms.campaigner.engine.processors;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.business.IncomingMessageType;
import com.alteregos.sms.campaigner.business.OutgoingMessageType;
import com.alteregos.sms.campaigner.data.dto.Dnd;
import com.alteregos.sms.campaigner.data.dto.IncomingMessage;
import com.alteregos.sms.campaigner.data.dto.OutgoingMessage;
import com.alteregos.sms.campaigner.engine.processors.helpers.RuleProcessResult;
import com.alteregos.sms.campaigner.engine.IProcessor;
import com.alteregos.sms.campaigner.services.DndService;
import com.alteregos.sms.campaigner.services.MessageService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * @author John Emmanuel
 */
public class SmsProcessor implements IProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsProcessor.class);
    private MessageService messageService;
    private DndService dndService;
    private String defaultMessage;
    private final String DEFAULT_MESSAGE_DISABLED_WARNING = "-- Received a message that did not match any rules. "
            + "In such circumstances, a default message should be sent. However, the option for sending the default "
            + "message has been explicitly disabled. As such no message will be sent";

    public SmsProcessor() {
        LOGGER.debug("** SmsProcessor()");
        messageService = Main.getApplication().getBean("messageService");
        dndService = Main.getApplication().getBean("dndService");
    }

    @Override
    public void process() {
        LOGGER.debug(">> process()");
        RuleProcessor ruleProcessor = new RuleProcessor();
        List<IncomingMessage> inboxList = filter(messageService.getIncomingMessages());
        List<OutgoingMessage> outboxList = new ArrayList<OutgoingMessage>();
        for (IncomingMessage sms : inboxList) {
            RuleProcessResult result = ruleProcessor.process(sms.getContent());
            String content = result.getContent();
            String reference = null;
            if (result.isRule()) {
                if (result.isDndRule()) {
                    reference = registerDnd(sms.getSenderNo());
                    content = (reference != null) ? content + reference : content;
                }

                if (!content.equals("") && (result.isDndRule() && reference != null)) {
                    LOGGER.debug("-- queueing message in outbox");
                    OutgoingMessage reply = new OutgoingMessage();
                    reply.setRecepientNo(sms.getSenderNo());
                    reply.setContent(content);
                    reply.setType(OutgoingMessageType.AUTO_REPLY);
                    outboxList.add(reply);
                    sms.setProcess(true);
                } else if (result.isDndRule() && reference == null) {
                    LOGGER.debug("-- requester already registered in DND");
                    OutgoingMessage reply = new OutgoingMessage();
                    reply.setRecepientNo(sms.getSenderNo());
                    reply.setContent("You are already registered on our DND list.");
                    reply.setType(OutgoingMessageType.AUTO_REPLY);
                    outboxList.add(reply);
                    sms.setProcess(true);
                } else {
                    if (defaultMessage != null) {
                        LOGGER.debug("-- sending default message");
                        OutgoingMessage reply = new OutgoingMessage();
                        reply.setRecepientNo(sms.getSenderNo());
                        reply.setContent(getDefaultMessage());
                        outboxList.add(reply);
                        sms.setProcess(true);
                    } else {
                        LOGGER.info(DEFAULT_MESSAGE_DISABLED_WARNING);
                        LOGGER.info("-- Details of the message: Mobile no. {}, Message content: {}", sms.getSenderNo(),
                                sms.getContent());
                    }
                }
            } else {
                LOGGER.debug("-- This is most probably a message received from vendors or the like. Will not process it");
            }
        }

        try {
            if (outboxList.size() > 0) {
                messageService.newOutgoingMessages(outboxList);
                messageService.updateIncomingMessages(inboxList);
            }
        } catch (Exception rollbackException) {
            LOGGER.error("-- Error when processing messages received: {}", rollbackException);
        }
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    private List<IncomingMessage> filter(List<IncomingMessage> inputList) {
        List<IncomingMessage> outputList = new ArrayList<IncomingMessage>();
        for (IncomingMessage sms : inputList) {
            if (!sms.getType().equals(IncomingMessageType.STATUS_REPORT) && !sms.isProcess()) {
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
            dndService.insert(dnd);
            success = true;
        } catch (Exception rollbackException) {
            LOGGER.error("-- Error when registering DND for mobile no. {}", mobileNo);
            LOGGER.error("-- Error details: {}", rollbackException);
            success = false;
        }

        String reference = success ? String.valueOf(dnd.getDndId()) : null;
        return reference;
    }
}
