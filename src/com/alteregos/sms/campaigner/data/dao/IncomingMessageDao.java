package com.alteregos.sms.campaigner.data.dao;

import com.alteregos.sms.campaigner.data.dto.IncomingMessage;

import java.util.List;

/**
 * @author john.emmanuel
 */
public interface IncomingMessageDao {
    IncomingMessage findById (int incomingMessageId);
    List<IncomingMessage> findAll ();    
    int insert(IncomingMessage message);
    int[] insert(List<IncomingMessage> messages);
    void update(IncomingMessage message);
    int[] update(List<IncomingMessage> messages);
}
