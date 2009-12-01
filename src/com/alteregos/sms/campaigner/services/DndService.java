package com.alteregos.sms.campaigner.services;

import com.alteregos.sms.campaigner.data.dao.DndDao;
import com.alteregos.sms.campaigner.data.dto.Dnd;
import com.alteregos.sms.campaigner.data.dto.DndPk;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author John Emmanuel
 */
@Service("dndService")
public class DndService {

    @Autowired(required = true)
    @Qualifier("sqliteDndDao")
    private DndDao dndDao;

    public List<Dnd> findAll() {
        return dndDao.findAll();
    }

    public int insert(Dnd dnd) {
        DndPk pk = dndDao.insert(dnd);
        return (pk != null) ? pk.getDndId() : 0;
    }
}
