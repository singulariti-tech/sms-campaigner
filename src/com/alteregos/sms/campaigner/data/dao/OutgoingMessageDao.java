package com.alteregos.sms.campaigner.data.dao;

import com.alteregos.sms.campaigner.data.dto.OutgoingMessage;
import com.alteregos.sms.campaigner.business.MessageStatus;

import java.util.List;

/**
 * @author john.emmanuel
 */
public interface OutgoingMessageDao {
    OutgoingMessage findById (int outgoingMessageId);
    List<OutgoingMessage> findAll ();
    List<OutgoingMessage> findByStatus (MessageStatus status);
    int insert(OutgoingMessage message);
    int[] insert(List<OutgoingMessage> messages);
    void update(OutgoingMessage message);
}
