package com.alteregos.sms.campaigner.services;

import com.alteregos.sms.campaigner.business.MessageStatus;
import com.alteregos.sms.campaigner.data.dao.IncomingMessageDao;
import com.alteregos.sms.campaigner.data.dao.OutgoingMessageDao;
import com.alteregos.sms.campaigner.data.dto.IncomingMessage;
import com.alteregos.sms.campaigner.data.dto.OutgoingMessage;
import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

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
    private PlatformTransactionManager transactionManager;

    public void setIncomingMessageDao(IncomingMessageDao incomingMessageDao) {
        this.incomingMessageDao = incomingMessageDao;
    }

    public void setOutgoingMessageDao(OutgoingMessageDao outgoingMessageDao) {
        this.outgoingMessageDao = outgoingMessageDao;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public List<IncomingMessage> getIncomingMessages() {
        return new ArrayList<IncomingMessage>();
    }

    public IncomingMessage getIncomingMessage(int messageId) {
        return incomingMessageDao.findById(messageId);
    }

    public int newIncomingMessage(IncomingMessage incomingMessage) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        int incomingMessageId = 0;
        try {
            incomingMessageDao.insert(incomingMessage);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
        transactionManager.commit(transactionStatus);
        return incomingMessageId;
    }

    public int[] newIncomingMessages(List<IncomingMessage> incomingMessages) {
        int[] counts = null;
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            counts = incomingMessageDao.insert(incomingMessages);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
        transactionManager.commit(transactionStatus);
        return counts;
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
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        int outgoingMessageId = 0;
        try {
            outgoingMessageId = outgoingMessageDao.insert(outgoingMessage);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
        transactionManager.commit(transactionStatus);
        return outgoingMessageId;
    }

    public int[] newOutgoingMessages(List<OutgoingMessage> outgoingMessages) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        int[] counts = null;
        try {
            counts = outgoingMessageDao.insert(outgoingMessages);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
        transactionManager.commit(transactionStatus);
        return counts;
    }

    public void updateOutgoingMessage(OutgoingMessage outgoingMessage) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            outgoingMessageDao.update(outgoingMessage);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
        transactionManager.commit(transactionStatus);
    }
}
