package com.alteregos.sms.campaigner.services;

import com.alteregos.sms.campaigner.data.dao.IncomingCallDao;
import com.alteregos.sms.campaigner.data.dto.IncomingCall;
import java.util.List;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author John
 */
public class IncomingCallService {

    private IncomingCallDao incomingCallDao;
    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setIncomingCallDao(IncomingCallDao incomingCallDao) {
        this.incomingCallDao = incomingCallDao;
    }

    public List<IncomingCall> findAll() {
        return incomingCallDao.findAll();
    }

    public int newIncomingCall(IncomingCall call) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        int incomingCallId = 0;
        try {
            incomingCallId = incomingCallDao.insert(call);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
        transactionManager.commit(transactionStatus);
        return incomingCallId;
    }

    public void updateIncomingCalls(List<IncomingCall> calls) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        int[] counts = null;
        try {
            counts = incomingCallDao.update(calls);
            //TODO Verify counts
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
        transactionManager.commit(transactionStatus);
    }
}
