package com.alteregos.sms.campaigner.data.dao;

import com.alteregos.sms.campaigner.data.dto.IncomingMessage;
import com.alteregos.sms.campaigner.data.dto.IncomingMessagePk;

import java.util.List;

/**
 * @author john.emmanuel
 */
public interface IncomingMessageDao {
    IncomingMessage findById (int incomingMessageId);
    List<IncomingMessage> findAll ();
    IncomingMessagePk insert(IncomingMessage message);
    void update(IncomingMessagePk pk, IncomingMessage message);
}
