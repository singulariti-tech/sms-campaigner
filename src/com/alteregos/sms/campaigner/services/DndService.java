package com.alteregos.sms.campaigner.services;

import com.alteregos.sms.campaigner.data.dao.DndDao;
import com.alteregos.sms.campaigner.data.dto.Dnd;
import java.util.List;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author John Emmanuel
 */
public class DndService {

    private DndDao dndDao;
    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setDndDao(DndDao dndDao) {
        this.dndDao = dndDao;
    }

    public List<Dnd> findAll() {
        return dndDao.findAll();
    }

    public int insert(Dnd dnd) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
        int dndId = 0;
        try {
            dndId = dndDao.insert(dnd);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
        transactionManager.commit(transactionStatus);
        return dndId;
    }
}
