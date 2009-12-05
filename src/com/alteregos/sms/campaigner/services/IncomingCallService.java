package com.alteregos.sms.campaigner.services;

import com.alteregos.sms.campaigner.data.dao.IncomingCallDao;
import com.alteregos.sms.campaigner.data.dto.IncomingCall;
import com.alteregos.sms.campaigner.data.dto.IncomingCallPk;
import java.util.List;

/**
 *
 * @author John
 */
public class IncomingCallService {

    private IncomingCallDao incomingCallDao;

    public void setIncomingCallDao(IncomingCallDao incomingCallDao) {
        this.incomingCallDao = incomingCallDao;
    }

    public List<IncomingCall> findAll() {
        return incomingCallDao.findAll();
    }

    public int newIncomingCall(IncomingCall call) {
        IncomingCallPk pk = incomingCallDao.insert(call);
        return pk != null ? pk.getIncomingCallId() : 0;
    }
}
