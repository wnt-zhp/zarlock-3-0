package cx.ath.jbzdak.zarlok.db.dao;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.dao.AbstractDAO;
import cx.ath.jbzdak.zarlok.entities.Dzien;

public class DzienDao extends AbstractDAO<Dzien>{

	public DzienDao(DBManager manager) {
		super(manager, Dzien.class);
	}

}
