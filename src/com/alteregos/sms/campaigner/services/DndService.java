package com.alteregos.sms.campaigner.services;

import com.alteregos.sms.campaigner.data.dao.DndDao;
import com.alteregos.sms.campaigner.data.dto.Dnd;
import com.alteregos.sms.campaigner.data.exceptions.DaoException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author John Emmanuel
 */
public class DndService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DndService.class);
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
            LOGGER.error("-- insert(dnd): {}", e);
            transactionManager.rollback(transactionStatus);
            throw new DaoException(e);
        }
        transactionManager.commit(transactionStatus);
        return dndId;
    }
}
