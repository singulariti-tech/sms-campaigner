package com.alteregos.sms.campaigner.services;

import com.alteregos.sms.campaigner.business.MessageStatus;
import com.alteregos.sms.campaigner.data.dao.IncomingMessageDao;
import com.alteregos.sms.campaigner.data.dao.OutgoingMessageDao;
import com.alteregos.sms.campaigner.data.dto.IncomingMessage;
import com.alteregos.sms.campaigner.data.dto.OutgoingMessage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author john.emmanuel
 * @version
 * @since
 *
 */
public class MessageService {

    private IncomingMessageDao incomingMessageDao;
    private OutgoingMessageDao outgoingMessageDao;

    public void setIncomingMessageDao(IncomingMessageDao incomingMessageDao) {
        this.incomingMessageDao = incomingMessageDao;
    }

    public void setOutgoingMessageDao(OutgoingMessageDao outgoingMessageDao) {
        this.outgoingMessageDao = outgoingMessageDao;
    }

    public List<IncomingMessage> getIncomingMessages() {
        return new ArrayList<IncomingMessage>();
    }

    public IncomingMessage getIncomingMessage(int messageId) {
        return incomingMessageDao.findById(messageId);
    }

    public int newIncomingMessage(IncomingMessage incomingMessage) {
        return incomingMessageDao.insert(incomingMessage);
    }

    public int[] newIncomingMessages(List<IncomingMessage> incomingMessages) {
        return incomingMessageDao.insert(incomingMessages);
    }

    public List<OutgoingMessage> getOutgoingMessages() {
        return new ArrayList<OutgoingMessage>();
    }

    public List<OutgoingMessage> getOutgoingMessages(MessageStatus status) {
        return new ArrayList<OutgoingMessage>();
    }

    public OutgoingMessage getOutgoingMessage(int messageId) {
        return outgoingMessageDao.findById(messageId);
    }

    public int newOutgoingMessage(OutgoingMessage outgoingMessage) {
        return outgoingMessageDao.insert(outgoingMessage);
    }

    public int[] newOutgoingMessages(List<OutgoingMessage> outgoingMessages) {
        return outgoingMessageDao.insert(outgoingMessages);
    }

    public void updateOutgoingMessage(OutgoingMessage outgoingMessage) {
        outgoingMessageDao.update(outgoingMessage);
    }
}
