package com.alteregos.sms.campaigner.engine.processors;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.business.IncomingMessageType;
import com.alteregos.sms.campaigner.business.OutgoingMessageType;
import com.alteregos.sms.campaigner.data.dto.Dnd;
import com.alteregos.sms.campaigner.data.dto.IncomingMessage;
import com.alteregos.sms.campaigner.data.dto.OutgoingMessage;
import com.alteregos.sms.campaigner.engine.processors.helpers.RuleProcessResult;
import com.alteregos.sms.campaigner.engine.IProcessor;
import com.alteregos.sms.campaigner.services.MessageService;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.RollbackException;
import org.apache.log4j.Logger;

/**
 * //TODO Insert or update messages wherever required
 * @author John Emmanuel
 */
public class SmsProcessor implements IProcessor {

    private static Logger log = LoggerHelper.getLogger();
    private MessageService messageService;
    private String defaultMessage;

    public SmsProcessor() {
        messageService = Main.getApplication().getBean("messageService");
    }

    @Override
    public void process() {
        log.debug("Processing messages received");
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
                    log.debug("Queueing message in outbox");
                    OutgoingMessage reply = new OutgoingMessage();
                    reply.setRecepientNo(sms.getSenderNo());
                    reply.setContent(content);
                    reply.setType(OutgoingMessageType.AUTO_REPLY);
                    outboxList.add(reply);
                    sms.setProcess(true);
                } else if (result.isDndRule() && reference == null) {
                    log.debug("Requester already registered");
                    OutgoingMessage reply = new OutgoingMessage();
                    reply.setRecepientNo(sms.getSenderNo());
                    reply.setContent("You are already registered on our DND list.");
                    reply.setType(OutgoingMessageType.AUTO_REPLY);
                    outboxList.add(reply);
                    sms.setProcess(true);
                } else {
                    if (defaultMessage != null) {
                        log.debug("Sending default message");
                        OutgoingMessage reply = new OutgoingMessage();
                        reply.setRecepientNo(sms.getSenderNo());
                        reply.setContent(getDefaultMessage());
                        outboxList.add(reply);
                        sms.setProcess(true);
                    } else {
                        log.info("Default message disabled...so will not send sms");
                        log.info("Details of the message: " + sms.getSenderNo() + " - " + sms.getContent());
                    }
                }
            } else {
                log.debug("This is most probably a message received from vendors or the like. Will not process it");
            }
        }

        try {
            //Persist all outgoing messages            
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
            //persist
            success = true;
        } catch (Exception rollbackException) {
            log.error("Error when registering DND");
            log.error("Mobile no. of registrant: " + mobileNo);
            log.error("Error details: " + rollbackException);
            success = false;
        }

        String reference = success ? String.valueOf(dnd.getDndId()) : null;
        return reference;
    }
}
