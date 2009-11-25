/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.engine.receivers;

import com.alteregos.sms.campaigner.data.beans.Calls;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import org.apache.log4j.Logger;

/**
 *
 * @author John Emmanuel
 */
public class CallReceiver {

    private static Logger log = LoggerHelper.getLogger();
    private EntityManager entityManager;

    public CallReceiver() {
        entityManager = javax.persistence.Persistence.createEntityManagerFactory("absolute-smsPU").createEntityManager();
    }

    public void receive(String gatewayId, String callerId) {
        log.error("Call received from " + callerId);
        Calls call = new Calls();
        call.setCaller(callerId);
        call.setGatewayId(gatewayId);
        call.setProcess(false);
        call.setReceiptDate(new Date());
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(call);
            entityManager.getTransaction().commit();
            System.out.println("Call from " + callerId + " logged.");
        } catch (RollbackException rollbackException) {
            log.error("Error when processing Call from " + callerId);
            log.error(rollbackException);
        }
    }
}
