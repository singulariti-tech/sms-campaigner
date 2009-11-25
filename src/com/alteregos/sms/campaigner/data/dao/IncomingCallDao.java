package com.alteregos.sms.campaigner.data.dao;

import com.alteregos.sms.campaigner.data.dto.IncomingCall;
import com.alteregos.sms.campaigner.data.dto.IncomingCallPk;

import java.util.List;

/**
 * @author john.emmanuel
 */
public interface IncomingCallDao {
    IncomingCall findById (int incomingCallId);
    List<IncomingCall> findAll ();
    IncomingCallPk insert(IncomingCall call);
    void update(IncomingCallPk pk, IncomingCall call);
}
