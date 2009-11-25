package com.alteregos.sms.campaigner.data.dao;

import com.alteregos.sms.campaigner.data.dto.Dnd;
import com.alteregos.sms.campaigner.data.dto.DndPk;

import java.util.List;

/**
 * @author john.emmanuel
 */
public interface DndDao {
    Dnd findById (int dndId);
    List<Dnd> findAll ();
    DndPk insert(Dnd dnd);
    void update(DndPk pk, Dnd dnd);
}
