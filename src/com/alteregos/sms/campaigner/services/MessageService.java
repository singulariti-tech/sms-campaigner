package com.alteregos.sms.campaigner.services;

import com.alteregos.sms.campaigner.business.MessageStatus;
import com.alteregos.sms.campaigner.data.dao.IncomingMessageDao;
import com.alteregos.sms.campaigner.data.dao.OutgoingMessageDao;
import com.alteregos.sms.campaigner.data.dto.IncomingMessage;
import com.alteregos.sms.campaigner.data.dto.OutgoingMessage;
import com.alteregos.sms.campaigner.data.exceptions.DaoException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
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
        List<IncomingMessage> messages = null;
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            messages = incomingMessageDao.findAll();
        } catch (Exception e) {
            LOGGER.error("-- getIncomingMessages(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
        return messages;
    }

    public IncomingMessage getIncomingMessage(int messageId) {
        IncomingMessage message = null;
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            message = incomingMessageDao.findById(messageId);
        } catch (Exception e) {
            LOGGER.error("-- getIncomingMessage(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
        return message;
    }

    public synchronized int newIncomingMessage(IncomingMessage incomingMessage) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        int incomingMessageId = 0;
        try {
            incomingMessageDao.insert(incomingMessage);
        } catch (Exception e) {
            LOGGER.error("-- newIncomingMessage(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
        return incomingMessageId;
    }

    public synchronized int[] newIncomingMessages(List<IncomingMessage> incomingMessages) {
        int[] counts = null;
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            counts = incomingMessageDao.insert(incomingMessages);
        } catch (Exception e) {
            LOGGER.error("-- newIncomingMessages(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
        return counts;
    }

    public synchronized void updateIncomingMessages(List<IncomingMessage> messages) {
        int[] counts = null;
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            counts = incomingMessageDao.update(messages);
            //TODO Verify update counts
        } catch (Exception e) {
            LOGGER.error("-- updateIncomingMessages(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
    }

    public List<OutgoingMessage> getOutgoingMessages() {
        List<OutgoingMessage> messages = null;
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            messages = outgoingMessageDao.findAll();
        } catch (Exception e) {
            LOGGER.error("-- getOutgoingMessages(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
        return messages;
    }

    public List<OutgoingMessage> getOutgoingMessages(MessageStatus status) {
        List<OutgoingMessage> messages = null;
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            messages = outgoingMessageDao.findByStatus(status);
        } catch (Exception e) {
            LOGGER.error("-- getOutgoingMessages(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
        return messages;
    }

    public OutgoingMessage getOutgoingMessage(int messageId) {
        return outgoingMessageDao.findById(messageId);
    }

    public synchronized int newOutgoingMessage(OutgoingMessage outgoingMessage) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        int outgoingMessageId = 0;
        try {
            outgoingMessageId = outgoingMessageDao.insert(outgoingMessage);
        } catch (Exception e) {
            LOGGER.error("-- newOutgoingMessage(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
        return outgoingMessageId;
    }

    public synchronized int[] newOutgoingMessages(List<OutgoingMessage> outgoingMessages) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        int[] counts = null;
        try {
            counts = outgoingMessageDao.insert(outgoingMessages);
        } catch (Exception e) {
            LOGGER.error("-- newOutgoingMessages(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
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
            LOGGER.error("-- updateOutgoingMessage(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
    }

    public synchronized void updateOutgoingMessages(List<OutgoingMessage> outgoingMessages) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            int[] counts = outgoingMessageDao.update(outgoingMessages);
            //TODO Verify counts
        } catch (Exception e) {
            LOGGER.error("-- updateOutgoingMessages(): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
    }
}
