package com.alteregos.sms.campaigner.services;

import com.alteregos.sms.campaigner.data.dao.DndDao;
import com.alteregos.sms.campaigner.data.dto.Dnd;
import com.alteregos.sms.campaigner.data.dto.DndPk;
import java.util.List;

/**
 *
 * @author John Emmanuel
 */
public class DndService {

    private DndDao dndDao;

    public void setDndDao(DndDao dndDao) {
        this.dndDao = dndDao;
    }

    public List<Dnd> findAll() {
        return dndDao.findAll();
    }

    public int insert(Dnd dnd) {
        DndPk pk = dndDao.insert(dnd);
        return (pk != null) ? pk.getDndId() : 0;
    }
}
