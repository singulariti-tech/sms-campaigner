package com.alteregos.sms.campaigner.services;

import com.alteregos.sms.campaigner.data.dao.IncomingCallDao;
import com.alteregos.sms.campaigner.data.dto.IncomingCall;
import com.alteregos.sms.campaigner.data.dto.IncomingCallPk;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author John
 */
@Service("incomingCallService")
public class IncomingCallService {

    @Autowired(required = true)
    @Qualifier("sqliteIncomingCallDao")
    private IncomingCallDao incomingCallDao;

    public List<IncomingCall> findAll() {
        return incomingCallDao.findAll();
    }

    public int newIncomingCall(IncomingCall call) {
        IncomingCallPk pk = incomingCallDao.insert(call);
        return pk != null ? pk.getIncomingCallId() : 0;
    }
}
