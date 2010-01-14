package com.alteregos.sms.campaigner.data.dao;

import com.alteregos.sms.campaigner.data.dto.IncomingCall;

import java.util.List;

/**
 * @author john.emmanuel
 */
public interface IncomingCallDao {
    IncomingCall findById (int incomingCallId);
    List<IncomingCall> findAll ();
    int insert(IncomingCall call);
    void update(IncomingCall call);
    int[] update(List<IncomingCall> calls);
}
