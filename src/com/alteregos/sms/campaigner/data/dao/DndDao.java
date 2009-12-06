package com.alteregos.sms.campaigner.data.dao;

import com.alteregos.sms.campaigner.data.dto.Dnd;

import java.util.List;

/**
 * @author john.emmanuel
 */
public interface DndDao {
    Dnd findById (int dndId);
    List<Dnd> findAll ();
    int insert(Dnd dnd);
    void update(Dnd dnd);
}
