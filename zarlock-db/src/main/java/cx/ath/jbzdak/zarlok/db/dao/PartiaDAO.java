package cx.ath.jbzdak.zarlok.db.dao;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.dao.AbstractDAO;
import cx.ath.jbzdak.zarlok.entities.Partia;

public class PartiaDAO extends AbstractDAO<Partia>{

	public PartiaDAO(DBManager manager) {
		super(manager, Partia.class);
	}

}
